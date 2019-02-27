package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.entity.Comment;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.service.ItemService;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.PREVIEW_ITEM_PAGE;

public class PreviewItemCommand implements Command {

    private static final Logger logger = LogManager.getLogger();
    private ItemService service = ServiceFactory.getInstance().getItemService();

    @Override
    public Router execute(HttpServletRequest req) {
        Router router = new Router();
        router.setPagePath(PREVIEW_ITEM_PAGE);
        String itemId = (String) req.getSession().getAttribute(ITEM_ID_PARAMETER_NAME);
        if (itemId == null) {
            req.getSession().setAttribute(MESSAGE_PARAMETER_NAME, MESSAGE_NOT_FOUND_ID_KEY);
            return router;
        }
        long id = Long.parseLong(itemId);
        try {
            Item item = service.find(id, req.getServletContext().getRealPath(""));
            req.getSession().setAttribute(ITEM_PARAMETER_NAME, item);
            List<Comment> comments = new ArrayList<>();
            comments.add(service.findCommentOnItem(id));
            req.getSession().setAttribute(COMMENTS_PARAMETER_NAME, comments);
        } catch (ServiceException exception) {
            logger.error("Error when try to preview Item");
        }
        return router;
    }
}
