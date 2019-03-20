package by.chmut.giftit.transfer;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/**
 * The Bank money transfer class provides communication
 * with the bank and transfer payment on the order.
 *
 * @author Sergei Chmut.
 */
public class BankMoneyTransfer {

    /**
     * The method emulates payment on request,
     * checking the data obtained for null.
     *
     * @param parameters the parameters for make payment
     * @param sum        the payment amount
     * @return {@code true} in case of success,
     * {@code false} otherwise
     */
    public static boolean makeTransaction(Map<String, String> parameters, BigDecimal sum) {
        long sizeBefore = parameters.size();
        long sizeAfter = parameters.entrySet()
                .stream()
                .filter(Objects::nonNull)
                .count();
        return sizeBefore == sizeAfter;
    }
}
