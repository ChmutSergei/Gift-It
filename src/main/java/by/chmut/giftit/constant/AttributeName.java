package by.chmut.giftit.constant;

import java.math.BigDecimal;

/**
 * The Attribute name class contains parameter name constants used in the application.
 *
 * @author Sergei Chmut.
 */
public class AttributeName {
    /**
     * Private constructor is forbidden to create objects of this class.
     */
    private AttributeName() {
    }
    // This group - names of parameters from jsp pages
    public static final String COMMAND_PARAMETER_NAME = "command";
    public static final String CUP_PARAMETER_NAME = "cup";
    public static final String SHIRT_PARAMETER_NAME = "shirt";
    public static final String PLATE_PARAMETER_NAME = "plate";
    public static final String PILLOW_PARAMETER_NAME = "pillow";
    public static final String PUZZLE_PARAMETER_NAME = "puzzle";
    public static final String MOUSE_PAD_PARAMETER_NAME = "mousepad";
    public static final String TOWEL_PARAMETER_NAME = "towel";
    public static final String LOW_PARAMETER_NAME = "low";
    public static final String MEDIUM_PARAMETER_NAME = "medium";
    public static final String HIGH_PARAMETER_NAME = "high";
    public static final String ALL_PARAMETER_NAME = "all";
    public static final String ACTION_TAG_TYPE_PARAMETER_NAME = "actionType";
    public static final String TYPE_PARAMETER_NAME = "type";
    public static final String ITEM_NAME_PARAMETER_NAME = "itemName";
    public static final String DESCRIPTION_PARAMETER_NAME = "description";
    public static final String ACTIVE_PARAMETER_NAME = "active";
    public static final String PRICE_PARAMETER_NAME = "price";
    public static final String ITEM_IMAGE_PARAMETER_NAME = "itemImage";
    public static final String MESSAGE_PARAMETER_NAME = "message";
    public static final String PREVIOUS_PAGE_PARAMETER_NAME = "prevPage";
    public static final String PAGE_PATH_PARAMETER_NAME = "pagePath";
    public static final String USER_PARAMETER_NAME = "user";
    public static final String USERNAME_PARAMETER_NAME = "username";
    public static final String PASSWORD_PARAMETER_NAME = "password";
    public static final String CONFIRM_PASSWORD_PARAMETER_NAME = "confirmPass";
    public static final String FIRST_NAME_PARAMETER_NAME = "firstName";
    public static final String LAST_NAME_PARAMETER_NAME = "lastName";
    public static final String EMAIL_PARAMETER_NAME = "email";
    public static final String PHONE_PARAMETER_NAME = "phone";
    public static final String ADDRESS_PARAMETER_NAME = "address";
    public static final String TITLE_ATTRIBUTE_NAME = "title";
    public static final String RESULT_ATTRIBUTE_NAME = "result";
    public static final String UPLOAD_FILE_ATTRIBUTE_NAME = "filePath";
    public static final String UPLOAD_FILENAME_ATTRIBUTE_NAME = "fileName";
    public static final String EXCEPTION_PARAMETER_NAME = "exception";
    public static final String RESULT_OF_SEARCH_ITEMS_PARAMETER_NAME = "resultSearch";
    public static final String PAGINATION_LIMIT_PARAMETER_NAME = "limit";
    public static final String PRICE_CRITERIA_PARAMETER_NAME = "criteriaPrice";
    public static final String ENABLE_CHECKBOX = "ON";
    public static final String DISABLE_CHECKBOX = "OFF";
    public static final String COUNT_COMMENTS_PARAMETER_NAME = "countComments";
    public static final String LOCALE_PARAMETER_NAME = "locale";
    public static final String DEFAULT_PARAMETER_LOCALE = "ru";
    public static final String ITEM_PARAMETER_NAME = "item";
    public static final String COMMENTS_PARAMETER_NAME = "comments";
    public static final String ITEM_ID_PARAMETER_NAME = "itemId";
    public static final String USERS_PARAMETER_NAME = "users";
    public static final String COUNT_ITEM_PARAMETER_NAME = "count";
    public static final String COUNT_IN_CART_PARAMETER_NAME = "countInCart";
    public static final String TOTAL_PARAMETER_NAME = "total";
    public static final String ITEM_TO_ADD_PARAMETER_NAME = "itemToAdd";
    public static final String ITEMS_FOR_CART_PARAMETER_NAME = "items";
    public static final String PAID_ITEMS_PARAMETER_NAME = "paidItems";
    public static final String CART_COMMAND_FLAG_PARAMETER_NAME = "cartCommand";
    public static final String CART_PARAMETER_NAME = "carts";
    public static final String CART_ID_PARAMETER_NAME = "cartId";
    public static final String COMMENT_ID_PARAMETER_NAME = "commentId";
    public static final String COMMENT_PARAMETER_NAME = "comment";
    public static final String QUESTIONS_PARAMETER_NAME = "questions";
    public static final String QUESTION_PARAMETER_NAME = "question";
    public static final String ORDERS_PARAMETER_NAME = "orders";
    public static final String ITEMS_FOR_ORDERS_PARAMETER_NAME = "itemsOrder";
    public static final String USERS_FOR_ORDERS_PARAMETER_NAME = "usersOrder";
    public static final String USER_SEARCH_TYPE_PARAMETER_NAME = "type";
    public static final String USER_ID_PARAMETER_NAME = "userId";
    public static final String INIT_DATE_PARAMETER_NAME = "initDate";
    public static final String RESULT_OF_SEARCH_USERS_PARAMETER_NAME = "resultUsers";
    public static final String BLOCKED_UNTIL_DATE_PARAMETER_NAME = "blockedUntil";
    public static final String TYPE_COMMAND_PARAMETER_NAME = "typeCommand";
    public static final String BLOCKED_COMMAND_PARAMETER_NAME = "block";
    public static final String UNLOCK_COMMAND_PARAMETER_NAME = "unlock";
    public static final String SET_ROLE_COMMAND_PARAMETER_NAME = "setRole";
    public static final String ROLE_PARAMETER_NAME = "role";
    public static final String DELETE_USER_COMMAND_PARAMETER_NAME = "delete";
    public static final String SHOW_RESULT_PARAMETER_NAME = "show";
    public static final String SUCCESSFULLY_COMPLETED_PARAMETER_NAME = "success";
    public static final String UNANSWERED_QUESTIONS_PARAMETER_NAME = "unanswered";
    public static final String QUESTION_ID_PARAMETER_NAME = "questionId";
    public static final String ANSWER_PARAMETER_NAME = "respon";
    public static final String ITEMS_PARAMETER_NAME = "items";
    public static final String MODERATE_COMMAND_PARAMETER_NAME = "action";
    public static final String ORDER_DETAILS_PARAMETER_NAME = "details";
    public static final String CARD_NAME_PARAMETER_NAME = "nameCard";
    public static final String CARD_CVC_PARAMETER_NAME = "cvc2";
    public static final String CARD_NUMBER_PARAMETER_NAME = "numCard";
    public static final String CARD_MONTH_PARAMETER_NAME = "month";
    public static final String CARD_YEAR_PARAMETER_NAME = "year";
    public static final String NEW_ORDER_PARAMETER_NAME = "newOrder";
    public static final String PAYMENT_SUCCESS_PARAMETER_NAME = "payment";
    public static final String COUNT_ITEM_AFTER_SEARCH_PARAMETER_NAME = "countSearch";
    public static final String NUMBER_PAGE_PARAMETER_NAME = "page";
    public static final String DATE_NOW_PARAMETER_NAME = "dateNow";
    // This group - constant keys for messages bundle
    public static final String MESSAGE_LOGIN_FAILED_KEY = "error.loginFailed";
    public static final String MESSAGE_NOT_FOUND_ID_KEY = "error.notFoundId";
    public static final String MESSAGE_ADMIN_ORDERS_ERROR_KEY = "admin.error.orders";
    public static final String MESSAGE_NOT_FOUND_USERS_KEY = "user.not.found";
    public static final String MESSAGE_PAYMENT_FAILED_KEY = "payment.failed";
    // This group - the commands that we get from the request
    public static final String ADD_CART_COMMAND = "ADD_ITEM";
    public static final String DELETE_CART_COMMAND = "DELETE_ITEM";
    public static final String MODERATE_DELETE_COMMAND = "delete";
    public static final String MODERATE_DELETE_BLOCK_LOW_COMMAND = "deleteb7";
    public static final String MODERATE_DELETE_BLOCK_HIGH_COMMAND = "deleteb14";
    // constants used in the application
    public static final BigDecimal LOW_BORDER_PRICE = new BigDecimal(30);
    public static final BigDecimal MEDIUM_BORDER_PRICE = new BigDecimal(60);
    public static final BigDecimal HIGH_BORDER_PRICE = new BigDecimal(100);
    public static final int DEFAULT_NUMBER_PAGE = 0;
    public static final int DEFAULT_PAGINATION_LIMIT = 16;
    public static final int MAX_COUNT_FOR_ITEM_TO_CART = 300;
    public static final int MIN_LENGTH_COMMENT = 3;
    public static final int MAX_LENGTH_COMMENT = 250;
    public static final int LOW_LEVEL_BLOCKING_DAYS = 7;
    public static final int HIGH_LEVEL_BLOCKING_DAYS = 14;
    public static final String DATE_TIME_FORMAT = "dd.MM.yyyy";
    public static final String PREVIOUS_PAGE = "previous";
    public static final String NEXT_PAGE = "next";
    public static final String DEFAULT_ITEM_PATH = "/item/";
    public static final String DEFAULT_UPLOAD_PATH = "/upload/";

}