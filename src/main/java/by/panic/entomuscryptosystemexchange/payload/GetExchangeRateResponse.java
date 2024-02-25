package by.panic.entomuscryptosystemexchange.payload;

import by.panic.entomuscryptosystemexchange.entity.enums.CryptoToken;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetExchangeRateResponse {
    private String obtainAmount;
    private CryptoToken obtainToken;
}
