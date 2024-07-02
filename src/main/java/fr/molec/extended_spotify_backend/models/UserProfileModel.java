package fr.molec.extended_spotify_backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserProfileModel {

    @JsonProperty("country")
    private String country;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("explicit_content")
    private ExplicitContent explicitContent;

    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;

    @JsonProperty("followers")
    private Followers followers;

    @JsonProperty("href")
    private String href;

    @JsonProperty("id")
    private String id;

    @JsonProperty("images")
    private List<UserImage> images;

    @JsonProperty("product")
    private String product;

    @JsonProperty("type")
    private String type;

    @JsonProperty("uri")
    private String uri;


    @Getter
    public static class ExplicitContent {

        @JsonProperty("filter_enabled")
        private boolean filterEnabled;

        @JsonProperty("filter_locked")
        private boolean filterLocked;
    }

    @Getter
    public static class ExternalUrls {

        @JsonProperty("spotify")
        private String spotify;
    }

    @Getter
    public static class Followers {

        @JsonProperty("href")
        private String href;

        @JsonProperty("total")
        private int total;
    }

    @Getter
    public static class UserImage {

        @JsonProperty("url")
        private String url;

        @JsonProperty("height")
        private int height;

        @JsonProperty("width")
        private int width;
    }
}
