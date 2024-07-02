package fr.molec.extended_spotify_backend.spotify_access;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.molec.extended_spotify_backend.models.TokenResponseModel;
import fr.molec.extended_spotify_backend.models.UserProfileModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class SpotifyAccessService {

    @Value("${CLIENT_ID}")
    private String clientId;

    @Value("${CLIENT_SECRET}")
    private String clientSecret;

    @Value("${REDIRECT_URI}")
    private String redirectUri;

    @Value("${SPOTIFY_ACCOUNT_BASE_URL}")
    private String accountBaseUrl;

    @Value("${ENDPOINT_TOKEN}")
    private String endpointToken;

    @Value("${SPOTIFY_API_BASE_URL}")
    private String apiBaseUrl;

    @Value("${ENDPOINT_USER_PROFILE}")
    private String endpointUserProfile;


    private final RestClient defaultClient = RestClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TokenResponseModel getToken(String code) throws JsonProcessingException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", redirectUri);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);

        String response = defaultClient.post()
                .uri(accountBaseUrl + endpointToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(params)
                .retrieve()
                .body(String.class);

        return objectMapper.readValue(response, TokenResponseModel.class);
    }

    public UserProfileModel getUserProfile(TokenResponseModel tokenResponseModel) throws JsonProcessingException {
        String response = defaultClient.get()
                .uri(apiBaseUrl + endpointUserProfile)
                .header("Authorization", tokenResponseModel.getTokenType() + " " + tokenResponseModel.getAccessToken())
                .retrieve()
                .body(String.class);

        return objectMapper.readValue(response, UserProfileModel.class);
    }

}
