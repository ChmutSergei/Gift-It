package by.chmut.giftit.command;

import by.chmut.giftit.command.impl.AboutCommand;
import by.chmut.giftit.controller.Router;
import org.mockito.Mock;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;

import static by.chmut.giftit.constant.PathPage.ABOUT_PAGE;
import static org.mockito.Mockito.mock;

public class AboutCommandTest {

    @Mock
    private HttpServletRequest request;

    private AboutCommand aboutCommand;


    @BeforeMethod
    public void setUp() {
        request = mock(HttpServletRequest.class);
        aboutCommand = new AboutCommand();
    }

    @AfterMethod
    public void tearDown() {
        request = null;
        aboutCommand = null;
    }

    @Test(description = "Test that AboutCommand return router with path to About Page.")
    public void testExecute() {
        Router expected = new Router();
        expected.setPagePath(ABOUT_PAGE);
        Router actual = aboutCommand.execute(request);
        Assert.assertEquals(actual.getPagePath(), expected.getPagePath());
    }
}
