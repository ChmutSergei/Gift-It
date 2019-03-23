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
import static by.chmut.giftit.constant.PathPage.ERROR_PAGE;
import static by.chmut.giftit.constant.PathPage.MAIN_PAGE;

/**
 * The Main command class provides the display of the Main page
 * with the results of the search for items
 *
 * @author Sergei Chmut.
 */
public class MainCommand implements Command {

    /**
     * The logger for logging possible errors.
     */
    private static final Logger logger = LogManager.getLogger();
    /**
     * The Item service to take advantage of business logic capabilities.
     */
    private ItemService service = ServiceFactory.getInstance().getItemService();

    /**
     * The method gets the search results (filter) of items and display options for items.
     * Sends that parameters to the service level and gets results.
     * In the absence of errors, the results are saved for view,
     * and we also receive data on the number of comments for each item.
     * In case of errors, the method fixes them and returns Router with Error page path.
     *
     * @param request the request object that is passed to the servlet
     * @return the router object that contains page path for forward
     */
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(MAIN_PAGE);
        List<Integer> itemIdList = (List<Integer>) request.getSession().getAttribute(RESULT_OF_SEARCH_ITEMS_PARAMETER_NAME);
        int limit = getLimitItemsOnPage(request);
        int currentPage = getCurrentPage(request, limit);
        int offset = limit * currentPage;
        String pathForTempFiles = request.getServletContext().getRealPath(DEFAULT_ITEM_PATH);
        List<Item> results = Collections.emptyList();
        if (itemIdList != null) {
            try {
                results = service.findResultsSearchWithLimit(itemIdList, limit, offset, pathForTempFiles);
            } catch (ServiceException exception) {
                logger.error("Error when find Item on filter", exception);
                request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
                router.setPagePath(ERROR_PAGE);
            }
        } else {
            try {
                int countItem = service.countAllItem();
                request.getSession().setAttribute(COUNT_ITEM_AFTER_SEARCH_PARAMETER_NAME, countItem);
                results = service.findAllWithLimit(pathForTempFiles, limit, offset);
            } catch (ServiceException exception) {
                logger.error("Error when find all Item", exception);
                request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
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

    /**
     * Method gets limit from session and check user's choice for limit.
     * If User change custom limit, set that choice in the session.
     *
     * @param request the request object that is passed to the servlet
     * @return the limit items on page parameter
     */
    private int getLimitItemsOnPage(HttpServletRequest request) {
        int limit = (int) request.getSession().getAttribute(PAGINATION_LIMIT_PARAMETER_NAME);
        String customChoiceLimit = request.getParameter(PAGINATION_LIMIT_PARAMETER_NAME);
        if (customChoiceLimit != null) {
            limit = Integer.parseInt(customChoiceLimit);
            request.getSession().setAttribute(PAGINATION_LIMIT_PARAMETER_NAME, limit);
        }
        return limit;
    }

    /**
     * Method gets current page from the session and check user's choice for the page.
     * If user change current page, calculate that page and set in the session.
     *
     * @param request the request object that is passed to the servlet
     * @param limit   the custom limit items on page
     * @return the current page
     */
    private int getCurrentPage(HttpServletRequest request, int limit) {
        int currentPage = (int) request.getSession().getAttribute(NUMBER_PAGE_PARAMETER_NAME);
        String customChoicePage = request.getParameter(NUMBER_PAGE_PARAMETER_NAME);
        if (customChoicePage != null) {
            currentPage = calculateCurrentPage(customChoicePage, currentPage, limit, request);
            request.getSession().setAttribute(NUMBER_PAGE_PARAMETER_NAME, currentPage);
        }
        return currentPage;
    }

    /**
     * Method calculate current page number.
     *
     * @param newPage     the custom user's new page
     * @param oldPage     the old page
     * @param limit       the limit items on page
     * @param request the request object that is passed to the servlet
     * @return the number of page
     */
    private int calculateCurrentPage(String newPage, int oldPage, int limit, HttpServletRequest request) {
        int result;
        int countItemsResultSearch = (int) request.getSession().getAttribute(COUNT_ITEM_AFTER_SEARCH_PARAMETER_NAME);
        int countPages = countItemsResultSearch / limit;
        switch (newPage) {
            case PREVIOUS_PAGE:
                result = oldPage - 1 >= 0 ? oldPage - 1 : 0;
                break;
            case NEXT_PAGE:
                result = oldPage + 1 <= countPages ? oldPage + 1 : countPages;
                break;
            default:
                result = Integer.parseInt(request.getParameter(NUMBER_PAGE_PARAMETER_NAME));
        }
        return result;
    }

}
