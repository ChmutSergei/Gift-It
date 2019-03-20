package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.ServiceFactory;
import by.chmut.giftit.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static by.chmut.giftit.command.CommandType.SEARCH_USER;
import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.ERROR_PATH;

/**
 * The User processing command class provides processing
 * of administrative commands over users.
 *
 * @author Sergei Chmut.
 */
public class UserProcessingCommand implements Command {

    /**
     * The logger for logging possible errors.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * The User service to take advantage of business logic capabilities.
     */
    private UserService service = ServiceFactory.getInstance().getUserService();

    /**
     * The method receives user data for the required task and parameters from the request
     * and sends it to the service level to perform administrative procedures.
     * If errors occur, Router with Error page path will be returned
     * and a corresponding message will be sent to the view.
     *
     * @param request the request object that is passed to the servlet
     * @return the router object that contains page path for redirect
     */
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setRedirectPath(SEARCH_USER.name().toLowerCase());
        String typeCommand = request.getParameter(TYPE_COMMAND_PARAMETER_NAME);
        String optionalId = request.getParameter(USER_ID_PARAMETER_NAME);
        String optionalDate = request.getParameter(BLOCKED_UNTIL_DATE_PARAMETER_NAME);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        LocalDate blockedUntil = !optionalDate.isEmpty() ? LocalDate.parse(optionalDate, formatter) : LocalDate.now();
        String newRole = request.getParameter(ROLE_PARAMETER_NAME);
        long userId = optionalId != null ? Long.parseLong(optionalId) : 0;
        try {
            boolean result = service.executeUserProcessingCommand(typeCommand, userId, blockedUntil, newRole);
            if (result) {
                request.getSession().setAttribute(SUCCESSFULLY_COMPLETED_PARAMETER_NAME, true);
            }
            request.getSession().setAttribute(SHOW_RESULT_PARAMETER_NAME, true);
        } catch (ServiceException exception) {
            logger.error("Error when processing user", exception);
            request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
            router.setRedirectPath(ERROR_PATH);
        }
        return router;
    }
}
