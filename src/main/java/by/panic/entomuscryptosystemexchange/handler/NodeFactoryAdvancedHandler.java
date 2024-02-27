package by.panic.entomuscryptosystemexchange.handler;

import by.panic.entomuscryptosystemexchange.exception.NodeFactoryException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NodeFactoryAdvancedHandler {
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NodeFactoryException.class)
    public by.panic.entomuscryptosystemexchange.payload.ExceptionHandler handleNodeFactoryException(NodeFactoryException nodeFactoryException) {
        return by.panic.entomuscryptosystemexchange.payload.ExceptionHandler.builder()
                .exceptionMessage(nodeFactoryException.getMessage())
                .build();
    }
}
