package fr.molec.extended_spotify_backend.spotify_access;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import fr.molec.extended_spotify_backend.models.TokenResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest(httpPort = 8080)
class SpotifyAccessServiceTest {

    static String responseBody = "{\"access_token\":\"BQAUqudYYPfvzkNRKdIjsH-O0Ssy00u2efc1Zp8tF2jmaTpjKPfRuSnNTHI5yb-KqO6D9III77Xb6w3uyCguK0-iKW_JcPH2HVsWMt95_drNxI0bgsDKoOPzWE4uiAaW42A2cqTW9idjb7BUi80A63nJFkK97OUt9X1RII8THGC3waVZg06G3g\",\"token_type\":\"Bearer\",\"expires_in\":3600,\"refresh_token\":\"AQBOtHnrLNRIz7nKeJWStD52nxVFtgeiRO7Hx2NbBQ2D5BhlCvKgezT_gOIR0qN-p9U7LrgkZdoh9IQobLo_nO4qOIZsLICIsEl_7FElPVYM7HBk2qJnXOAc4fUIPwrC3k4\"}";

    private SpotifyAccessService spotifyAccessService;
    private final String baseUrl = "http://localhost:8080/";
    private final String endpointToken = "/api/token";

    @BeforeEach
    void setUp() {
        spotifyAccessService = new SpotifyAccessService();
        ReflectionTestUtils.setField(spotifyAccessService, "baseUrl", baseUrl);
        ReflectionTestUtils.setField(spotifyAccessService, "endpointToken", endpointToken);
    }

    @Test
    void getToken() throws JsonProcessingException {
        String code = "123456789azerty";
        stubFor(post(urlEqualTo(endpointToken))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));

        TokenResponseModel tokenResponseModel = spotifyAccessService.getToken(code);

        assertEquals("BQAUqudYYPfvzkNRKdIjsH-O0Ssy00u2efc1Zp8tF2jmaTpjKPfRuSnNTHI5yb-KqO6D9III77Xb6w3uyCguK0-iKW_JcPH2HVsWMt95_drNxI0bgsDKoOPzWE4uiAaW42A2cqTW9idjb7BUi80A63nJFkK97OUt9X1RII8THGC3waVZg06G3g", tokenResponseModel.getAccessToken());
        assertEquals("Bearer", tokenResponseModel.getTokenType());
        assertEquals(3600, tokenResponseModel.getExpiresIn());
        assertEquals("AQBOtHnrLNRIz7nKeJWStD52nxVFtgeiRO7Hx2NbBQ2D5BhlCvKgezT_gOIR0qN-p9U7LrgkZdoh9IQobLo_nO4qOIZsLICIsEl_7FElPVYM7HBk2qJnXOAc4fUIPwrC3k4", tokenResponseModel.getRefreshToken());
    }


}