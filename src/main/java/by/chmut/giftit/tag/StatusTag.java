package by.chmut.giftit.tag;

import by.chmut.giftit.entity.User;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * The Status tag class creates a custom tag
 * that displays the role of the specified user.
 *
 * @author Sergei Chmut.
 */
public class StatusTag extends TagSupport {

    /**
     * The specified User.
     */
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * The method displays the user's role and username for the specified user.
     *
     * @return the int equals skip body
     * @throws JspException if the tag could not be handled
     */
    @Override
    public int doStartTag() throws JspException {
        if (user != null) {
            try {
                pageContext.getOut().write("<hr/>" + "<p id=status>" + user.getRole() +
                        " : " + user.getUsername() + "</p>" + "<hr/>");
            } catch (IOException exception) {
                throw new JspException(exception.getMessage());
            }
        }
        return SKIP_BODY;
    }
}
