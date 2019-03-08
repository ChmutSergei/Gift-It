package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.entity.Comment;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.ItemService;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.ServiceFactory;
import by.chmut.giftit.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.ERROR_PAGE;
import static by.chmut.giftit.constant.PathPage.PREVIEW_ITEM_PAGE;

public class PreviewItemCommand implements Command {

    private static final Logger logger = LogManager.getLogger();
    private ItemService itemService = ServiceFactory.getInstance().getItemService();
    private UserService userService = ServiceFactory.getInstance().getUserService();

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(PREVIEW_ITEM_PAGE);
        String itemId = (String) request.getSession().getAttribute(ITEM_ID_PARAMETER_NAME);
        if (itemId == null) {
            request.getSession().setAttribute(MESSAGE_PARAMETER_NAME, MESSAGE_NOT_FOUND_ID_KEY);
            return router;
        }
        long id = Long.parseLong(itemId);
        try {
            Optional<Item> item = itemService.find(id, request.getServletContext().getRealPath(""));
            if (item.isPresent()){
                request.getSession().setAttribute(ITEM_PARAMETER_NAME, item.get());
            } else {
                request.getSession().setAttribute(MESSAGE_PARAMETER_NAME, MESSAGE_NOT_FOUND_ID_KEY); // TODO заменить сообщение
            }
            List<Comment> comments = itemService.findCommentOnItem(id, Comment.CommentStatus.ACTIVE);
            request.getSession().setAttribute(COMMENTS_PARAMETER_NAME, comments);
            if (!comments.isEmpty()) {
                findUserOnComment(comments, request);
            }
        } catch (ServiceException exception) {
            logger.error("Error when try to preview Item", exception);
            request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
            router.setPagePath(ERROR_PAGE);
        }
        return router;
    }

    private void findUserOnComment(List<Comment> comments, HttpServletRequest request) throws ServiceException {
        Map<Long, User> usersForComments = new HashMap<>();
        for (Comment comment : comments) {
            Optional<User> optionalUser = userService.find(comment.getUserId());
            optionalUser.ifPresent(user -> usersForComments.put(user.getUserId(), user));
        }
        request.getSession().setAttribute(USERS_PARAMETER_NAME, usersForComments);
    }
}
