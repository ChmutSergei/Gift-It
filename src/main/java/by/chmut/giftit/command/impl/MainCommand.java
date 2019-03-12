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

public class MainCommand implements Command {

    private static final Logger logger = LogManager.getLogger();
    private ItemService service = ServiceFactory.getInstance().getItemService();

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(MAIN_PAGE);
        List<Integer> itemIdList = (List<Integer>) request.getSession().getAttribute(RESULT_OF_SEARCH_ITEMS_PARAMETER_NAME);
        int limit = setLimit(request);
        int currentPage = setCurrentPage(request, limit);
        int offset = limit * currentPage;
        String pathForTempFiles = request.getServletContext().getRealPath("");
        List<Item> results = Collections.emptyList();
        if (itemIdList != null) {
            try {
                results = service.findByIdWithLimit(itemIdList, limit, offset, pathForTempFiles);
            } catch (ServiceException exception) {
                logger.error("Error when find Item on filter", exception);
                request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
                router.setPagePath(ERROR_PAGE);
            }
        } else {
            try {
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

    private int setLimit(HttpServletRequest request) {
        int limit = (int) request.getSession().getAttribute(PAGINATION_LIMIT_PARAMETER_NAME);
        String customChoiceLimit = request.getParameter(PAGINATION_LIMIT_PARAMETER_NAME);
        if (customChoiceLimit != null) {
            limit = Integer.parseInt(customChoiceLimit);
            request.getSession().setAttribute(PAGINATION_LIMIT_PARAMETER_NAME, limit);
        }
        return limit;
    }

    private int setCurrentPage(HttpServletRequest request, int limit) {
        int currentPage = (int) request.getSession().getAttribute(NUMBER_PAGE_PARAMETER_NAME);
        String customChoicePage = request.getParameter(NUMBER_PAGE_PARAMETER_NAME);
        if (customChoicePage != null) {
            currentPage = parse(customChoicePage, currentPage, limit, request);
            request.getSession().setAttribute(NUMBER_PAGE_PARAMETER_NAME, currentPage);
        }
        return currentPage;
    }

    private int parse(String newPage, int currentPage, int limit, HttpServletRequest request) {
        int numberPage;
        int countItems = (int) request.getSession().getAttribute(COUNT_ITEM_AFTER_SEARCH_PARAMETER_NAME);
        int countPages = countItems / limit - 1;
        switch (newPage) {
            case PREVIOUS_PAGE:
                numberPage = currentPage - 1 >= 0 ? currentPage - 1 : 0;
                break;
            case NEXT_PAGE:
                numberPage = currentPage + 1 <= countPages ? currentPage + 1 : countPages;
                break;
            default:
                numberPage = Integer.parseInt(request.getParameter(NUMBER_PAGE_PARAMETER_NAME));
        }
        return numberPage;
    }

}
