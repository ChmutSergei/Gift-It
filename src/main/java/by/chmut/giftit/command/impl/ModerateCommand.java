package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.CommentService;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static by.chmut.giftit.command.CommandType.MODERATOR;
import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.ERROR_PAGE;

/**
 * The Moderate command class provides moderation of comments.
 *
 * @author Sergei Chmut.
 */
public class ModerateCommand implements Command {

    /**
     * The logger for logging possible errors.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * The Comment service to take advantage of business logic capabilities.
     */
    private CommentService commentService = ServiceFactory.getInstance().getCommentService();

    /**
     * The method receives the parameters for moderation from the request
     * and sends them to the service level to perform moderation.
     * In case of an error or not performing the operation,
     * the method will record an error with the message
     * and return the Router with Error page path.
     *
     * @param request the request object that is passed to the servlet
     * @return the router object that contains page path for forward or redirect
     */
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setRedirectPath(MODERATOR.name().toLowerCase());
        String moderatorCommand = request.getParameter(MODERATE_COMMAND_PARAMETER_NAME);
        String commentId = request.getParameter(COMMENT_ID_PARAMETER_NAME);
        Map<Long, User> users = (Map<Long, User>) request.getSession().getAttribute(USERS_PARAMETER_NAME);
        try {
            boolean done = commentService.moderate(moderatorCommand, commentId, users);
            if (!done) {
                logger.error("Error when try to moderate comments");
                request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, "Error when try to moderate comments");
                router.setPagePath(ERROR_PAGE);
            }
        } catch (ServiceException exception) {
            logger.error("Error when try to moderate comments", exception);
            request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
            router.setPagePath(ERROR_PAGE);
        }
        return router;
    }
}
