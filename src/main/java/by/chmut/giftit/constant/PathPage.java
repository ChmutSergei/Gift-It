package by.chmut.giftit.constant;

public class PathPage {

    private PathPage() {}

    public static final String TEMPLATE_PAGE = "/WEB-INF/view/layout/template.jspx";
    public static final String HOME_PAGE = "pages/main.jspx";
    public static final String ERROR_PAGE = "/errors/error.jspx";
    public static final String SIGNIN_PAGE = "pages/sign_in.jspx";
    public static final String SIGNUP_PAGE = "pages/sign_up.jspx";
    public static final String ADD_ITEM_PAGE = "pages/add_item.jspx";
    public static final String PREFIX = "/controller?command=";

    public static final String ERROR_PATH = "error";
    public static final String SIGNIN_PATH = "signin";
    public static final String SIGNUP_PATH = "signup";
    public static final String HOME_PATH = "home";
}
