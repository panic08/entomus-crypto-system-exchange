package by.panic.entomuscryptosystemexchange.service.impl;

import by.panic.entomuscryptosystemexchange.api.NodeFactoryApi;
import by.panic.entomuscryptosystemexchange.api.payload.nodeFactory.NodeFactoryReceiveRequest;
import by.panic.entomuscryptosystemexchange.api.payload.nodeFactory.NodeFactoryReceiveResponse;
import by.panic.entomuscryptosystemexchange.dto.ExchangeDto;
import by.panic.entomuscryptosystemexchange.entity.Exchange;
import by.panic.entomuscryptosystemexchange.entity.enums.CryptoToken;
import by.panic.entomuscryptosystemexchange.entity.enums.ExchangeStatus;
import by.panic.entomuscryptosystemexchange.mapper.ExchangeToExchangeDtoMapperImpl;
import by.panic.entomuscryptosystemexchange.payload.CreateExchangeRequest;
import by.panic.entomuscryptosystemexchange.payload.GetExchangeRateResponse;
import by.panic.entomuscryptosystemexchange.repository.ExchangeRepository;
import by.panic.entomuscryptosystemexchange.scheduler.CryptoCurrency;
import by.panic.entomuscryptosystemexchange.service.ExchangeService;
import by.panic.entomuscryptosystemexchange.util.RounderUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {
    private final ExchangeRepository exchangeRepository;
    private final ExchangeToExchangeDtoMapperImpl exchangeToExchangeDtoMapper;
    private final CryptoCurrency cryptoCurrency;
    private final RounderUtil rounderUtil;
    private final NodeFactoryApi nodeFactoryApi;

    @Value("${exchange.fee}")
    private Double exchangeFee;

    @Override
    public GetExchangeRateResponse getRate(String giveAmount, CryptoToken givenToken, CryptoToken obtainToken) {
//        double givenTokenCurrency = cryptoCurrency.getUsdPrice().get(givenToken);
//        double obtainTokenCurrency = cryptoCurrency.getUsdPrice().get(obtainToken);
//
//        double exchangeRate = (givenTokenCurrency / obtainTokenCurrency) * Double.parseDouble(giveAmount);
//
//        exchangeRate = exchangeRate - (exchangeRate * exchangeFee);

        double givenTokenCurrency = cryptoCurrency.getUsdPrice().get(givenToken);
        double obtainTokenCurrency = cryptoCurrency.getUsdPrice().get(obtainToken);

        BigDecimal givenAmountDecimal = new BigDecimal(giveAmount);

        switch (givenToken) {
            case BTC, BCH, LTC -> givenAmountDecimal = rounderUtil.truncateFractionalPart(givenAmountDecimal, 8);
            case ETH, ETC, BNB, DAI, MATIC, AVAX, SOL -> givenAmountDecimal = rounderUtil.truncateFractionalPart(givenAmountDecimal, 9);
            case TRX, USDT, USDC -> givenAmountDecimal = rounderUtil.truncateFractionalPart(givenAmountDecimal, 6);
        }


        BigDecimal obtainAmountDecimal = BigDecimal.valueOf(givenTokenCurrency / obtainTokenCurrency);
        obtainAmountDecimal = obtainAmountDecimal.multiply(givenAmountDecimal);

        obtainAmountDecimal = obtainAmountDecimal.subtract(obtainAmountDecimal.multiply(BigDecimal.valueOf(exchangeFee)));

        switch (obtainToken) {
            case BTC, BCH, LTC -> obtainAmountDecimal = rounderUtil.truncateFractionalPart(obtainAmountDecimal, 5);
            case ETH, ETC, BNB, DAI, MATIC, AVAX -> obtainAmountDecimal = rounderUtil.truncateFractionalPart(obtainAmountDecimal, 5);
            case TRX, USDT, USDC -> obtainAmountDecimal = rounderUtil.truncateFractionalPart(obtainAmountDecimal, 4);
            case SOL -> obtainAmountDecimal = rounderUtil.truncateFractionalPart(obtainAmountDecimal, 4);
        }

        System.out.println(obtainAmountDecimal);

        return GetExchangeRateResponse.builder()
                .obtainAmount(obtainAmountDecimal.toPlainString())
                .obtainToken(obtainToken)
                .build();
    }

    @Override
    @Transactional
    public ExchangeDto create(CreateExchangeRequest createExchangeRequest) {
        double givenTokenCurrency = cryptoCurrency.getUsdPrice().get(createExchangeRequest.getGivenToken());
        double obtainTokenCurrency = cryptoCurrency.getUsdPrice().get(createExchangeRequest.getObtainToken());

        BigDecimal givenAmountDecimal = new BigDecimal(createExchangeRequest.getGivenAmount());
        BigInteger givenAmount = null;

        switch (createExchangeRequest.getGivenToken()) {
            case BTC, BCH, LTC -> {
                givenAmountDecimal = rounderUtil.truncateFractionalPart(givenAmountDecimal, 8);

                givenAmount = givenAmountDecimal.multiply(BigDecimal.valueOf(1e8)).toBigInteger();
            }
            case ETH, ETC, BNB, DAI, MATIC, AVAX -> {
                givenAmountDecimal = rounderUtil.truncateFractionalPart(givenAmountDecimal, 9);

                givenAmount = givenAmountDecimal.multiply(BigDecimal.valueOf(1e18)).toBigInteger();
            }
            case TRX, USDT, USDC -> {
                givenAmountDecimal = rounderUtil.truncateFractionalPart(givenAmountDecimal, 6);

                givenAmount = givenAmountDecimal.multiply(BigDecimal.valueOf(1e6)).toBigInteger();
            }
            case SOL -> {
                givenAmountDecimal = rounderUtil.truncateFractionalPart(givenAmountDecimal, 9);

                givenAmount = givenAmountDecimal.multiply(BigDecimal.valueOf(1e9)).toBigInteger();
            }
        }


        BigDecimal obtainAmountDecimal = BigDecimal.valueOf(givenTokenCurrency / obtainTokenCurrency);
        obtainAmountDecimal = obtainAmountDecimal.multiply(givenAmountDecimal);

        obtainAmountDecimal = obtainAmountDecimal.subtract(obtainAmountDecimal.multiply(BigDecimal.valueOf(exchangeFee)));

        BigInteger obtainAmount = null;

        switch (createExchangeRequest.getObtainToken()) {
            case BTC, BCH, LTC -> {
                obtainAmountDecimal = rounderUtil.truncateFractionalPart(obtainAmountDecimal, 5);

                obtainAmount = obtainAmountDecimal.multiply(BigDecimal.valueOf(1e8)).toBigInteger();
            }
            case ETH, ETC, BNB, DAI, MATIC, AVAX -> {
                obtainAmountDecimal = rounderUtil.truncateFractionalPart(obtainAmountDecimal, 5);

                obtainAmount = obtainAmountDecimal.multiply(BigDecimal.valueOf(1e18)).toBigInteger();
            }
            case TRX, USDT, USDC -> {
                obtainAmountDecimal = rounderUtil.truncateFractionalPart(obtainAmountDecimal, 4);

                obtainAmount = obtainAmountDecimal.multiply(BigDecimal.valueOf(1e6)).toBigInteger();
            }
            case SOL -> {
                obtainAmountDecimal = rounderUtil.truncateFractionalPart(obtainAmountDecimal, 4);

                obtainAmount = obtainAmountDecimal.multiply(BigDecimal.valueOf(1e9)).toBigInteger();
            }
        }

        System.out.println(givenAmountDecimal + " " + givenAmount);

        System.out.println(obtainAmountDecimal.toPlainString() + " " + obtainAmount);

        NodeFactoryReceiveResponse nodeFactoryReceiveResponse = nodeFactoryApi.receive(NodeFactoryReceiveRequest.builder()
                .network(createExchangeRequest.getGivenNetwork())
                .token(createExchangeRequest.getGivenToken())
                .amount(givenAmount)
                .timeout(3600)
                .build());
        //sendNode

        long createdAt = System.currentTimeMillis();

        Exchange newExchange = Exchange.builder()
                .status(ExchangeStatus.DEPOSIT_WAITING)
                .uuid(existOnUuid(UUID.randomUUID().toString()))
                .givenNetwork(createExchangeRequest.getGivenNetwork())
                .givenToken(createExchangeRequest.getGivenToken())
                .givenAmount(givenAmount.toString())
                .givenAddress(createExchangeRequest.getGivenAddress())

                .obtainNetwork(createExchangeRequest.getObtainNetwork())
                .obtainToken(createExchangeRequest.getObtainToken())
                .obtainAmount(obtainAmount.toString())
                .obtainAddress(nodeFactoryReceiveResponse.getAddress())
                .expiredAt(createdAt + (3600 * 1000))
                .createdAt(createdAt)
                .build();

        newExchange = exchangeRepository.save(newExchange);

        newExchange.setGivenAmount(givenAmountDecimal.toPlainString());
        newExchange.setObtainAmount(obtainAmountDecimal.toPlainString());

        return exchangeToExchangeDtoMapper.exchangeToExchangeDto(newExchange);
    }

    private String existOnUuid(String uuid) {
        if (exchangeRepository.existsByUuid(uuid)) {
            return existOnUuid(UUID.randomUUID().toString());
        } else {
            return uuid;
        }
    }
}
