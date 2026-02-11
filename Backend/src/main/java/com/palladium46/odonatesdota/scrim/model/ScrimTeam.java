package com.palladium46.odonatesdota.scrim.model;

import com.palladium46.odonatesdota.team.model.Team;
import com.palladium46.odonatesdota.user.model.User;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class ScrimTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "squad_captain_id")
    private User squadCaptain;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "scrim_team_users",
            joinColumns = @JoinColumn(name = "scrim_team_id"),
            inverseJoinColumns = @JoinColumn(name = "app_user_id")
    )
    private Set<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> players) {
        this.users = players;
    }

    public User getSquadCaptain() {
        return squadCaptain;
    }

    public void setSquadCaptain(User squadCaptain) {
        this.squadCaptain = squadCaptain;
    }
}
