package com.palladium46.odonatesdota.scrim.service;

import com.palladium46.odonatesdota.exceptions.MaxActiveScrimException;
import com.palladium46.odonatesdota.scrim.model.*;
import com.palladium46.odonatesdota.scrim.model.enums.GameStatus;
import com.palladium46.odonatesdota.scrim.model.enums.PreGameStatus;
import com.palladium46.odonatesdota.scrim.model.enums.ProposalStatus;
import com.palladium46.odonatesdota.scrim.model.responses.ScrimDetailsResponse;
import com.palladium46.odonatesdota.scrim.repository.ScrimProposalRepository;
import com.palladium46.odonatesdota.scrim.repository.ScrimRepository;
import com.palladium46.odonatesdota.scrim.repository.ScrimTeamRepository;
import com.palladium46.odonatesdota.security.service.SecurityService;
import com.palladium46.odonatesdota.sse.SseController;
import com.palladium46.odonatesdota.team.model.Team;
import com.palladium46.odonatesdota.user.model.User;
import com.palladium46.odonatesdota.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Service
public class ScrimService {

    int MIN_USERS = 5;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private final int MAXIMUM_ACTIVE_SCRIM_CREATED = 10;
    @Value("${application.acronym}")
    private String appAcronym;

    private static SecureRandom random = new SecureRandom();

    private final SecurityService securityService;

    private final ScrimRepository scrimRepository;

    private final ScrimProposalRepository scrimProposalRepository;

    private final UserService userService;

    private final ScrimTeamRepository scrimTeamRepository;

    private final SseController sseController;

    public  ScrimService(SecurityService securityService,
                         ScrimRepository scrimRepository,
                         ScrimProposalRepository scrimProposalRepository,
                         UserService userService,
                         ScrimTeamRepository scrimTeamRepository,
                         @Lazy SseController sseController) {
        this.securityService = securityService;
        this.scrimRepository = scrimRepository;
        this.scrimProposalRepository = scrimProposalRepository;
        this.userService = userService;
        this.scrimTeamRepository = scrimTeamRepository;
        this.sseController = sseController;
    }

    public ScrimDto saveScrim(Scrim scrim, String timezone, String[] selectedPlayerIds) throws Exception {
        User currentUser = securityService.getFullCurrentUser();
        verifyActiveScrimLimit(currentUser);

        Set<User> selectedUsers = findSelectedUsers(selectedPlayerIds);
        ScrimTeam newScrimTeam = createScrimTeam(currentUser, selectedUsers);
        scrimTeamRepository.save(newScrimTeam);

        Scrim newScrim = createScrim(scrim, timezone, newScrimTeam);
        sseController.triggerScrimListUpdateEvent("client update");
        return ScrimMapper.toDto(scrimRepository.save(newScrim), timezone);
    }

    public ResponseEntity<ScrimDto> getFullScrim(Long scrimId, String timezone) throws Exception {
        Scrim scrim = scrimRepository.findScrimById(scrimId);
        if (scrim == null) {
            throw new Exception("Scrim not found");
        }
        LocalDateTime currentUTCDateTime = LocalDateTime.now(ZoneOffset.UTC);
        Scrim refreshedScrim = refreshScrimStatus(scrim, currentUTCDateTime);
        if ( refreshedScrim != null ){
            scrimRepository.save(refreshedScrim);
            return ResponseEntity.ok(ScrimMapper.toDto(scrim, timezone));
        }
        return null;
    }

    public List<ScrimDto> getAllActiveScrims(String timezone) {
        List<Scrim> scrims = scrimRepository.findByGameStatusNot(GameStatus.FINISHED);
        LocalDateTime currentUTCDateTime = LocalDateTime.now(ZoneOffset.UTC);

        ConcurrentLinkedQueue<Scrim> scrimsToUpdate = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Scrim> scrimsToDelete = new ConcurrentLinkedQueue<>();

        List<Scrim> refreshedScrims = refreshScrims(scrims, currentUTCDateTime, scrimsToUpdate, scrimsToDelete);

        updateAndDeleteScrims(scrimsToUpdate, scrimsToDelete);

        return convertToScrimDtos(refreshedScrims, timezone);
    }

