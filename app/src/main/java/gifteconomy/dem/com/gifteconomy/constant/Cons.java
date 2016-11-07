package gifteconomy.dem.com.gifteconomy.constant;

/**
 * Created by Workstation01 on 7/16/2016.
 */
public class Cons {

    public static final int NO_USER = 1;
    public static final int NO_SKILL = 2;
    public static final int NO_THINGS = 3;
    public static final int NO_BOOK = 4;
    public static String male="MALE";
    public static String female="FEMALE";

    public static String[] FB_READ_PERMISSIONS = new String[]{"public_profile, email, user_birthday, user_friends"};

    public static final String BASEURL="http://demarlos.in/gift_economy/api/";
    //public static final String BASEURL="192.168.21.101/gift_economy/api/";
    public static final String LOGIN="login";
    public static final String SIGNUP="signup";
    public static final String INTEREST="interest";
    public static final String USER="user";
    public static final String NEARBY="nearby";
    public static final String SKILS="skills";
    public static final String POST_ITEMS="items";


    public static enum SocialMediaProvider {
        FACEBOOK,
        GOOGLE_PLUS
    }

}
