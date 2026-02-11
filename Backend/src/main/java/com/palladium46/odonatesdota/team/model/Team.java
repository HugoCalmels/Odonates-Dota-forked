package com.palladium46.odonatesdota.team.model;

import com.palladium46.odonatesdota.scrim.model.ScrimTeam;
import com.palladium46.odonatesdota.user.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String name;

    private String password;

    private String logo;

    private String logoName;

    @OneToMany(mappedBy = "team")
    private Set<User> users;

    @ManyToOne
    @JoinColumn(name = "captain_id")
    private User captain;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column
    private String acronym;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private Set<ScrimTeam> scrimTeams;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TeamStatus status;

    public Team() {
        this.users = new LinkedHashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users.stream()
                .sorted(Comparator.comparingInt(User::getUserOrder))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        user.setUserOrder(users.size() + 1);
        users.add(user);
        user.setTeam(this);
    }

    public void removeUser(User user) {
        users.remove(user);
        user.setTeam(null);
        int order = 1;
        for (User u : users) {
            u.setUserOrder(order++);
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public User getCaptain() {
        return captain;
    }

    public void setCaptain(User captain) {
        this.captain = captain;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getLogoName() {
        return logoName;
    }

    public void setLogoName(String logoName) {
        this.logoName = logoName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public Set<ScrimTeam> getScrimTeams() {
        return scrimTeams;
    }

    public void setScrimTeams(Set<ScrimTeam> scrimTeams) {
        this.scrimTeams = scrimTeams;
    }

    public TeamStatus getStatus() {
        return status;
    }

    public void setStatus(TeamStatus status) {
        this.status = status;
    }
}
