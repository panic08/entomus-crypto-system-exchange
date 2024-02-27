package by.panic.entomuscryptosystemexchange.api.payload.nodeFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeFactoryReceiveResponse {
    @JsonProperty("id")
    private long id;

    @JsonProperty("address")
    private String address;
}