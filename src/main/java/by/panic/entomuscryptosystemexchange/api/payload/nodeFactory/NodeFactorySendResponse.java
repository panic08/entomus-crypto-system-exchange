package by.panic.entomuscryptosystemexchange.api.payload.nodeFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeFactorySendResponse {
    @JsonProperty("Hash")
    private String hash;
}