    public ResponseEntity<ScrimDto> joinScrim(Long scrimId) throws Exception {
        User currentUser = securityService.getFullCurrentUser();
        Team userTeam = currentUser.getTeam();
        Scrim currentScrim = scrimRepository.findScrimById(scrimId);

        currentScrim.setPreGameStatus(PreGameStatus.FULL);
        ScrimTeam secondScrimTeam = new ScrimTeam();
        secondScrimTeam.setTeam(userTeam);
        currentScrim.setSecondScrimTeam(secondScrimTeam);
        scrimRepository.save(currentScrim);
        return ResponseEntity.ok(ScrimMapper.toDto(currentScrim, null));
    }

    public ResponseEntity<Void> deleteScrim(Long scrimId) throws Exception {
        Scrim scrim = scrimRepository.findScrimById(scrimId);
        User currentUser = securityService.getFullCurrentUser();

        if (isTeamMember(currentUser, scrim.getFirstScrimTeam().getTeam())){
            scrimRepository.delete(scrim);
           sseController.triggerScrimListUpdateEvent("client update");
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            throw new Exception("Failed to delete Team");
        }
    }

    public ResponseEntity<ScrimDto> cancelScrim(Long scrimId) throws Exception {
        Scrim scrim = scrimRepository.findScrimById(scrimId);
        User currentUser = securityService.getFullCurrentUser();

        if (isTeamMember(currentUser, scrim.getSecondScrimTeam().getTeam())){
            scrim.setSecondScrimTeam(null);
            scrim.setPreGameStatus(PreGameStatus.JOINABLE);
            scrimRepository.save(scrim);
            return ResponseEntity.ok(ScrimMapper.toDto(scrim, null));
        } else {
            throw new Exception("Failed to cancel Team");
        }
    }

    public ResponseEntity<ScrimDetailsResponse> getScrimUserStatus(Long scrimId) throws Exception {
        User currentUser = securityService.getFullCurrentUser();
        Scrim currentScrim = scrimRepository.findScrimById(scrimId);
        ScrimDetailsResponse userStatus = new ScrimDetailsResponse();
        canUserJoinScrim(currentUser, currentScrim, userStatus);
        canUserDeleteScrim(currentUser, currentScrim, userStatus);
        canUserCancelScrim(currentUser, currentScrim, userStatus);
        isUserAlreadyTagged(currentUser, currentScrim, userStatus);
        setUserHasSentProposalStatus(currentUser.getTeam(), currentScrim, userStatus);

        return ResponseEntity.ok(userStatus);
    }

    public List<ScrimDto> getScrimHistory(String timezone) throws Exception {
        User currentUser = securityService.getFullCurrentUser();
        List<Scrim> scrims = scrimRepository.findScrimsByUser(currentUser);
        LocalDateTime currentUTCDateTime = LocalDateTime.now(ZoneOffset.UTC);

        ConcurrentLinkedQueue<Scrim> scrimsToUpdate = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Scrim> scrimsToDelete = new ConcurrentLinkedQueue<>();

        List<Scrim> refreshedScrims = refreshScrims(scrims, currentUTCDateTime, scrimsToUpdate, scrimsToDelete);

        updateAndDeleteScrims(scrimsToUpdate, scrimsToDelete);

        return convertToScrimHistoryDtos(refreshedScrims, timezone);
    }

    public ResponseEntity<ScrimDto> proposeScrim(Long scrimId, String[] selectedPlayerIds, String timezone) throws Exception {
        User currentUser = securityService.getFullCurrentUser();
        Scrim scrim = scrimRepository.findById(scrimId)
                .orElseThrow(() -> new Exception("Scrim not found"));

        if (hasUserSentProposal(currentUser.getTeam(), scrim)) {
            throw new Exception("You have already sent a proposal for this scrim.");
        }

        Set<User> selectedUsers = findSelectedUsers(selectedPlayerIds);
        ScrimTeam newScrimTeam = ScrimMapper.initScrimTeam(
                currentUser,
                currentUser.getTeam(),
                selectedUsers
        );
        scrimTeamRepository.save(newScrimTeam);

        ScrimProposal newProposal = ScrimMapper.initScrimProposal(
                scrim,
                newScrimTeam,
                currentUser.getTeam()
        );
        scrimProposalRepository.save(newProposal);

        scrim.addScrimProposal(newProposal);
        scrimRepository.save(scrim);
        return ResponseEntity.ok(ScrimMapper.toDto(scrim, timezone));
    }

