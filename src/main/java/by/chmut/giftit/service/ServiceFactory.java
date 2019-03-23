package by.chmut.giftit.service;

import by.chmut.giftit.service.impl.*;

/**
 * The Service factory class represents
 * access to common business logic interfaces.
 *
 * @author Sergei Chmut.
 */
public class ServiceFactory {

    private static final ServiceFactory INSTANCE = new ServiceFactory();

    private final ItemService itemService = new ItemServiceImpl();
    private final UserService userService = new UserServiceImpl();
    private final CartService cartService = new CartServiceImpl();
    private final CommentService commentService = new CommentServiceImpl();
    private final QuestionService questionService = new QuestionServiceImpl();
    private final OrderService orderService = new OrderServiceImpl();
    private final AjaxService ajaxService = new AjaxServiceImpl();

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return INSTANCE;
    }

    public ItemService getItemService() {
        return itemService;
    }

    public UserService getUserService() {
        return userService;
    }

    public CartService getCartService() {
        return cartService;
    }

    public CommentService getCommentService() {
        return commentService;
    }

    public QuestionService getQuestionService() {
        return questionService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public AjaxService getAjaxService() {
        return ajaxService;
    }
}
