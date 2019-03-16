package by.chmut.giftit.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.math.BigDecimal;

public class FormatTag extends TagSupport {

    private static final BigDecimal HUNDRED = new BigDecimal(100);

    private BigDecimal price;

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            BigDecimal fraction = price.remainder(BigDecimal.ONE);
            int penny = fraction.multiply(HUNDRED).intValue();
            int rubles = price.subtract(fraction).intValue();
            if (penny > 0) {
                pageContext.getOut().write(rubles + " руб. " + penny + " коп.");
            } else {
                pageContext.getOut().write(rubles + " руб. ");
            }
        } catch (IOException exception) {
            throw new JspException(exception.getMessage());
        }
        return SKIP_BODY;
    }
}
