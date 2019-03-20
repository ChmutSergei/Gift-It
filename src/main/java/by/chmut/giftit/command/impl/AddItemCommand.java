package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.service.ItemService;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static by.chmut.giftit.command.CommandType.CREATE_ITEM;
import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.ERROR_PATH;

/**
 * The Add item command class attempts to create a new Item
 * and add it to the product catalog.
 *
 * @author Sergei Chmut.
 */
public class AddItemCommand implements Command {

    /**
     * The logger for logging possible errors.
     */
    private static final Logger logger = LogManager.getLogger();
    /**
     * The Item service to take advantage of business logic capabilities.
     */
    private ItemService service = ServiceFactory.getInstance().getItemService();

    /**
     * The method removes the passed parameters from the request
     * and sends them to the service level to attempt to create a new product.
     * If an error occurs during creation,
     * return Router with Error page path for display it.
     * Also if an error occurs during uploading image file for new item,
     * it will cause redirect to the Error page.
     *
     * @param request the request object that is passed to the servlet
     * @return the router object that contains page path for redirect
     */
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        if (request.getSession().getAttribute(EXCEPTION_PARAMETER_NAME) != null) {
            router.setRedirectPath(ERROR_PATH);
            return router;
        }
        router.setRedirectPath(CREATE_ITEM.name().toLowerCase());
        Map<String, Object> itemParameters = getParametersFromRequest(request);
        try {
            service.create(itemParameters);
        } catch (ServiceException exception) {
            logger.error("Error when create new item", exception);
            request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
            router.setRedirectPath(ERROR_PATH);
        }
        return router;
    }

    /**
     * Method provides to get parameters from request.
     *
     * @param request the request object that is passed to the servlet
     * @return the map of the search parameters
     */
    private Map<String, Object> getParametersFromRequest(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        result.put(ITEM_NAME_PARAMETER_NAME, request.getParameter(ITEM_NAME_PARAMETER_NAME));
        result.put(TYPE_PARAMETER_NAME, request.getParameterValues(TYPE_PARAMETER_NAME));
        result.put(ACTIVE_PARAMETER_NAME, request.getParameterValues(ACTIVE_PARAMETER_NAME));
        result.put(DESCRIPTION_PARAMETER_NAME, request.getParameter(DESCRIPTION_PARAMETER_NAME));
        result.put(PRICE_PARAMETER_NAME, request.getParameter(PRICE_PARAMETER_NAME));
        result.put(ITEM_IMAGE_PARAMETER_NAME, request.getSession().getAttribute(UPLOAD_FILE_ATTRIBUTE_NAME));
        return result;
    }
}
