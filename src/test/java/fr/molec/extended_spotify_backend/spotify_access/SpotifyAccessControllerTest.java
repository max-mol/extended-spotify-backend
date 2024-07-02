package fr.molec.extended_spotify_backend.spotify_access;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.molec.extended_spotify_backend.models.TokenResponseModel;
import fr.molec.extended_spotify_backend.models.UserProfileModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SpotifyAccessControllerTest {

    @Mock
    SpotifyAccessService spotifyAccessService;

    @Spy
    @InjectMocks
    SpotifyAccessController spotifyAccessController;

    @Test
    void getAuthorization() throws JsonProcessingException {
        String code = "test-code";

        TokenResponseModel tokenResponseModel = new TokenResponseModel();
        tokenResponseModel.setAccessToken("test-token");

        UserProfileModel userProfileModel = new UserProfileModel();
        userProfileModel.setDisplayName("Steven Tyler");

        when(spotifyAccessService.getToken(code)).thenReturn(tokenResponseModel);
        when(spotifyAccessService.getUserProfile(tokenResponseModel)).thenReturn(userProfileModel);

        spotifyAccessController.getAuthorization(code);

        verify(spotifyAccessService, times(1)).getToken(code);
        verify(spotifyAccessService, times(1)).getUserProfile(tokenResponseModel);
    }

}