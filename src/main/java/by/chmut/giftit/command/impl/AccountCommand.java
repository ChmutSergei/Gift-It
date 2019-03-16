package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.entity.*;
import by.chmut.giftit.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.ACCOUNT_PAGE;
import static by.chmut.giftit.constant.PathPage.ERROR_PATH;

public class AccountCommand implements Command {

    private static final Logger logger = LogManager.getLogger();

    private ItemService itemService = ServiceFactory.getInstance().getItemService();
    private CommentService commentService = ServiceFactory.getInstance().getCommentService();
    private QuestionService questionService = ServiceFactory.getInstance().getQuestionService();

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(ACCOUNT_PAGE);
        User user = (User) request.getSession().getAttribute(USER_PARAMETER_NAME);
        try {
            List<Item> paidItems = itemService.findPaidItems(user.getUserId(), request.getServletContext().getRealPath(DEFAULT_ITEM_PATH));
            request.getSession().setAttribute(PAID_ITEMS_PARAMETER_NAME, paidItems);
            List<Comment> comments = commentService.findByUserId(user.getUserId());
            request.getSession().setAttribute(COMMENTS_PARAMETER_NAME, comments);
            Map<Long, Item> items = itemService.findByComment(comments);
            request.getSession().setAttribute(ITEMS_FOR_CART_PARAMETER_NAME, items);
            List<Question> questions = questionService.findByUserId(user.getUserId());
            request.getSession().setAttribute(QUESTIONS_PARAMETER_NAME, questions);
        } catch (ServiceException exception) {
            logger.error("Error when try to find paid Items, comments, questions", exception);
            request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
            router.setRedirectPath(ERROR_PATH);
        }
        return router;
    }
}
