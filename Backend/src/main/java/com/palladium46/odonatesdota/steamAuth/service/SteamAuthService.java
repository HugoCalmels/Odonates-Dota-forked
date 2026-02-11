package com.palladium46.odonatesdota.steamAuth.service;

import com.palladium46.odonatesdota.exceptions.*;
import com.palladium46.odonatesdota.security._refreshToken.model.RefreshToken;
import com.palladium46.odonatesdota.security._refreshToken.service.RefreshTokenService;
import com.palladium46.odonatesdota.security.service.JwtService;
import com.palladium46.odonatesdota.security.service.SecurityService;
import com.palladium46.odonatesdota.security.service.UserInfoService;
import com.palladium46.odonatesdota.user.service.UserService;
import com.palladium46.odonatesdota.utils.CookiesUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SteamAuthService {

    RestTemplate restTemplate = new RestTemplate();

    @Value("${frontend.interface}")
    private String frontendInterface;

    @Value("${jwt.expiration.ms}")
    private int accessTokenExpirationMs;

    @Value("${jwt.refresh.expiration.ms}")
    private int refreshTokenExpirationMs;

    private final UserService userService;

    private final SecurityService securityService;

    private final RefreshTokenService refreshTokenService;

    private final JwtService jwtService;

    private final UserInfoService userInfoService;

    public SteamAuthService(UserService userService, SecurityService securityService, RefreshTokenService refreshTokenService, JwtService jwtService, UserInfoService userInfoService) {
        this.userService = userService;
        this.securityService = securityService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.userInfoService = userInfoService;
    }

    public ResponseEntity<String> processLogin(@RequestParam Map<String, String> params,String currentRoute,HttpServletResponse httpServletResponse) throws Exception {

        try {
            if (isValidOpenIDResponse(params)) {
                System.out.println("PROCESSING WITH A VALID OPENID RESPONSE, CREATING/EDITING THE USER");
                Map<String, String> verificationParams = buildVerificationParams(params);
                HttpHeaders headers = buildHttpHeaders();
                MultiValueMap<String, String> body = buildRequestBody(verificationParams);
                HttpEntity<MultiValueMap<String, String>> requestEntity = buildRequestEntity(headers, body);

                ResponseEntity<String> responseEntity = sendRequestToSteamAPI(requestEntity);

                String verificationResponse = responseEntity.getBody();

                if (isValidSteamLogin(verificationResponse, params)) {

                    String steamId64bits = getSteamIdFromClaimedId(params);

                    System.out.println(steamId64bits);

                    try {
                        userService.saveOrUpdate(steamId64bits);
                    } catch (BannedUserException e) {
                        // Renvoie un statut 403 FORBIDDEN au frontend si l'utilisateur est banni
                        return new ResponseEntity<>("User is banned", HttpStatus.FORBIDDEN);
                    }

                    handleResponse(httpServletResponse, steamId64bits);

                    String encodedRoute = currentRoute.replace(" ", "%20");
                    String redirectUrl = frontendInterface + encodedRoute;

                    HttpHeaders responseHeaders = new HttpHeaders();
                    responseHeaders.setLocation(new URI(redirectUrl));

                    return new ResponseEntity<>(responseHeaders, HttpStatus.SEE_OTHER);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("User credentials not valid");
        }

        return null;
    }


    // ---------- Méthodes auxiliaires --------------

    private boolean isValidOpenIDResponse(Map<String, String> params) {
        return "id_res".equalsIgnoreCase(params.get("openid.mode"));
    }

    private Map<String, String> buildVerificationParams(Map<String, String> params) {
        Map<String, String> verificationParams = new HashMap<>();
        verificationParams.put("openid.assoc_handle", params.get("openid.assoc_handle"));
        verificationParams.put("openid.signed", params.get("openid.signed"));
        verificationParams.put("openid.sig", params.get("openid.sig"));
        verificationParams.put("openid.ns", "http://specs.openid.net/auth/2.0");
        verificationParams.put("openid.mode", "check_authentication");

        if (params.containsKey("openid.signed")) {
            String openidSigned = params.get("openid.signed");
            if (openidSigned != null) {
                String[] signed = openidSigned.split(",");
                for (String item : signed) {
                    String val = params.get("openid." + item);
                    verificationParams.put("openid." + item, val);
                }
            } else {
                throw new BadRequestException("Steam credentials openid.signed is null");
            }
        } else {
            throw new BadRequestException("Steam credentials are missing params");
        }
        return verificationParams;
    }

    private HttpHeaders buildHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_FORM_URLENCODED));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private MultiValueMap<String, String> buildRequestBody(Map<String, String> verificationParams) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.setAll(verificationParams);
        return body;
    }

    private HttpEntity<MultiValueMap<String, String>> buildRequestEntity(HttpHeaders headers, MultiValueMap<String, String> body) {
        return new HttpEntity<>(body, headers);
    }

    private ResponseEntity<String> sendRequestToSteamAPI(HttpEntity<MultiValueMap<String, String>> requestEntity) {
        return restTemplate.postForEntity("https://steamcommunity.com/openid/login", requestEntity, String.class);
    }

    private boolean isValidSteamLogin(String verificationResponse, Map<String, String> params) {
        return verificationResponse != null && verificationResponse.contains("is_valid:true");
    }

    private String getSteamIdFromClaimedId(Map<String, String> params) {
        Pattern pattern = Pattern.compile("https://steamcommunity.com/openid/id/(\\d+)");
        Matcher matcher = pattern.matcher(params.get("openid.claimed_id"));
        return matcher.find() ? matcher.group(1) : null;
    }

    private void handleResponse(HttpServletResponse response, String steamId64bits){
        String token = jwtService.generateToken(steamId64bits);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(steamId64bits);
        CookiesUtils.addToCookie(response, "token", token, accessTokenExpirationMs);
        CookiesUtils.addToCookie(response, "refreshToken", refreshToken.getToken(), refreshTokenExpirationMs);
    }

}
