package com.palladium46.odonatesdota.user.model;

import com.palladium46.odonatesdota.steamAuth.model.PlayerInfo;
import com.palladium46.odonatesdota.team.model.TeamMapper;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import static com.palladium46.odonatesdota.steamAuth.utils.SteamAuthUtils.steamId64ToSteamId32;

@Component
public class UserMapper {

    public static User initUser(String steamId64bits, Role role, PlayerInfo playerInfo) {
        User newUser = new User();
        newUser.setSteamId32bits(steamId64ToSteamId32(steamId64bits));
        newUser.setSteamId64bits(steamId64bits);
        newUser.setUserName(playerInfo.getName());
        newUser.setAvatar(playerInfo.getAvatar());
        newUser.setRankTier(playerInfo.getSeasonRank());
        newUser.addRole(role);
        newUser.setLastEdit(LocalDateTime.now());
        newUser.setProfileUrl(playerInfo.getProfileUrl());
        newUser.setLeaderboardRank(playerInfo.getLeaderboardRanking());
        newUser.setCountry(playerInfo.getCountry());

        return newUser;
    }

    public static UserDto toDto(User user) {
        UserDto out = mapUserToDto(user);
        out.setTeam(TeamMapper.toDto(user.getTeam()));
        return out;
    }

    //TODO faire mieux genre separer les endpoitns pour get les team members
    public static UserDto toDtoWithoutTeams(User user) {
        return mapUserToDto(user);
    }

    private static UserDto mapUserToDto(User user) {
        UserDto out = new UserDto();
        out.setId(user.getId().toString());
        out.setUserName(user.getUserName());
        out.setSteamId64bits(user.getSteamId64bits());
        out.setSteamId32bits(user.getSteamId32bits());
        out.setRoles(user.getRoles());
        out.setAvatar(user.getAvatar());
        out.setRankTier(user.getRankTier());
        out.setUserOrder(user.getUserOrder());
        out.setBanned(user.isBanned());
        out.setLeaderboardRank(user.getLeaderboardRank());
        out.setCountry(user.getCountry());

        return out;
    }

    public static User updateUserWithPlayerInfo(User existingUser, PlayerInfo playerInfo) {
        existingUser.setUserName(playerInfo.getName());
        existingUser.setRankTier(playerInfo.getSeasonRank());
        existingUser.setAvatar(playerInfo.getAvatar());
        existingUser.setLeaderboardRank(playerInfo.getLeaderboardRanking());
        existingUser.setCountry(playerInfo.getCountry());

        return existingUser;
    }
}
