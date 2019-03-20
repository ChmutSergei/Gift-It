package by.chmut.giftit.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * The Format tag class converts the set price
 * to the monetary format for Belarus rubles.
 *
 * @author Sergei Chmut.
 */
public class FormatTag extends TagSupport {

    /**
     * The constant HUNDRED.
     */
    private static final BigDecimal HUNDRED = new BigDecimal(100);

    /**
     * The specified Price.
     */
    private BigDecimal price;

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * The method accepts the price in BigDecimal
     * and parses the value into rubles and pennies.
     * The result prints to the tag.
     *
     * @return the int equals skip body
     * @throws JspException if the tag could not be handled
     */
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
