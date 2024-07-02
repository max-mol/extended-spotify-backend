package fr.molec.extended_spotify_backend.spotify_access;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.molec.extended_spotify_backend.models.TokenResponseModel;
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

    @Value("${SPOTIFY_BASE_URL}")
    private String baseUrl;

    @Value("${ENDPOINT_TOKEN}")
    private String endpointToken;

    private final RestClient defaultClient = RestClient.create();

    public TokenResponseModel getToken(String code) throws JsonProcessingException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", redirectUri);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);

        String response = defaultClient.post()
                .uri(baseUrl + endpointToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(params)
                .retrieve()
                .body(String.class);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response, TokenResponseModel.class);
    }

}
