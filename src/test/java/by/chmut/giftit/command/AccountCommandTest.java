package by.chmut.giftit.command;

import by.chmut.giftit.command.impl.AccountCommand;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.CommentService;
import by.chmut.giftit.service.ItemService;
import by.chmut.giftit.service.QuestionService;
import by.chmut.giftit.service.ServiceException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;

import static by.chmut.giftit.constant.AttributeName.DEFAULT_ITEM_PATH;
import static by.chmut.giftit.constant.AttributeName.USER_PARAMETER_NAME;
import static by.chmut.giftit.constant.PathPage.ACCOUNT_PAGE;
import static by.chmut.giftit.constant.PathPage.ERROR_PATH;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


public class AccountCommandTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private User user;
    @Mock
    private HttpSession session;
    @Mock
    private ServletContext context;
    @Mock
    private ItemService itemService;
    @Mock
    private CommentService commentService;
    @Mock
    private QuestionService questionService;
    @InjectMocks
    private AccountCommand accountCommand;

    private static final long ID_FOR_TEST = 1;
    private static final String PATH_FOR_TEST = "";

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        doReturn(session).when(request).getSession();
        when(session.getAttribute(USER_PARAMETER_NAME)).thenReturn(user);
        when(user.getUserId()).thenReturn(ID_FOR_TEST);
        doReturn(context).when(request).getServletContext();
    }

    @Test(description = "Positive test when services not throw any exception")
    public void testExecutePositive() throws ServiceException {
        when(itemService.findPaidItems(ID_FOR_TEST, PATH_FOR_TEST)).thenReturn(new ArrayList<>());
        when(commentService.findByUserId(ID_FOR_TEST)).thenReturn(new ArrayList<>());
        when(itemService.findByComment(new ArrayList<>())).thenReturn(new HashMap<>());
        when(questionService.findByUserId(ID_FOR_TEST)).thenReturn(new ArrayList<>());
        Router expected = new Router();
        expected.setPagePath(ACCOUNT_PAGE);
        Router actual = accountCommand.execute(request);
        Assert.assertEquals(actual.getPagePath(), expected.getPagePath());
    }

    @Test(description = "Test when item service throw service exception")
    public void testExecuteItemServiceException() throws ServiceException {
        ServiceException exception = new ServiceException("Error with find paid items");
        when(itemService.findPaidItems(user.getUserId(), request.getServletContext().getRealPath(DEFAULT_ITEM_PATH))).thenThrow(exception);
        when(commentService.findByUserId(ID_FOR_TEST)).thenReturn(new ArrayList<>());
        when(itemService.findByComment(new ArrayList<>())).thenReturn(new HashMap<>());
        when(questionService.findByUserId(ID_FOR_TEST)).thenReturn(new ArrayList<>());
        Router expected = new Router();
        expected.setRedirectPath(ERROR_PATH);
        Router actual = accountCommand.execute(request);
        Assert.assertEquals(actual.getPagePath(), expected.getPagePath());
    }

}
