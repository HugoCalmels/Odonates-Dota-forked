package com.palladium46.odonatesdota.security._refreshToken.repository;

import com.palladium46.odonatesdota.security._refreshToken.model.RefreshToken;
import com.palladium46.odonatesdota.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    @Modifying
    int deleteByUser(User user);

}
