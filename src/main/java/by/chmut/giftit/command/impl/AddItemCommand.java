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

public class AddItemCommand implements Command {

    private static final Logger logger = LogManager.getLogger();
    private ItemService service = ServiceFactory.getInstance().getItemService();

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        if (request.getSession().getAttribute(EXCEPTION_PARAMETER_NAME) != null) {
            router.setRedirectPath(ERROR_PATH);
            return router;
        }
        router.setRedirectPath(CREATE_ITEM.name().toLowerCase());
        Map<String, Object> itemParameters = setParametersFromRequest(request);
        try {
            service.create(itemParameters);
        } catch (ServiceException exception) {
            logger.error("Error when create new item", exception);
            request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
            router.setRedirectPath(ERROR_PATH);
        }
        return router;
    }

    private Map<String, Object> setParametersFromRequest(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        result.put(ITEM_NAME_PARAMETER_NAME, req.getParameter(ITEM_NAME_PARAMETER_NAME));
        result.put(TYPE_PARAMETER_NAME, req.getParameterValues(TYPE_PARAMETER_NAME));
        result.put(ACTIVE_PARAMETER_NAME, req.getParameterValues(ACTIVE_PARAMETER_NAME));
        result.put(DESCRIPTION_PARAMETER_NAME, req.getParameter(DESCRIPTION_PARAMETER_NAME));
        result.put(PRICE_PARAMETER_NAME, req.getParameter(PRICE_PARAMETER_NAME));
        result.put(ITEM_IMAGE_PARAMETER_NAME, req.getSession().getAttribute(UPLOAD_FILE_ATTRIBUTE_NAME));
        return result;
    }
}
