package by.chmut.giftit.service;

import by.chmut.giftit.service.impl.CartServiceImpl;
import by.chmut.giftit.service.impl.ItemServiceImpl;
import by.chmut.giftit.service.impl.UserServiceImpl;

public class ServiceFactory {

    private static final ServiceFactory INSTANCE = new ServiceFactory();

    private final ItemService itemService = new ItemServiceImpl();
    private final UserService userService = new UserServiceImpl();
    private final CartService cartService = new CartServiceImpl();

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
}
