package by.chmut.giftit.transfer;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

public class BankMoneyTransfer {

    public static boolean makeTransaction(Map<String, String> parameters, BigDecimal sum) {
        long sizeBefore = parameters.size();
        long sizeAfter = parameters.entrySet()
                                    .stream()
                                    .filter(Objects::nonNull)
                                    .count();
        return sizeBefore == sizeAfter;
    }
}
