package by.panic.entomuscryptosystemexchange.payload;

import by.panic.entomuscryptosystemexchange.entity.enums.CryptoNetwork;
import by.panic.entomuscryptosystemexchange.entity.enums.CryptoToken;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateExchangeRequest {
    private String givenAmount;
    private CryptoNetwork givenNetwork;
    private CryptoToken givenToken;
    private String givenAddress;

    private CryptoNetwork obtainNetwork;
    private CryptoToken obtainToken;
    private String obtainAddress;
}
