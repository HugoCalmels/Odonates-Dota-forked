package com.palladium46.odonatesdota.scrim.repository;

import com.palladium46.odonatesdota.scrim.model.Scrim;
import com.palladium46.odonatesdota.scrim.model.enums.GameStatus;
import com.palladium46.odonatesdota.team.model.Team;
import com.palladium46.odonatesdota.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface ScrimRepository extends JpaRepository<Scrim, Long> {

    Scrim findScrimById(long id);

    ArrayList<Scrim> findAll();

    List<Scrim> findByGameStatusNot(GameStatus status);

    @Query("SELECT COUNT(s) FROM Scrim s " +
            "WHERE s.firstScrimTeam.squadCaptain = :user AND s.gameStatus != 'FINISHED'")
    long countByFirstScrimTeamCaptainAndStatusNot(@Param("user") User user);


    @Query("SELECT s FROM Scrim s " +
            "LEFT JOIN s.firstScrimTeam fst " +
            "LEFT JOIN s.secondScrimTeam sst " +
            "WHERE (:user MEMBER OF fst.users OR :user MEMBER OF sst.users)")
    List<Scrim> findScrimsByUser(@Param("user") User user);

    List<Scrim> findByFirstScrimTeamTeam(Team team);

    List<Scrim> findBySecondScrimTeamTeam(Team team);
}
