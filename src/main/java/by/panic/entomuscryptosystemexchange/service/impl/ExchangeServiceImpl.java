package by.panic.entomuscryptosystemexchange.service.impl;

import by.panic.entomuscryptosystemexchange.entity.enums.CryptoToken;
import by.panic.entomuscryptosystemexchange.payload.GetExchangeRateResponse;
import by.panic.entomuscryptosystemexchange.scheduler.CryptoCurrency;
import by.panic.entomuscryptosystemexchange.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {
    private final CryptoCurrency cryptoCurrency;

    @Value("${exchange.fee}")
    private Double exchangeFee;

    @Override
    public GetExchangeRateResponse getRate(String giveAmount, CryptoToken giveToken, CryptoToken obtainToken) {
        double giveTokenCurrency = cryptoCurrency.getUsdPrice().get(giveToken);
        double obtainTokenCurrency = cryptoCurrency.getUsdPrice().get(obtainToken);

        double exchangeRate = (giveTokenCurrency / obtainTokenCurrency) * Double.parseDouble(giveAmount);

        exchangeRate = exchangeRate - (exchangeRate * exchangeFee);

        return GetExchangeRateResponse.builder()
                .obtainAmount(String.valueOf(exchangeRate))
                .obtainToken(obtainToken)
                .build();
    }
}
