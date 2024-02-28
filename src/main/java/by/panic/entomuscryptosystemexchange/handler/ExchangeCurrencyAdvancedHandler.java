package by.panic.entomuscryptosystemexchange.handler;

import by.panic.entomuscryptosystemexchange.exception.ExchangeCurrencyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExchangeCurrencyAdvancedHandler {
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExchangeCurrencyException.class)
    public by.panic.entomuscryptosystemexchange.payload.ExceptionHandler handleExchangeCurrencyException(ExchangeCurrencyException
                                                                                                                     exchangeCurrencyException) {
        return by.panic.entomuscryptosystemexchange.payload.ExceptionHandler.builder()
                .exceptionMessage(exchangeCurrencyException.getMessage())
                .build();
    }
}
