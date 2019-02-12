package by.chmut.giftit.controller;

import static by.chmut.giftit.controller.Router.DispatcherType.FORWARD;
import static by.chmut.giftit.controller.Router.DispatcherType.REDIRECT;

public class Router {

    enum DispatcherType {
        FORWARD, REDIRECT
    }

    private String pagePath;

    private DispatcherType dispatcher = FORWARD;


    public DispatcherType getDispatcher() {
        return dispatcher;
    }

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public void setRedirect() {
        this.dispatcher = REDIRECT;
    }
}
