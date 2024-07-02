package fr.molec.extended_spotify_backend.spotify_access;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import fr.molec.extended_spotify_backend.models.TokenResponseModel;
import fr.molec.extended_spotify_backend.models.UserProfileModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest(httpPort = 8080)
class SpotifyAccessServiceTest {

    static String responseBodyToken = "{\"access_token\":\"BQAUqudYYPfvzkNRKdIjsH-O0Ssy00u2efc1Zp8tF2jmaTpjKPfRuSnNTHI5yb-KqO6D9III77Xb6w3uyCguK0-iKW_JcPH2HVsWMt95_drNxI0bgsDKoOPzWE4uiAaW42A2cqTW9idjb7BUi80A63nJFkK97OUt9X1RII8THGC3waVZg06G3g\",\"token_type\":\"Bearer\",\"expires_in\":3600,\"refresh_token\":\"AQBOtHnrLNRIz7nKeJWStD52nxVFtgeiRO7Hx2NbBQ2D5BhlCvKgezT_gOIR0qN-p9U7LrgkZdoh9IQobLo_nO4qOIZsLICIsEl_7FElPVYM7HBk2qJnXOAc4fUIPwrC3k4\"}";
    static String responseBodyUserProfile = """
            {
              "display_name" : "Florian Leca",
              "external_urls" : {
                "spotify" : "https://open.spotify.com/user/slashnroses17"
              },
              "href" : "https://api.spotify.com/v1/users/slashnroses17",
              "id" : "slashnroses17",
              "images" : [ {
                "url" : "https://scontent-bru2-1.xx.fbcdn.net/v/t1.6435-1/118627067_10218234200502778_7488138702910286581_n.jpg?stp=cp0_dst-jpg_p50x50&_nc_cat=101&ccb=1-7&_nc_sid=312bcd&_nc_ohc=Rwj2RV_ntL8Q7kNvgHmOKXX&_nc_ht=scontent-bru2-1.xx&edm=AP4hL3IEAAAA&oh=00_AYDHoEtwsM5pYmNI5_NQ4SpDM04JOpX_ZVr1xCLmAY0WCQ&oe=66AB403E",
                "height" : 64,
                "width" : 64
              }, {
                "url" : "https://scontent-bru2-1.xx.fbcdn.net/v/t1.6435-1/118627067_10218234200502778_7488138702910286581_n.jpg?stp=dst-jpg_p320x320&_nc_cat=101&ccb=1-7&_nc_sid=05c18e&_nc_ohc=Rwj2RV_ntL8Q7kNvgHmOKXX&_nc_ht=scontent-bru2-1.xx&edm=AP4hL3IEAAAA&oh=00_AYA99gU1mPkU6pw_3ure0JgWbfDMc8wigD7BC6vgB0AlGA&oe=66AB403E",
                "height" : 300,
                "width" : 300
              } ],
              "type" : "user",
              "uri" : "spotify:user:slashnroses17",
              "followers" : {
                "href" : null,
                "total" : 13
              }
            }""";

    private final String baseUrl = "http://localhost:8080/";
    private final String endpointToken = "/api/token";
    private final String endpointUserProfile = "/api/me";
    private SpotifyAccessService spotifyAccessService;

    @BeforeEach
    void setUp() {
        spotifyAccessService = new SpotifyAccessService();
        ReflectionTestUtils.setField(spotifyAccessService, "accountBaseUrl", baseUrl);
        ReflectionTestUtils.setField(spotifyAccessService, "apiBaseUrl", baseUrl);
        ReflectionTestUtils.setField(spotifyAccessService, "endpointToken", endpointToken);
        ReflectionTestUtils.setField(spotifyAccessService, "endpointUserProfile", endpointUserProfile);
    }

    @Test
    void getToken() throws JsonProcessingException {
        String code = "123456789azerty";
        stubFor(post(urlEqualTo(endpointToken))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBodyToken)));

        TokenResponseModel tokenResponseModel = spotifyAccessService.getToken(code);

        assertEquals("BQAUqudYYPfvzkNRKdIjsH-O0Ssy00u2efc1Zp8tF2jmaTpjKPfRuSnNTHI5yb-KqO6D9III77Xb6w3uyCguK0-iKW_JcPH2HVsWMt95_drNxI0bgsDKoOPzWE4uiAaW42A2cqTW9idjb7BUi80A63nJFkK97OUt9X1RII8THGC3waVZg06G3g", tokenResponseModel.getAccessToken());
        assertEquals("Bearer", tokenResponseModel.getTokenType());
        assertEquals(3600, tokenResponseModel.getExpiresIn());
        assertEquals("AQBOtHnrLNRIz7nKeJWStD52nxVFtgeiRO7Hx2NbBQ2D5BhlCvKgezT_gOIR0qN-p9U7LrgkZdoh9IQobLo_nO4qOIZsLICIsEl_7FElPVYM7HBk2qJnXOAc4fUIPwrC3k4", tokenResponseModel.getRefreshToken());
    }

    @Test
    void getUserProfile() throws JsonProcessingException {
        stubFor(get(urlEqualTo(endpointUserProfile))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBodyUserProfile)));

        UserProfileModel userProfileModel = spotifyAccessService.getUserProfile(new TokenResponseModel());

        assertEquals("Florian Leca", userProfileModel.getDisplayName());
        assertEquals("https://open.spotify.com/user/slashnroses17", userProfileModel.getExternalUrls().getSpotify());
        assertEquals("https://api.spotify.com/v1/users/slashnroses17", userProfileModel.getHref());
        assertEquals("slashnroses17", userProfileModel.getId());
        assertEquals("https://scontent-bru2-1.xx.fbcdn.net/v/t1.6435-1/118627067_10218234200502778_7488138702910286581_n.jpg?stp=cp0_dst-jpg_p50x50&_nc_cat=101&ccb=1-7&_nc_sid=312bcd&_nc_ohc=Rwj2RV_ntL8Q7kNvgHmOKXX&_nc_ht=scontent-bru2-1.xx&edm=AP4hL3IEAAAA&oh=00_AYDHoEtwsM5pYmNI5_NQ4SpDM04JOpX_ZVr1xCLmAY0WCQ&oe=66AB403E", userProfileModel.getImages().get(0).getUrl());
        assertEquals(64, userProfileModel.getImages().get(0).getHeight());
        assertEquals(64, userProfileModel.getImages().get(0).getWidth());
        assertEquals("user", userProfileModel.getType());
        assertEquals("spotify:user:slashnroses17", userProfileModel.getUri());
        assertEquals(13, userProfileModel.getFollowers().getTotal());
    }

}