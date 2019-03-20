package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.entity.Comment;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.ERROR_PAGE;
import static by.chmut.giftit.constant.PathPage.MODERATE_COMMENT_PAGE;

/**
 * The Moderator command class provides the Moderator page display
 * with relevant information for moderation.
 *
 * @author Sergei Chmut.
 */
public class ModeratorCommand implements Command {

    /**
     * The logger for logging possible errors.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * The Item service to take advantage of business logic capabilities.
     */
    private ItemService itemService = ServiceFactory.getInstance().getItemService();
    /**
     * The User service to take advantage of business logic capabilities.
     */
    private UserService userService = ServiceFactory.getInstance().getUserService();
    /**
     * The Comment service to take advantage of business logic capabilities.
     */
    private CommentService commentService = ServiceFactory.getInstance().getCommentService();

    /**
     * The method refers to the business logic for information on moderation
     * and transmits it to the view. If successful, returns Router with
     * Moderate Comment page path, and in case of an error,
     * registers it and returns Router with Error page path.
     *
     * @param request the request object that is passed to the servlet
     * @return the router object that contains page path for forward
     */
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(MODERATE_COMMENT_PAGE);
        try {
            List<Comment> comments = commentService.findCommentToModerate();
            request.getSession().setAttribute(COMMENTS_PARAMETER_NAME, comments);
            if (!comments.isEmpty()) {
                Map<Long, Item> items = itemService.findByComment(comments);
                Map<Long, User> users = userService.findByComment(comments);
                request.getSession().setAttribute(USERS_PARAMETER_NAME, users);
                request.getSession().setAttribute(ITEMS_PARAMETER_NAME, items);
            }
        } catch (ServiceException exception) {
            logger.error("Error when try to find comments for moderator", exception);
            request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
            router.setPagePath(ERROR_PAGE);
        }
        return router;
    }
}
