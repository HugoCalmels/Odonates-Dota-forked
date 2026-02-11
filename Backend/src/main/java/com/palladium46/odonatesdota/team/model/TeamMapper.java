package com.palladium46.odonatesdota.team.model;

import com.palladium46.odonatesdota.user.model.User;
import com.palladium46.odonatesdota.user.model.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TeamMapper {

    public static Team initTeam(String teamName, String teamPassword, User creator, String logoUrl, String fileName) {
        Team newTeam = new Team();
        newTeam.setName(teamName);
        newTeam.setPassword(teamPassword);
        newTeam.addUser(creator);
        newTeam.setLogo(logoUrl);
        newTeam.setCaptain(creator);
        newTeam.setCreatedAt(LocalDateTime.now());
        newTeam.setLogoName(fileName );
        newTeam.setStatus(TeamStatus.ACTIVE);
        return newTeam;
    }

    public static TeamDto toDto(Team team){
        if (team == null) return null;
        TeamDto out = new TeamDto();
        out.setName(team.getName());
        out.setUsers(team.getUsers().stream().map(UserMapper::toDtoWithoutTeams).collect(Collectors.toSet()));
        out.setLogo(team.getLogo());
        out.setCaptain(UserMapper.toDtoWithoutTeams(team.getCaptain()));
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String formattedDateTime = team.getCreatedAt().format(formatter);
        out.setCreatedAt(formattedDateTime);
        out.setLogoName(team.getLogoName());
        out.setId(String.valueOf(team.getId()));
        out.setStatus(team.getStatus());
        return out;
    }

    public static TeamDto toDtoWithoutUsers(Team team) {
        return mapTeamToDto(team);
    }

    private static TeamDto mapTeamToDto(Team team) {
        TeamDto out = new TeamDto();
        out.setName(team.getName());
        out.setLogo(team.getLogo());
        out.setCaptain(UserMapper.toDtoWithoutTeams(team.getCaptain()));
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String formattedDateTime = team.getCreatedAt().format(formatter);
        out.setCreatedAt(formattedDateTime);
        out.setLogoName(team.getLogoName());
        out.setId(String.valueOf(team.getId()));
        out.setStatus(team.getStatus());
        // Ne pas inclure les utilisateurs dans la sortie DTO
        return out;
    }
}
