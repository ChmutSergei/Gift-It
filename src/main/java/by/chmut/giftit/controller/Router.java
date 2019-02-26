package by.chmut.giftit.controller;

import static by.chmut.giftit.constant.PathPage.*;
import static by.chmut.giftit.controller.Router.DispatcherType.FORWARD;
import static by.chmut.giftit.controller.Router.DispatcherType.REDIRECT;

public class Router {

    enum DispatcherType {
        FORWARD, REDIRECT
    }

    private String templatePage = TEMPLATE_PAGE;
    private String pagePath;
    private DispatcherType dispatcher = FORWARD;

    public String getTemplatePage() {
        return templatePage;
    }

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        if (ERROR_PAGE.equals(pagePath)) {
            this.templatePage = ERROR_PAGE;
        }
        this.pagePath = pagePath;
    }

    public void setRedirectPath(String command) {
        this.dispatcher = REDIRECT;
        this.pagePath =  PREFIX + command;
    }

    public DispatcherType getDispatcher() {
        return dispatcher;
    }

}
