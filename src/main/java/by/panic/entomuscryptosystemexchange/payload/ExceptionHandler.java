package by.panic.entomuscryptosystemexchange.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(name = "Exception")
public class ExceptionHandler {
    @JsonProperty("exception_message")
    private String exceptionMessage;
}
