package fr.molec.extended_spotify_backend.spotify_access;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.molec.extended_spotify_backend.models.TokenResponseModel;
import fr.molec.extended_spotify_backend.models.UserProfileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpotifyAccessController {

    @Autowired
    SpotifyAccessService spotifyAccessService;

    @GetMapping("/callback")
    public void getAuthorization(@RequestParam String code) throws JsonProcessingException {
        System.out.println("Retrieved code: " + code);

        TokenResponseModel token = spotifyAccessService.getToken(code);
        System.out.println("Retrieved token: " + token.getAccessToken());

        UserProfileModel userProfile = spotifyAccessService.getUserProfile(token);
        System.out.println("Retrieved user profile: " + userProfile.getDisplayName());
    }

}