    public ResponseEntity<ScrimDto> cancelProposedScrim(Long scrimId, String timezone) throws Exception {
        User currentUser = securityService.getFullCurrentUser();
        Scrim scrim = scrimRepository.findById(scrimId)
                .orElseThrow(() -> new Exception("Scrim not found"));

        if (!hasUserSentProposal(currentUser.getTeam(), scrim)) {
            throw new Exception("You haven't sent a proposal for this scrim.");
        }

        ScrimProposal proposal = scrimProposalRepository.findByProposerTeamAndScrim(currentUser.getTeam(), scrim);
        scrim.removeScrimProposal(proposal);
        scrimProposalRepository.delete(proposal);
        scrimRepository.save(scrim);

        return ResponseEntity.ok(ScrimMapper.toDto(scrim, timezone));
    }

    public ResponseEntity<ScrimDto> acceptScrimProposal(Long proposalId, String timezone) throws Exception {
        User currentUser = securityService.getFullCurrentUser();
        ScrimProposal proposal = scrimProposalRepository.findById(proposalId)
                .orElseThrow(() -> new Exception("Proposal not found"));

        Scrim scrim = proposal.getScrim();

        if (!isUserAdminOfScrim(currentUser, scrim)) {
            throw new Exception("You do not have permission to accept this proposal.");
        }

        rejectOtherProposals(scrim, proposalId);

        scrim.setSecondScrimTeam(proposal.getProposerScrimTeam());
        scrim.setPreGameStatus(PreGameStatus.FULL);
        scrim.setGameStatus(GameStatus.READY);
        proposal.setStatus(ProposalStatus.ACCEPTED);
        scrimProposalRepository.save(proposal);
        scrimRepository.save(scrim);
        LocalDateTime currentUTCDateTime = LocalDateTime.now(ZoneOffset.UTC);
        Scrim refreshedScrim = refreshScrimStatus(scrim,currentUTCDateTime);
        return ResponseEntity.ok(ScrimMapper.toDto(refreshedScrim, timezone));
    }

    public ResponseEntity<ScrimDto> rejectScrimProposal(Long proposalId, String timezone) throws Exception {
        User currentUser = securityService.getFullCurrentUser();
        ScrimProposal proposal = scrimProposalRepository.findById(proposalId)
                .orElseThrow(() -> new Exception("Proposal not found"));

        Scrim scrim = proposal.getScrim();

        if (!isUserAdminOfScrim(currentUser, scrim)) {
            throw new Exception("You do not have permission to reject this proposal.");
        }

        scrimProposalRepository.delete(proposal);
        return ResponseEntity.ok(ScrimMapper.toDto(scrim, timezone));
    }

