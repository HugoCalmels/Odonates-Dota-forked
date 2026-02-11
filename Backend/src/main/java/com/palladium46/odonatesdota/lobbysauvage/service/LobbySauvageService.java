package com.palladium46.odonatesdota.lobbysauvage.service;

import com.palladium46.odonatesdota.exceptions.EntityNotFoundException;
import com.palladium46.odonatesdota.exceptions.LobbyFullException;
import com.palladium46.odonatesdota.lobbysauvage.model.LobbySauvage;
import com.palladium46.odonatesdota.lobbysauvage.model.LobbyStatus;
import com.palladium46.odonatesdota.lobbysauvage.repository.LobbySauvageRepository;
import com.palladium46.odonatesdota.security.service.SecurityService;
import com.palladium46.odonatesdota.sse.SseController;
import com.palladium46.odonatesdota.user.model.User;
import com.palladium46.odonatesdota.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LobbySauvageService {
//TODO Optimize the saves with cascades I have la flemme la
    private final SecurityService securityService;
    private final LobbySauvageRepository lobbySauvageRepository;
    private final UserRepository userRepository;
    private final SseController sseController;

    public LobbySauvageService(SecurityService securityService,
                               LobbySauvageRepository lobbySauvageRepository,
                               UserRepository userRepository,
                               SseController sseController) {
        this.securityService = securityService;
        this.lobbySauvageRepository = lobbySauvageRepository;
        this.userRepository = userRepository;
        this.sseController = sseController;
    }

    public LobbySauvage createLobby() throws Exception {
        LobbySauvage out = new LobbySauvage();
        User user = securityService.getFullCurrentUser();
        //TODO create only if user not in lobby
        out.setStatus(LobbyStatus.WAITING_FOR_PLAYER);
        out.addUser(user);
        out = lobbySauvageRepository.save(out);
        user.setLobbySauvage(out);
        userRepository.save(user);

        sseController.updateListLobbies();
        return out;
    }

    public LobbySauvage joinLobby(Long id) throws Exception {
        LobbySauvage lobby = lobbySauvageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lobby id : " + id + " not found"));
        if (lobby.getUsers().size() >= 10) {
            throw new LobbyFullException("The lobby is full");
        }
        User user = securityService.getFullCurrentUser();
        lobby.addUser(user);
        user.setLobbySauvage(lobby);
        if (lobby.getUsers().size() == 10) {
            lobby.setStatus(LobbyStatus.READY);
        }
        userRepository.save(user);
        sseController.updateListLobbies();
        return lobbySauvageRepository.save(lobby);
    }

    public List<LobbySauvage> getAllLobby() {
        return lobbySauvageRepository.findAll();
    }

    public boolean leave() throws Exception {
        User user = securityService.getFullCurrentUser();

        if (user.getLobbySauvage() == null) {
            return true;
        }
        LobbySauvage lobbySauvage = user.getLobbySauvage();
        user.setLobbySauvage(null);
        lobbySauvage.removeUser(user);
        sseController.updateEventLobbies(lobbySauvage.getId());
        userRepository.save(user);
        if (lobbySauvage.getUsers().isEmpty()) {
            lobbySauvageRepository.delete(lobbySauvage);
        } else {
            lobbySauvageRepository.save(lobbySauvage);
        }
        sseController.updateListLobbies();

        return true;
    }

    public LobbySauvage getUserCurrentLobby() throws Exception {
        return securityService.getCurrentUser().getLobbySauvage();
    }
}
