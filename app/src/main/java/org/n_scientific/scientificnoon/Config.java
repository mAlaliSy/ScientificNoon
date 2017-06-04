package org.n_scientific.scientificnoon;

/**
 * Created by mohammad on 29/05/17.
 */

public final class Config { // Avoid extending container class..

    private Config() { // Avoid creating instances from container class..

    }

    public static final String API_URL = "http://n-scientific.org/wp-json/wp/v2/";
    public static final int DEFAULT_POSTS_PER_CALL = 10;
    public static final int DEFAULT_COMMENTS_PER_CALL = 20;
    public static final int NUMBER_OF_USERS_PER_CALL = 10;


    public static final String YOUTUBE_API_KEY = "AIzaSyCS-Rv7SraUngQpFeL1L-6Px2kuGB5Nxlo";
    public static final String SOUNDCLOUD_API_KEY = "cd9d2e5604410d714e32642a4ec0eed4";


    public static final String FETCHED_POST_COMMENT_DATE_PATTERN = "yyyy-MM-dd-HH:mm:ss";
    public static final String POST_COMMENT_DATE_PATTERN = "dd/MM/yyyy";
    public static final String POST_COMMENT_DATE_PATTERN_NO_YEAR = "dd/MM";

    public static final int MAX_FONT_SIZE = 23;
    public static final int MIN_FONT_SIZE = 11;

    public static final String TWITTER_URL = "https://twitter.com/N_Scientific";
    public static final String YOUTUBE_URL = "https://www.youtube.com/channel/UCGApGLFzrw_wJ45dQXzMMZw";
    public static final String SOUNDCLOUD_URL = "https://soundcloud.com/n-scientific";




}