    public ResponseEntity<Long> getNumberScrimsPlayedFromOneTeam(Long teamId){
        return ResponseEntity.ok(scrimTeamRepository.countByTeamIdAndScrimStatusFinished(teamId));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //
    // ~~~~~~~~~~~~~~~~~~~~~~~~~ Méthodes auxiliaires  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

    private void verifyActiveScrimLimit(User currentUser) throws MaxActiveScrimException {
        long activeScrimCount = scrimRepository.countByFirstScrimTeamCaptainAndStatusNot(currentUser);
        if (activeScrimCount >= MAXIMUM_ACTIVE_SCRIM_CREATED) {
            throw new MaxActiveScrimException("Maximum active Scrim entities reached !");
        }
    }

    private Set<User> findSelectedUsers(String[] selectedPlayerIds) {
        List<UUID> selectedUserIds = Arrays.stream(selectedPlayerIds)
                .map(UUID::fromString)
                .collect(Collectors.toList());
        return userService.findUsersByIds(selectedUserIds);
    }

    private ScrimTeam createScrimTeam(User currentUser, Set<User> selectedUsers) {
        return ScrimMapper.initScrimTeam(
                currentUser,
                currentUser.getTeam(),
                selectedUsers
        );
    }

    private Scrim createScrim(Scrim scrim, String timezone, ScrimTeam scrimTeam) {
        return ScrimMapper.initScrim(
                scrim.getGameMode(),
                setDateForUTC(scrim.getStartDateTime(), timezone),
                scrim.getMinMmrAccepted(),
                scrim.getMaxMmrAccepted(),
                scrimTeam,
                generateLobbyNameWithAppAcronym(),
                generateLobbyPassword()
        );
    }

    private String generateLobbyNameWithAppAcronym() {
        return appAcronym + ":" + generateLobbyName();
    }

    private List<Scrim> refreshScrims(List<Scrim> scrims, LocalDateTime currentUTCDateTime,
                                      ConcurrentLinkedQueue<Scrim> scrimsToUpdate, ConcurrentLinkedQueue<Scrim> scrimsToDelete) {
        return scrims.parallelStream()
                .peek(scrim -> {
                    Scrim updatedScrim = refreshScrimStatus(scrim, currentUTCDateTime);
                    if (updatedScrim == null) {
                        scrimsToDelete.add(scrim);
                    } else if (scrim.getGameStatus() != updatedScrim.getGameStatus()) {
                        scrimsToUpdate.add(updatedScrim);
                    }
                })
                .collect(Collectors.toList());
    }

    private void updateAndDeleteScrims(ConcurrentLinkedQueue<Scrim> scrimsToUpdate, ConcurrentLinkedQueue<Scrim> scrimsToDelete) {
        if (!scrimsToUpdate.isEmpty()) {
            scrimRepository.saveAll(new ArrayList<>(scrimsToUpdate));
        }
        if (!scrimsToDelete.isEmpty()) {
            scrimRepository.deleteAll(new ArrayList<>(scrimsToDelete));
        }
    }

    private List<ScrimDto> convertToScrimDtos(List<Scrim> scrims, String timezone) {
        return scrims.stream()
                .filter(scrim -> scrim.getGameStatus() != GameStatus.FINISHED)
                .sorted(Comparator.comparing(Scrim::getStartDateTime))
                .map(scrim -> ScrimMapper.toDto(scrim, timezone))
                .collect(Collectors.toList());
    }

    private List<ScrimDto> convertToScrimHistoryDtos(List<Scrim> scrims, String timezone) {
        return scrims.stream()
                .sorted(Comparator.comparing(Scrim::getStartDateTime).reversed())
                .map(scrim -> ScrimMapper.toDto(scrim, timezone))
                .collect(Collectors.toList());
    }

    private void rejectOtherProposals(Scrim scrim, Long acceptedProposalId) {
        List<ScrimProposal> otherProposals = scrim.getProposals()
                .stream()
                .filter(p -> !p.getId().equals(acceptedProposalId))
                .collect(Collectors.toList());

        otherProposals.forEach(p -> {
            p.setStatus(ProposalStatus.REJECTED);
            scrimProposalRepository.save(p);
        });
    }

    private boolean isUserAdminOfScrim(User user, Scrim scrim) {
        ScrimTeam firstScrimTeam = scrim.getFirstScrimTeam();
        if (firstScrimTeam != null) {
            Team userTeam = user.getTeam();
            Team firstTeam = firstScrimTeam.getTeam();
            return userTeam.equals(firstTeam);
        }
        return false;
    }

    public boolean hasUserSentProposal(Team team, Scrim scrim) {
        ScrimProposal pendingProposal = scrimProposalRepository.findFirstByProposerTeamAndScrimAndStatus(team, scrim, ProposalStatus.PENDING);
        return pendingProposal != null;
    }

    public static String generateRandomString(int length, String characters) {
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            randomString.append(characters.charAt(randomIndex));
        }
        return randomString.toString();
    }

    public static String generateLobbyName() {
        int length = getRandomLength(6, 8);
        return generateRandomString(length, CHARACTERS);
    }

    public static String generateLobbyPassword() {
        int length = getRandomLength(6, 8);
        return generateRandomString(length, CHARACTERS);
    }

