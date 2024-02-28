package by.panic.entomuscryptosystemexchange.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("exchanges.limit")
@Getter
@Setter
public class ExchangeCurrencyLimit {
    private Double minEth;

    private Double minBtc;

    private Double minTrx;

    private Double minBnb;

    private Double minPolygon;

    private Double minEtc;

    private Double minAvax;

    private Double minBch;

    private Double minSol;

    private Double minLtc;

    private Double minUsdt;
    private Double minUsdc;
    private Double minDai;
}
