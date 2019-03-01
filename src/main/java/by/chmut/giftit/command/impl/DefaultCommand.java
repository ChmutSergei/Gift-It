package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.service.ItemService;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.AttributeName.COUNT_COMMENTS_PARAMETER_NAME;
import static by.chmut.giftit.constant.PathPage.ERROR_PAGE;
import static by.chmut.giftit.constant.PathPage.HOME_PAGE;

public class DefaultCommand implements Command {

    private static final Logger logger = LogManager.getLogger();
    private ItemService service = ServiceFactory.getInstance().getItemService();

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(HOME_PAGE);
        List<Integer> itemIdList = (List<Integer>) request.getSession().getAttribute(RESULT_OF_SEARCH_ITEMS_PARAMETER_NAME);
        int limit = (int) request.getSession().getAttribute(PAGINATION_LIMIT_PARAMETER_NAME);
        int offset = (int) request.getSession().getAttribute(PAGINATION_OFFSET_PARAMETER_NAME);
        String pathForTempFiles = request.getServletContext().getRealPath("");
        List<Item> results = Collections.emptyList();
        if (itemIdList != null) {
            try {
                results = service.findItemsOnId(itemIdList, limit, offset, pathForTempFiles);
            } catch (ServiceException exception) {
                logger.error("Error when find Item on filter", exception);
                router.setPagePath(ERROR_PAGE);
            }
        } else {
            try {
                results = service.findAll(pathForTempFiles, 3, offset);
            } catch (ServiceException exception) {
                logger.error("Error when find all Item", exception);
                router.setPagePath(ERROR_PAGE);
            }
        }
        if (!results.isEmpty()) {
            try {
                Map<Long, Integer> countCommenstMap = service.findCountCommentsForItem(results);
                request.getSession().setAttribute(COUNT_COMMENTS_PARAMETER_NAME, countCommenstMap);
            } catch (ServiceException exception) {
                logger.error("Error when count comments for Item", exception);
            }
        }
        request.getSession().setAttribute(RESULT_ATTRIBUTE_NAME, results);
        return router;
    }

}
