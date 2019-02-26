package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.dao.CommentDao;
import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.dao.DaoFactory;
import by.chmut.giftit.dao.ItemDao;
import by.chmut.giftit.entity.Comment;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.service.ItemService;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.impl.ItemServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static by.chmut.giftit.constant.AttributeName.COMMENTS_PARAMETER_NAME;
import static by.chmut.giftit.constant.AttributeName.ITEM_PARAMETER_NAME;
import static by.chmut.giftit.constant.PathPage.PREVIEW_ITEM_PAGE;

public class PreviewItemCommand implements Command {

    private static final Logger logger = LogManager.getLogger();
    private ItemService service = new ItemServiceImpl();
    private ItemDao itemDao = DaoFactory.getInstance().getItemDao();
    private CommentDao dao = DaoFactory.getInstance().getCommentDao();

    @Override
    public Router execute(HttpServletRequest req) {
        Router router = new Router();
        router.setPagePath(PREVIEW_ITEM_PAGE);
        long itemId = Long.parseLong(req.getParameter(ITEM_PARAMETER_NAME));
        try {
            Item item = itemDao.findEntity(itemId, req.getServletContext().getRealPath(""));
            req.getSession().setAttribute(ITEM_PARAMETER_NAME, item);
            List<Comment> comments = new ArrayList<>();
            comments.add(dao.findEntity(itemId));
            req.getSession().setAttribute(COMMENTS_PARAMETER_NAME, comments);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return router;
    }
}
