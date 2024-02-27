package by.panic.entomuscryptosystemexchange.api.payload.nodeFactory;

import by.panic.entomuscryptosystemexchange.api.payload.nodeFactory.enums.NodeFactoryGetStatusStatus;
import by.panic.entomuscryptosystemexchange.entity.enums.CryptoNetwork;
import by.panic.entomuscryptosystemexchange.entity.enums.CryptoToken;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeFactoryGetStatusResponse {
    @JsonProperty("id")
    private long id;

    private int block;

    private String hash;

    @JsonProperty("confirmations")
    private int confirmation;

    @JsonProperty("blockchain")
    private CryptoNetwork network;

    private CryptoToken token;

    private String amount;

    private NodeFactoryGetStatusStatus status;

    @JsonProperty("toaddress")
    private String toAddress;
}
