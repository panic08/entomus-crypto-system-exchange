package by.panic.entomuscryptosystemexchange.service;

import by.panic.entomuscryptosystemexchange.dto.ExchangeDto;
import by.panic.entomuscryptosystemexchange.entity.enums.CryptoNetwork;
import by.panic.entomuscryptosystemexchange.entity.enums.CryptoToken;
import by.panic.entomuscryptosystemexchange.payload.CreateExchangeRequest;
import by.panic.entomuscryptosystemexchange.payload.GetExchangeRateResponse;

public interface ExchangeService {
    GetExchangeRateResponse getRate(String giveAmount, CryptoToken giveToken, CryptoToken obtainToken);
    ExchangeDto create(CreateExchangeRequest createExchangeRequest);
}
