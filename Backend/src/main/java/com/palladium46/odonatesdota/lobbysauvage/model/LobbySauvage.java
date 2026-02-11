package com.palladium46.odonatesdota.lobbysauvage.model;

import com.palladium46.odonatesdota.user.model.User;
import com.palladium46.odonatesdota.user.model.UserMapper;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "lobby_sauvage")
public class LobbySauvage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "lobbySauvage", fetch = FetchType.EAGER)
    private Set<User> users;

    private LobbyStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDateTime;

    private String lobbyName;

    @PrePersist
    protected void onCreate() {
        createDateTime = new Date();
        lobbyName = generateLobbyName();
    }

    private String generateLobbyName() {
        String[] adjectives = {
                "Abyssal", "Ancient", "Arcane", "Blighted", "Chaotic",
                "Divine", "Ethereal", "Frosty", "Immortal", "Infernal",
                "Mystic", "Nether", "Primal", "Radiant", "Spectral",
                "Stormy", "Twilight", "Unholy", "Vengeful", "Vicious"
        };
        String[] nouns = {
                "Alchemist", "Anomaly", "Archon", "Artifact", "Avatar", "Barracks", "Behemoth", "Blade", "Blight",
                "Cataclysm", "Chronicle", "Citadel", "Conclave", "Conduit", "Crusader", "Crystal", "Curse",
                "Djinn", "Dominion", "Druid", "Elemental", "Enigma", "Epicenter", "Ethereal", "Forge",
                "Garrison", "Glyph", "Golem", "Guardian", "Harbinger", "Herald", "Illusion", "Invoker",
                "Juggernaut", "Knight", "Labyrinth", "Leviathan", "Maelstrom", "Magus", "Mantle", "Marauder",
                "Monolith", "Nemesis", "Obelisk", "Oracle", "Outcast", "Outpost", "Phantom", "Prophet",
                "Revenant", "Rogue", "Rune", "Sanctum", "Scourge", "Sentinel", "Serpent",
                "Shade", "Shaman", "Shrine", "Sorcerer", "Specter", "Spirit", "Tempest",
                "Throne", "Titan", "Totem", "Tribunal", "Vanguard", "Vestige", "Vigil", "Vortex", "Warden", "Warlock", "Zealot"
        };

        int adjectiveIndex = (int) (Math.random() * adjectives.length);
        int nounIndex = (int) (Math.random() * nouns.length);
        return adjectives[adjectiveIndex] + " " + nouns[nounIndex];
    }

    public LobbySauvageDto toDto() {
        return new LobbySauvageDto(this.id, this.users.stream().map(UserMapper::toDtoWithoutTeams).collect(Collectors.toSet()), this.status.toString(), this.createDateTime, this.lobbyName);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public LobbyStatus getStatus() {
        return status;
    }

    public void setStatus(LobbyStatus status) {
        this.status = status;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public void addUser(User user) {
        if (this.users == null) {
            this.users = new HashSet<>();
        }
        this.users.add(user);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }
}
