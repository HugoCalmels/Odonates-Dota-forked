package com.palladium46.odonatesdota.scrim.model;

import com.palladium46.odonatesdota.team.model.Team;
import com.palladium46.odonatesdota.user.model.User;
import jakarta.persistence.*;

@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String message;

    @OneToOne
    @JoinColumn(name = "scrim_id")
    private Scrim scrim;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Scrim getScrim() {
        return scrim;
    }

    public void setScrim(Scrim scrim) {
        this.scrim = scrim;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
