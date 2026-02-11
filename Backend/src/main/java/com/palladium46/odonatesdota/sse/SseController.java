package com.palladium46.odonatesdota.sse;

import com.palladium46.odonatesdota.scrim.service.ScrimService;
import com.palladium46.odonatesdota.security.service.SecurityService;
import com.palladium46.odonatesdota.team.service.TeamService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@CrossOrigin(origins = "https://app.dota-arena.fr")
@RestController()
@RequestMapping("/sse")
public class SseController {
    private final SecurityService securityService;
    private final ScrimService scrimService;
    private final TeamService teamService;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public SseController(ScrimService scrimService, SecurityService securityService, TeamService teamService) {
        this.scrimService = scrimService;
        this.securityService = securityService;
        this.teamService = teamService;
    }

    private final CopyOnWriteArrayList<LobbySseEmitter> emittersList = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<LobbySseEmitter> emittersEvent = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<SseEmitter> teamListEmitters = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<SseEmitter> scrimListEmitters = new CopyOnWriteArrayList<>();

    @GetMapping("/lobby-sauvage-list-event")
    public SseEmitter streamListLobbies() {
        LobbySseEmitter emitter = new LobbySseEmitter();
        initEmitter(this.emittersList, emitter);
        sendInitialEvent(emitter, "Connection Established for list");
        return emitter;
    }

    @GetMapping("/lobby-sauvage-event")
    public SseEmitter streamLobbies() throws Exception {
        long lobbyId = 44L;
        LobbySseEmitter emitter = new LobbySseEmitter(lobbyId, securityService.getCurrentUser().getId());
        initEmitter(this.emittersEvent, emitter);
        sendInitialEvent(emitter, "Connection Established for event");
        return emitter;
    }

    @GetMapping("/team-list-event")
    public SseEmitter streamTeamListEvent() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        initEmitter(this.teamListEmitters, emitter);
        sendInitialEvent(emitter, "Connection Established for team-list-event");
        return emitter;
    }

    @GetMapping("/scrim-list-event")
    public SseEmitter streamScrimListEvent() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        initEmitter(this.scrimListEmitters, emitter);
        sendInitialEvent(emitter, "Connection Established for scrim-list-event");
        return emitter;
    }

    private <T extends SseEmitter> void initEmitter(CopyOnWriteArrayList<T> emitters, T emitter) {
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));
    }

    private <T extends SseEmitter> void sendInitialEvent(T emitter, String message) {
        try {
            emitter.send(SseEmitter.event().name("INIT").data(message));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
    }

    public void updateListLobbies() {
        executorService.submit(() -> {
            System.out.println("NEW DATA LIST : list size = " + emittersList.size() + " " + Math.random());
            for (LobbySseEmitter emitter : emittersList) {
                sendUpdate(emitter, "Updated Lobby list");
            }
        });
    }

    public void updateEventLobbies(Long id) {
        for (LobbySseEmitter emitter : emittersEvent) {
            if (emitter.getLobbyId().equals(id)) {
                sendUpdate(emitter, "Updated Lobby event");
            }
        }
    }

    public void triggerTeamListUpdateEvent(String message) {
        executorService.submit(() -> {
            System.out.println("Updating team list emitters, size: " + teamListEmitters.size());
            for (SseEmitter emitter : teamListEmitters) {
                sendUpdate(emitter, "TEAMLIST_UPDATE: " + message);
            }
            System.out.println("Event TEAMLIST_UPDATE sent to all emitters");
        });
    }

    public void triggerScrimListUpdateEvent(String message) {
        executorService.submit(() -> {
            System.out.println("Updating scrim list emitters, size: " + scrimListEmitters.size());
            for (SseEmitter emitter : scrimListEmitters) {
                sendUpdate(emitter, "SCRIMLIST_UPDATE: " + message);
            }
        });
    }

    private <T extends SseEmitter> void sendToEmitters(CopyOnWriteArrayList<T> emitters, String eventName, String data) {
        synchronized (emitters) { // Synchronize to prevent concurrent modifications
            Iterator<T> iterator = emitters.iterator();
            while (iterator.hasNext()) {
                T emitter = iterator.next();
                try {
                    emitter.send(SseEmitter.event().name(eventName).data(data));
                } catch (IOException e) {
                    emitter.completeWithError(e);
                    iterator.remove();  // Remove the emitter if an error occurs
                }
            }
        }
    }

    private <T extends SseEmitter> void sendUpdate(T emitter, String data) {
        try {
            emitter.send(data);
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
    }

}
