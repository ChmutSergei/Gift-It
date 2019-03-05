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

public class UserProcessingCommand implements Command {

    private static final Logger logger = LogManager.getLogger();

    private UserService service = ServiceFactory.getInstance().getUserService();

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setRedirectPath(SEARCH_USER.name().toLowerCase());
        String typeCommand = request.getParameter(TYPE_COMMAND_PARAMETER_NAME);
        String optionalId = request.getParameter(USER_ID_PARAMETER_NAME);
        String optionalDate = request.getParameter(BLOCKED_UNTIL_DATE_PARAMETER_NAME);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
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
            exception.printStackTrace();
        }
        return router;
    }
}
