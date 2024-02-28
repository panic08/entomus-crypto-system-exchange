package by.panic.entomuscryptosystemexchange.dto;

import by.panic.entomuscryptosystemexchange.entity.enums.CryptoNetwork;
import by.panic.entomuscryptosystemexchange.entity.enums.CryptoToken;
import by.panic.entomuscryptosystemexchange.entity.enums.ExchangeStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "Exchange")
public class ExchangeDto {
    private ExchangeStatus status;

    private String uuid;

    private String givenAmount;

    private CryptoNetwork givenNetwork;

    private CryptoToken givenToken;

    private String givenAddress;

    private String givenTxId;

    private String obtainAmount;

    private CryptoNetwork obtainNetwork;

    private CryptoToken obtainToken;

    private String obtainAddress;

    private long expiredAt;

    private Long updatedAt;

    private long createdAt;
}
