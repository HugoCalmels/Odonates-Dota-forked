package com.palladium46.odonatesdota.team.repository;

import com.palladium46.odonatesdota.team.model.Team;
import com.palladium46.odonatesdota.team.model.TeamStatus;
import com.palladium46.odonatesdota.user.model.User;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    ArrayList<Team> findAll();

    boolean existsByUsers(User user);

    ArrayList<Team> findByNameContainingIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String teamName);

    Team findByName(String name);

    @Nullable
    Team findTeamById(Long id);

    List<Team> findByStatusNot(TeamStatus status);

    Team findByNameAndStatusNot(String name, TeamStatus status);

    @Query("SELECT t FROM Team t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')) AND t.status != :status")
    List<Team> findByNameContainingIgnoreCaseAndStatusNot(@Param("name") String name, @Param("status") TeamStatus status);

    boolean existsByNameIgnoreCaseAndStatusNot(String name, TeamStatus status);
}
