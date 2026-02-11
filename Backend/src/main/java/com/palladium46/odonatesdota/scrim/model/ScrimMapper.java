package com.palladium46.odonatesdota.scrim.model;

import com.palladium46.odonatesdota.scrim.model.enums.*;
import com.palladium46.odonatesdota.team.model.Team;
import com.palladium46.odonatesdota.team.model.TeamMapper;
import com.palladium46.odonatesdota.user.model.User;
import com.palladium46.odonatesdota.user.model.UserMapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class ScrimMapper {

    public static Scrim initScrim(GameMode gameMode,
                                  LocalDateTime startDate,
                                  Integer minMmrAccepted,
                                  Integer maxMmrAccepted,
                                  ScrimTeam firstScrimTeam,
                                  String lobbyName,
                                  String lobbyPassword) {
        Scrim newScrim = new Scrim();
        newScrim.setGameMode(gameMode);
        newScrim.setMinMmrAccepted(minMmrAccepted);
        newScrim.setMaxMmrAccepted(maxMmrAccepted);
        newScrim.setFirstScrimTeam(firstScrimTeam);
        newScrim.setSecondScrimTeam(null);
        newScrim.setGameStatus(GameStatus.NOT_STARTED);
        newScrim.setPreGameStatus(PreGameStatus.JOINABLE);
        newScrim.setLobbyName(lobbyName);
        newScrim.setLobbyPassword(lobbyPassword);
        newScrim.setStartDateTime(startDate);
        newScrim.setProposals(new ArrayList<>());
        return newScrim;
    }

    public static ScrimTeam initScrimTeam(User captain, Team currentTeam, Set<User> selectedPlayers){
        ScrimTeam newScrimTeam = new ScrimTeam();
        newScrimTeam.setSquadCaptain(captain);
        newScrimTeam.setTeam(currentTeam);
        newScrimTeam.setUsers(selectedPlayers);
        return newScrimTeam;
    }

    public static ScrimProposal initScrimProposal(Scrim scrim, ScrimTeam scrimTeam, Team team){
        ScrimProposal newScrimProposal = new ScrimProposal();
        newScrimProposal.setScrim(scrim);
        newScrimProposal.setProposerScrimTeam(scrimTeam);
        newScrimProposal.setProposerTeam(team);
        newScrimProposal.setStatus(ProposalStatus.PENDING);
        return newScrimProposal;
    }

    public static ScrimDto toDto(Scrim scrim, String timezone) {
        ScrimDto out = new ScrimDto();
        out.setId(scrim.getId());
        out.setMaxMmrAccepted(scrim.getMaxMmrAccepted());
        out.setMinMmrAccepted(scrim.getMinMmrAccepted());
        out.setFirstScrimTeam(scrimTeamToDto(scrim.getFirstScrimTeam()));
        out.setSecondScrimTeam(scrimTeamToDto(scrim.getSecondScrimTeam()));
        out.setGameMode(scrim.getGameMode());
        out.setGameStatus(scrim.getGameStatus());
        out.setPreGameStatus(scrim.getPreGameStatus());
        out.setLobbyName(scrim.getLobbyName());
        out.setLobbyPassword(scrim.getLobbyPassword());
        out.setProposals(mapScrimProposalToDto(scrim.getProposals()));

        if (timezone != null) {
            out.setStartDateTime(convertToTimeZone(scrim.getStartDateTime(), timezone));
        } else {
            out.setStartDateTime(scrim.getStartDateTime());
        }

        out.setFirstTeamHasFinished(scrim.isFirstTeamHasFinished());
        out.setSecondTeamHasFinished(scrim.isSecondTeamHasFinished());

        return out;
    }

    public static List<ScrimProposalDto> mapScrimProposalToDto(List<ScrimProposal> proposals){
        return proposals.stream()
                .map( proposal -> {
                    ScrimProposalDto proposalDto = new ScrimProposalDto();
                    proposalDto.setId(proposal.getId());
                    proposalDto.setProposerTeam(TeamMapper.toDtoWithoutUsers(proposal.getProposerTeam()));
                    proposalDto.setProposerScrimTeam(scrimTeamToDto(proposal.getProposerScrimTeam()));
                    proposalDto.setStatus(proposal.getStatus());
                    return proposalDto;
                })
                .collect(Collectors.toList());
    }

    public static ScrimTeamDto scrimTeamToDto(ScrimTeam scrimTeam) {
        if (scrimTeam == null) {
            return null;
        }
        ScrimTeamDto dto = new ScrimTeamDto();
        dto.setId(scrimTeam.getId());
        dto.setSquadCaptain(UserMapper.toDtoWithoutTeams(scrimTeam.getSquadCaptain()));
        dto.setTeam(TeamMapper.toDtoWithoutUsers(scrimTeam.getTeam()));
        dto.setUsers(scrimTeam.getUsers().stream()
                .map(UserMapper::toDtoWithoutTeams)
                .collect(Collectors.toSet()));
        return dto;
    }

    public static LocalDateTime convertToTimeZone(LocalDateTime localDateTime, String timeZoneId) {
        return localDateTime
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of(timeZoneId))
                .toLocalDateTime();
    }

}
