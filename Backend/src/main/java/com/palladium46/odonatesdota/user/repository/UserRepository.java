package com.palladium46.odonatesdota.user.repository;

import com.palladium46.odonatesdota.team.model.Team;
import com.palladium46.odonatesdota.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUserName(String username);

    User findBySteamId64bits(String steamId64bits);

    ArrayList<User> findAll();

    void deleteById(UUID id);

    boolean existsById(UUID id);

    boolean existsBySteamId64bits(String steamId64bits);

    @Query("SELECT u FROM app_user u WHERE u.id IN :ids")
    Set<User> findUsersByIds(@Param("ids") List<UUID> ids);
}
