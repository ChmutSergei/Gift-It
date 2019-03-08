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

public class ModeratorCommand implements Command {

    private static final Logger logger = LogManager.getLogger();

    private ItemService itemService = ServiceFactory.getInstance().getItemService();
    private UserService userService = ServiceFactory.getInstance().getUserService();
    private CommentService commentService = ServiceFactory.getInstance().getCommentService();

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
