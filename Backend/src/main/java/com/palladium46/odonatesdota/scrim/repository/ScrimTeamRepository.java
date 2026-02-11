package com.palladium46.odonatesdota.scrim.repository;

import com.palladium46.odonatesdota.scrim.model.ScrimTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScrimTeamRepository extends JpaRepository<ScrimTeam, Long> {
    @Query("SELECT COUNT(s) FROM Scrim s " +
            "WHERE (s.firstScrimTeam.team.id = :teamId OR s.secondScrimTeam.team.id = :teamId) " +
            "AND s.gameStatus = 'FINISHED'")
    long countByTeamIdAndScrimStatusFinished(@Param("teamId") Long teamId);
}
