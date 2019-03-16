package by.chmut.giftit.tag;

import by.chmut.giftit.entity.User;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class StatusTag extends TagSupport {

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            pageContext.getOut().write("<hr/>" + "<p id=status>" + user.getRole() +
                    " : " + user.getUsername() + "</p>" + "<hr/>");
        } catch (IOException exception) {
            throw new JspException(exception.getMessage());
        }
        return SKIP_BODY;
    }
}
