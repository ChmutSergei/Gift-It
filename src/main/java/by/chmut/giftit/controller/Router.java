package by.chmut.giftit.controller;

import static by.chmut.giftit.constant.PathPage.*;
import static by.chmut.giftit.controller.Router.DispatcherType.FORWARD;
import static by.chmut.giftit.controller.Router.DispatcherType.REDIRECT;

/**
 * The Router class is the result of executing the command passed in the request.
 * It contains the FORWARD or REDIRECT dispatcher type, as the page path used dispatcher.
 *
 * @author Sergei Chmut.
 */
public class Router {

    /**
     * The enum Dispatcher type.
     *
     * @author Sergei Chmut.
     */
    enum DispatcherType {
        /**
         * Forward dispatcher type.
         */
        FORWARD,
        /**
         * Redirect dispatcher type.
         */
        REDIRECT
    }

    /**
     * This template page is always used by the application.
     * It is a layout and important which page will be added inside the layout.
     */
    private String templatePage = TEMPLATE_PAGE;
    /**
     * The path for the page to be added inside the layout.
     */
    private String pagePath;
    /**
     * Default dispatcher type is FORWARD.
     */
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
