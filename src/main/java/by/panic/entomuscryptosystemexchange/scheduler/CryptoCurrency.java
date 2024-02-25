package by.panic.entomuscryptosystemexchange.scheduler;

import by.panic.entomuscryptosystemexchange.api.CoinGeckoApi;
import by.panic.entomuscryptosystemexchange.api.payload.coingecko.CoinGeckoGetSimplePriceResponse;
import by.panic.entomuscryptosystemexchange.entity.enums.CryptoToken;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
@RequiredArgsConstructor
@Slf4j
public class CryptoCurrency {
    private final CoinGeckoApi coinGeckoApi;

    private Map<CryptoToken, Double> usdPrice = new HashMap<>();

    @Scheduled(fixedDelay = 30000)
    public void updateCryptoCurrency() {
        log.info("Starting updating cryptos prices on {}", CryptoCurrency.class);

        CoinGeckoGetSimplePriceResponse getCryptosPrice = coinGeckoApi.getCryptosPrice();

        usdPrice.put(CryptoToken.BTC, getCryptosPrice.getBtc().getUsd());
        usdPrice.put(CryptoToken.ETH, getCryptosPrice.getEth().getUsd());
        usdPrice.put(CryptoToken.ETC, getCryptosPrice.getEtc().getUsd());
        usdPrice.put(CryptoToken.TRX, getCryptosPrice.getTrx().getUsd());
        usdPrice.put(CryptoToken.MATIC, getCryptosPrice.getMatic().getUsd());
        usdPrice.put(CryptoToken.AVAX, getCryptosPrice.getAvax().getUsd());
        usdPrice.put(CryptoToken.BNB, getCryptosPrice.getBnb().getUsd());
        usdPrice.put(CryptoToken.SOL, getCryptosPrice.getSol().getUsd());
        usdPrice.put(CryptoToken.LTC, getCryptosPrice.getLtc().getUsd());
        usdPrice.put(CryptoToken.BCH, getCryptosPrice.getBch().getUsd());
        usdPrice.put(CryptoToken.USDT, getCryptosPrice.getUsdt().getUsd());
        usdPrice.put(CryptoToken.USDC, getCryptosPrice.getUsdc().getUsd());
        usdPrice.put(CryptoToken.DAI, getCryptosPrice.getDai().getUsd());

        System.out.println(usdPrice.get(CryptoToken.BTC));
    }
}
