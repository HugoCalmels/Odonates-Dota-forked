package com.palladium46.odonatesdota.steamAuth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.palladium46.odonatesdota.exceptions.BadRequestException;
import com.palladium46.odonatesdota.exceptions.InternalServerErrorException;
import com.palladium46.odonatesdota.steamAuth.model.PlayerInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;

import static com.palladium46.odonatesdota.steamAuth.utils.SteamAuthUtils.steamId64ToSteamId32;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SteamApiInteractionService {

    @Value("${fedup.steam.api.key}")
    private String steamApiKey;

    @Value("${opendota.api.base_url}")
    private String opendotaApiBaseUrl;

    @Value("${steam.api}")
    private String steamApiEndpoint;

    public PlayerInfo getPlayerInfo(String steamId64bits) throws Exception {
        //String testId = "76561198048536965";
        String steamId32bits = steamId64ToSteamId32(steamId64bits);

        PlayerInfo playerInfo = getOpenDotaPlayerInfo(steamId32bits, steamId64bits);

        if (playerInfo == null) {
            playerInfo = getSteamPlayerInfo(steamId64bits);
        }

        return playerInfo;
    }

    public PlayerInfo getOpenDotaPlayerInfo(String steamId32bits, String steamId64bits) throws ServiceUnavailableException {
        String apiUrl = opendotaApiBaseUrl + "/players/" + steamId32bits;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            URI uri = new URI(apiUrl);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, JsonNode.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                JsonNode jsonNode = responseEntity.getBody();

                if (jsonNode != null && jsonNode.has("profile")) {
                    JsonNode profile = jsonNode.get("profile");

                    Map<String, String> steamData = fetchPersonaNameAndAvatarFromSteam(steamId64bits);
                    String name = steamData.getOrDefault("personaname", null);
                    String avatar = steamData.getOrDefault("avatar", null);

                    String profileUrl = profile.has("profileurl") ? profile.get("profileurl").asText() : null;
                    int rankTier = jsonNode.has("rank_tier") ? jsonNode.get("rank_tier").asInt() : 0;

                    Integer leaderboardRanking = jsonNode.has("leaderboard_rank") && !jsonNode.get("leaderboard_rank").isNull()
                            ? jsonNode.get("leaderboard_rank").asInt()
                            : null;

                    String loccountrycode = profile.has("loccountrycode") && !profile.get("loccountrycode").isNull()
                            ? profile.get("loccountrycode").asText()
                            : null;

                    return new PlayerInfo(name, avatar, rankTier, profileUrl, leaderboardRanking, loccountrycode);
                }
            }
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (URISyntaxException e) {
            throw new InternalServerErrorException("URISyntaxException: Failed to fetch data from OpenDota API");
        } catch (ResourceAccessException e) {
            throw new ServiceUnavailableException("OpenDota API is currently unavailable");
        } catch (Exception e) {
            throw new ServiceUnavailableException("Failed to fetch data from OpenDota API");
        }

        return null;
    }

    private Map<String, String> fetchPersonaNameAndAvatarFromSteam(String steamId64) {
        System.out.println("ONE FETCH STARTED - 1");
        String apiUrl = steamApiEndpoint + "/GetPlayerSummaries/v2/?key=" + steamApiKey + "&steamids=" + steamId64;
        Map<String, String> result = new HashMap<>();

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<JsonNode> responseEntity = restTemplate.getForEntity(apiUrl, JsonNode.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                JsonNode rootNode = responseEntity.getBody();
                JsonNode playersNode = rootNode.path("response").path("players");

                if (playersNode.isArray() && playersNode.size() > 0) {
                    JsonNode playerNode = playersNode.get(0);

                    result.put("personaname", playerNode.path("personaname").asText(null));
                    result.put("avatar", playerNode.path("avatar").asText(null));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public PlayerInfo getSteamPlayerInfo(String steamId64) throws Exception {
        System.out.println("ONE FETCH STARTED - 2");
        try {
            String apiUrl = steamApiEndpoint + "/GetPlayerSummaries/v0002/?key=" + steamApiKey + "&steamids=" + steamId64;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
            String responseBody = responseEntity.getBody();

            if (responseBody != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);

                JsonNode playersNode = rootNode.path("response").path("players");
                if (playersNode.isArray() && playersNode.size() > 0) {
                    JsonNode playerNode = playersNode.get(0);

                    PlayerInfo playerInfo = new PlayerInfo(
                            playerNode.path("personaname").asText(),
                            playerNode.path("avatar").asText(),
                            null,
                            playerNode.path("profileurl").asText(),
                            null,
                            null
                    );
                    return playerInfo;
                }

            }
        } catch (Exception e) {
            throw new BadRequestException("SteamId is not valid");
        }

        return null;
    }

    public String convertUrl(String badUrl) {
        if (badUrl.contains("/id/")) {
            return badUrl.replace("/id/", "/profiles/");
        } else {
            return badUrl;
        }
    }

    public boolean isPublicSteamProfile(String steamId64) throws Exception {
        // Méthode non utilisée, je la laisse au cas ou
        String apiUrl = steamApiEndpoint + "/GetFriendList/v0001/?key=" + steamApiKey + "&steamid=" + steamId64 + "&relationship=friend";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + steamApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        try{
            ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
            String responseBody = responseEntity.getBody();
            return responseBody != null && !responseBody.trim().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

}
