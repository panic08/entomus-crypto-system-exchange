package by.panic.entomuscryptosystemexchange.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

@Component
public class RounderUtil {
    public BigDecimal truncateFractionalPart(BigDecimal original, long n) {
        // Округляем дробную часть до n знаков
        return original.setScale((int) n, BigDecimal.ROUND_DOWN);
    }

    public BigInteger replaceDigitsWithZero(BigInteger number, int index) {
        if (index < 0 || index >= number.toString().length()) {
            throw new IllegalArgumentException("Index out of bounds");
        }

        String numberString = number.toString();
        StringBuilder modifiedNumber = new StringBuilder(numberString.substring(0, index));

        for (int i = index; i < numberString.length(); i++) {
            modifiedNumber.append("0");
        }

        return new BigInteger(modifiedNumber.toString());
    }
}