package by.chmut.giftit.controller;

import static by.chmut.giftit.constant.PathPage.ERROR_PAGE;
import static by.chmut.giftit.constant.PathPage.ERROR_PATH;
import static by.chmut.giftit.controller.Router.DispatcherType.FORWARD;
import static by.chmut.giftit.controller.Router.DispatcherType.REDIRECT;

public class Router {

    enum DispatcherType {
        FORWARD, REDIRECT
    }

    private static final String PREFIX = "/controller?command=";
    private String page = "/WEB-INF/view/layout/template.jspx";
    private String pagePath;
    private String redirectPath;
    private DispatcherType dispatcher = FORWARD;

    public String getPage() {
        return page;
    }

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        if (ERROR_PAGE.equals(pagePath)) {
            this.page = ERROR_PAGE;
        }
        this.pagePath = pagePath;
    }

    public String getRedirectPath() {
        return redirectPath;
    }

    public void setRedirectPath(String page) {
        this.dispatcher = REDIRECT;
        this.redirectPath =  PREFIX + page;
    }

    public DispatcherType getDispatcher() {
        return dispatcher;
    }

}
