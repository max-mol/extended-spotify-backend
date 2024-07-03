package fr.molec.extended_spotify_backend.spotify_access;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.molec.extended_spotify_backend.models.TokenResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class SpotifyAccessController {

    @Value("${REDIRECT_URI_FRONT}")
    private String redirectUriFront;

    @Autowired
    private SpotifyAccessService spotifyAccessService;

    @GetMapping("/callback")
    public void getAuthorization(@RequestParam String code) throws JsonProcessingException {
        System.out.println("Retrieved code: " + code);

        TokenResponseModel token = spotifyAccessService.getToken(code);
        System.out.println("Retrieved token: " + token.getAccessToken());

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(redirectUriFront + "?token=" + token.getAccessToken());
    }

}
