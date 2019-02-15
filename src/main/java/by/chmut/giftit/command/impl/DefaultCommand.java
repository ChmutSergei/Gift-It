package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.criteria.Criteria;
import by.chmut.giftit.criteria.SearchCriteria;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.service.ItemService;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.impl.ItemServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.ERROR_PAGE;
import static by.chmut.giftit.constant.PathPage.HOME_PAGE;

public class DefaultCommand implements Command {

    private static final Logger logger = LogManager.getLogger();
    private ItemService service = new ItemServiceImpl();

    @Override
    public Router execute(HttpServletRequest req) {
        Router router = new Router();
        router.setPagePath(HOME_PAGE);
        List<Criteria> criteria = createCriteria(req);
        try {
            List<Item> results = service.find(criteria);
            req.getSession().setAttribute(RESULT_ATTRIBUTE_NAME, results);
        } catch (ServiceException e) {
            logger.error("Error when find Item on criteria", e);
            router.setPagePath(ERROR_PAGE);
        }
        return router;
    }

    private List<Criteria> createCriteria(HttpServletRequest req) {
        Criteria<SearchCriteria.Type> typeCriteria = new Criteria<>(SearchCriteria.Type.class);
        Criteria<SearchCriteria.Price> priceCriteria = new Criteria<>(SearchCriteria.Price.class);
        List<Criteria> criteria = new ArrayList<>();
        String cup = req.getParameter(CUP_PARAMETER_NAME);
        String shirt = req.getParameter(SHIRT_PARAMETER_NAME);
        String plate = req.getParameter(PLATE_PARAMETER_NAME);
        String puzzle = req.getParameter(PUZZLE_PARAMETER_NAME);
        String mousePad = req.getParameter(MOUSE_PAD_PARAMETER_NAME);
        String towel = req.getParameter(TOWEL_PARAMETER_NAME);
        String low = req.getParameter(LOW_PARAMETER_NAME);
        String medium = req.getParameter(MEDIUM_PARAMETER_NAME);
        String high = req.getParameter(HIGH_PARAMETER_NAME);
        String premium = req.getParameter(PREMIUM_PARAMETER_NAME);
        typeCriteria.add(SearchCriteria.Type.CUP, cup);
        typeCriteria.add(SearchCriteria.Type.SHIRT, shirt);
        typeCriteria.add(SearchCriteria.Type.PLATE, plate);
        typeCriteria.add(SearchCriteria.Type.PUZZLE, puzzle);
        typeCriteria.add(SearchCriteria.Type.MOUSE_PAD, mousePad);
        typeCriteria.add(SearchCriteria.Type.TOWEL, towel);
        priceCriteria.add(SearchCriteria.Price.LOW, low);
        priceCriteria.add(SearchCriteria.Price.MEDIUM, medium);
        priceCriteria.add(SearchCriteria.Price.HIGH, high);
        priceCriteria.add(SearchCriteria.Price.PREMIUM, premium);
        criteria.add(typeCriteria);
        criteria.add(priceCriteria);
        return criteria;
    }
}
