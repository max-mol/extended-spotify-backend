package fr.molec.extended_spotify_backend.spotifyaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpotifyAccessController {

    @Autowired
    SpotifyAccessService spotifyAccessService;

    @GetMapping("/callback")
    public void getAuthorizationCode(@RequestParam String code) {
        spotifyAccessService.requestAccessAndRefreshTokens(code);
    }

}