    private static int getRandomLength(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public Scrim refreshScrimStatus(Scrim scrim, LocalDateTime currentUTCDateTime) {
        LocalDateTime startDateTime = scrim.getStartDateTime();
        LocalDateTime startDateTimeMinus15Minutes = startDateTime.minusMinutes(15);
        LocalDateTime startDateTimePlus1Hour = startDateTime.plusHours(1);

        if (scrim.getSecondScrimTeam() != null) {
            if (currentUTCDateTime.isAfter(startDateTimePlus1Hour)) {
                scrim.setGameStatus(GameStatus.FINISHED);
            } else if (currentUTCDateTime.isAfter(startDateTimeMinus15Minutes)) {
                scrim.setGameStatus(GameStatus.STARTED);
            }
        } else {
            if (currentUTCDateTime.isAfter(startDateTime)) {
                return null;
            }
        }
        return scrim;
    }

    public static LocalDateTime setDateForUTC(LocalDateTime localDateTime, String timezone) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of(timezone));
        LocalDateTime startDateTimeUTC = zonedDateTime
                .withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();
        return startDateTimeUTC;
    }

    public void isUserAlreadyTagged(User currentUser, Scrim currentScrim, ScrimDetailsResponse userStatus) throws Exception {
        userStatus.setAlreadyTagged(false);
        if (currentScrim.getFirstScrimTeam().getUsers().contains(currentUser)) {
            userStatus.setAlreadyTagged(true);
        } else if (currentScrim.getSecondScrimTeam() != null && currentScrim.getSecondScrimTeam().getUsers().contains(currentUser)) {
            userStatus.setAlreadyTagged(true);
        }
    }

    public void canUserJoinScrim(User currentUser, Scrim currentScrim, ScrimDetailsResponse userStatus) throws Exception {
        userStatus.setCanJoinScrim(false);
        Team currentUserTeam = currentUser.getTeam();
        if ( currentUserTeam != null && currentUserTeam.getUsers().size() >= 1 && currentUserTeam != currentScrim.getFirstScrimTeam().getTeam() ){
            userStatus.setCanJoinScrim(true);
        }
    }

    public  void canUserDeleteScrim(User currentUser, Scrim currentScrim, ScrimDetailsResponse userStatus) throws Exception {
        userStatus.setCanDeleteScrim(false);
        if (isTeamMember(currentUser, currentScrim.getFirstScrimTeam().getTeam())){
            userStatus.setCanDeleteScrim(true);
        }
    }

    public  void canUserCancelScrim(User currentUser, Scrim currentScrim, ScrimDetailsResponse userStatus) throws Exception {
        userStatus.setCanCancelScrim(false);
        if ( currentScrim.getSecondScrimTeam() != null && isTeamMember(currentUser, currentScrim.getSecondScrimTeam().getTeam())){
            userStatus.setCanCancelScrim(true);
        }
    }

    public boolean isTeamMember(User currentUser, Team team){
        if (team != null) {
            Set<User> teamMembers = team.getUsers();
            return teamMembers.contains(currentUser);
        }
        return false;
    }

    public void setUserHasSentProposalStatus(Team team, Scrim currentScrim, ScrimDetailsResponse userStatus){
        boolean teamHasSentProposal = hasUserSentProposal(team, currentScrim);
        userStatus.setHasAlreadySentProposal(teamHasSentProposal);
    }

    @Transactional
    public void cleanupNonFinishedScrimsForTeam(Team team) {
        List<Scrim> scrimsAsFirstTeam = scrimRepository.findByFirstScrimTeamTeam(team);

        scrimsAsFirstTeam.forEach(scrim -> {
            if (scrim.getGameStatus() == GameStatus.NOT_STARTED || scrim.getGameStatus() == GameStatus.READY ) {
                scrimRepository.delete(scrim);
            }
        });

        List<Scrim> scrimsAsSecondTeam = scrimRepository.findBySecondScrimTeamTeam(team);

        scrimsAsSecondTeam.forEach(scrim -> {
            if (scrim.getGameStatus() == GameStatus.NOT_STARTED || scrim.getGameStatus() == GameStatus.READY ) {
                scrim.setSecondScrimTeam(null);
                scrim.setGameStatus(GameStatus.NOT_STARTED);
                scrim.setPreGameStatus(PreGameStatus.JOINABLE);
                scrimRepository.save(scrim);
            }
        });

        List<ScrimProposal> scrimProposals = scrimProposalRepository.findByProposerTeam(team);

        scrimProposals.forEach(scrimProposal -> {
            scrimProposalRepository.delete(scrimProposal);
        });
    }

}
