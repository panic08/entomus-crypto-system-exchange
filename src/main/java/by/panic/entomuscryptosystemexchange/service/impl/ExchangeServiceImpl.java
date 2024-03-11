package by.panic.entomuscryptosystemexchange.service.impl;

import by.panic.entomuscryptosystemexchange.api.NodeFactoryApi;
import by.panic.entomuscryptosystemexchange.api.payload.nodeFactory.*;
import by.panic.entomuscryptosystemexchange.api.payload.nodeFactory.enums.NodeFactoryGetStatusStatus;
import by.panic.entomuscryptosystemexchange.dto.ExchangeDto;
import by.panic.entomuscryptosystemexchange.entity.Exchange;
import by.panic.entomuscryptosystemexchange.entity.enums.CryptoToken;
import by.panic.entomuscryptosystemexchange.entity.enums.ExchangeStatus;
import by.panic.entomuscryptosystemexchange.exception.ExchangeCurrencyException;
import by.panic.entomuscryptosystemexchange.exception.NodeFactoryException;
import by.panic.entomuscryptosystemexchange.mapper.ExchangeToExchangeDtoMapperImpl;
import by.panic.entomuscryptosystemexchange.payload.CreateExchangeRequest;
import by.panic.entomuscryptosystemexchange.payload.GetExchangeRateResponse;
import by.panic.entomuscryptosystemexchange.property.ExchangeCurrencyLimit;
import by.panic.entomuscryptosystemexchange.repository.ExchangeRepository;
import by.panic.entomuscryptosystemexchange.scheduler.CryptoCurrency;
import by.panic.entomuscryptosystemexchange.service.ExchangeService;
import by.panic.entomuscryptosystemexchange.util.QrUtil;
import by.panic.entomuscryptosystemexchange.util.RounderUtil;
import com.google.zxing.WriterException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeServiceImpl implements ExchangeService {
    private final ExecutorService executorService;
    private final ExchangeRepository exchangeRepository;
    private final NodeFactoryApi nodeFactoryApi;
    private final ExchangeToExchangeDtoMapperImpl exchangeToExchangeDtoMapper;
    private final CryptoCurrency cryptoCurrency;
    private final RounderUtil rounderUtil;
    private final QrUtil qrUtil;
    private final ExchangeCurrencyLimit exchangeCurrencyLimit;

    @Value("${exchange.fee}")
    private Double exchangeFee;

    /**
     * Get an exchange-rate of pair give_amount/obtain_amount
     * For example, we can convert trx to btc using this method, also counting the exchange fee
     */

    @Override
    public GetExchangeRateResponse getRate(String giveAmount, CryptoToken givenToken, CryptoToken obtainToken) {
        double givenTokenCurrency = cryptoCurrency.getUsdPrice().get(givenToken);
        double obtainTokenCurrency = cryptoCurrency.getUsdPrice().get(obtainToken);

        BigDecimal givenAmountDecimal = new BigDecimal(giveAmount);

        switch (givenToken) {
            case BTC, BCH, LTC -> givenAmountDecimal = rounderUtil.truncateFractionalPart(givenAmountDecimal, 8);
            case ETH, ETC, BNB, DAI, MATIC, AVAX, SOL -> givenAmountDecimal = rounderUtil.truncateFractionalPart(givenAmountDecimal, 9);
            case TRX, USDT, USDC -> givenAmountDecimal = rounderUtil.truncateFractionalPart(givenAmountDecimal, 6);
        }

        switch (givenToken) {
            case BTC -> {
                BigDecimal exchangeCurrency = BigDecimal.valueOf(exchangeCurrencyLimit.getMinBtc());
                if (givenAmountDecimal.compareTo(exchangeCurrency) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrency.toPlainString() + " for exchange");
                }
            }
            case BCH -> {
                BigDecimal exchangeCurrency = BigDecimal.valueOf(exchangeCurrencyLimit.getMinBch());
                if (givenAmountDecimal.compareTo(exchangeCurrency) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrency.toPlainString() + " for exchange");
                }
            }
            case LTC -> {
                BigDecimal exchangeCurrency = BigDecimal.valueOf(exchangeCurrencyLimit.getMinLtc());
                if (givenAmountDecimal.compareTo(exchangeCurrency) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrency.toPlainString() + " for exchange");
                }
            }
            case ETH -> {
                BigDecimal exchangeCurrency = BigDecimal.valueOf(exchangeCurrencyLimit.getMinEth());
                if (givenAmountDecimal.compareTo(exchangeCurrency) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrency.toPlainString() + " for exchange");
                }
            }
            case ETC -> {
                BigDecimal exchangeCurrency = BigDecimal.valueOf(exchangeCurrencyLimit.getMinEtc());
                if (givenAmountDecimal.compareTo(exchangeCurrency) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrency.toPlainString() + " for exchange");
                }
            }
            case BNB -> {
                BigDecimal exchangeCurrency = BigDecimal.valueOf(exchangeCurrencyLimit.getMinBnb());
                if (givenAmountDecimal.compareTo(exchangeCurrency) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrency.toPlainString() + " for exchange");
                }
            }
            case DAI -> {
                BigDecimal exchangeCurrency = BigDecimal.valueOf(exchangeCurrencyLimit.getMinDai());
                if (givenAmountDecimal.compareTo(exchangeCurrency) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrency.toPlainString() + " for exchange");
                }
            }
            case MATIC -> {
                BigDecimal exchangeCurrency = BigDecimal.valueOf(exchangeCurrencyLimit.getMinPolygon());
                if (givenAmountDecimal.compareTo(exchangeCurrency) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrency.toPlainString() + " for exchange");
                }
            }
            case AVAX -> {
                BigDecimal exchangeCurrency = BigDecimal.valueOf(exchangeCurrencyLimit.getMinAvax());
                if (givenAmountDecimal.compareTo(exchangeCurrency) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrency.toPlainString() + " for exchange");
                }
            }
            case TRX -> {
                BigDecimal exchangeCurrency = BigDecimal.valueOf(exchangeCurrencyLimit.getMinTrx());
                if (givenAmountDecimal.compareTo(exchangeCurrency) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrency.toPlainString() + " for exchange");
                }
            }
            case USDT -> {
                BigDecimal exchangeCurrency = BigDecimal.valueOf(exchangeCurrencyLimit.getMinUsdt());
                if (givenAmountDecimal.compareTo(exchangeCurrency) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrency.toPlainString() + " for exchange");
                }
            }
            case USDC -> {
                BigDecimal exchangeCurrency = BigDecimal.valueOf(exchangeCurrencyLimit.getMinUsdc());
                if (givenAmountDecimal.compareTo(exchangeCurrency) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrency.toPlainString() + " for exchange");
                }
            }
            case SOL -> {
                BigDecimal exchangeCurrency = BigDecimal.valueOf(exchangeCurrencyLimit.getMinSol());
                if (givenAmountDecimal.compareTo(exchangeCurrency) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrency.toPlainString() + " for exchange");
                }
            }
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

        return GetExchangeRateResponse.builder()
                .obtainAmount(obtainAmountDecimal.toPlainString())
                .obtainToken(obtainToken)
                .build();
    }

    /**
     * Create an exchange
     */

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

        switch (createExchangeRequest.getGivenToken()) {
            case BTC -> {
                if (givenAmountDecimal.compareTo(BigDecimal.valueOf(exchangeCurrencyLimit.getMinBtc())) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrencyLimit.getMinBtc() + " for exchange");
                }
            }
            case BCH -> {
                if (givenAmountDecimal.compareTo(BigDecimal.valueOf(exchangeCurrencyLimit.getMinBch())) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrencyLimit.getMinBch() + " for exchange");
                }
            }
            case LTC -> {
                if (givenAmountDecimal.compareTo(BigDecimal.valueOf(exchangeCurrencyLimit.getMinLtc())) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrencyLimit.getMinLtc() + " for exchange");
                }
            }
            case ETH -> {
                if (givenAmountDecimal.compareTo(BigDecimal.valueOf(exchangeCurrencyLimit.getMinEth())) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrencyLimit.getMinEth() + " for exchange");
                }
            }
            case ETC -> {
                if (givenAmountDecimal.compareTo(BigDecimal.valueOf(exchangeCurrencyLimit.getMinEtc())) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrencyLimit.getMinEtc() + " for exchange");
                }
            }
            case BNB -> {
                if (givenAmountDecimal.compareTo(BigDecimal.valueOf(exchangeCurrencyLimit.getMinBnb())) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrencyLimit.getMinBnb() + " for exchange");
                }
            }
            case DAI -> {
                if (givenAmountDecimal.compareTo(BigDecimal.valueOf(exchangeCurrencyLimit.getMinDai())) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrencyLimit.getMinDai() + " for exchange");
                }
            }
            case MATIC -> {
                if (givenAmountDecimal.compareTo(BigDecimal.valueOf(exchangeCurrencyLimit.getMinPolygon())) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrencyLimit.getMinPolygon() + " for exchange");
                }
            }
            case AVAX -> {
                if (givenAmountDecimal.compareTo(BigDecimal.valueOf(exchangeCurrencyLimit.getMinAvax())) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrencyLimit.getMinAvax() + " for exchange");
                }
            }
            case TRX -> {
                if (givenAmountDecimal.compareTo(BigDecimal.valueOf(exchangeCurrencyLimit.getMinTrx())) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrencyLimit.getMinTrx() + " for exchange");
                }
            }
            case USDT -> {
                if (givenAmountDecimal.compareTo(BigDecimal.valueOf(exchangeCurrencyLimit.getMinUsdt())) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrencyLimit.getMinUsdt() + " for exchange");
                }
            }
            case USDC -> {
                if (givenAmountDecimal.compareTo(BigDecimal.valueOf(exchangeCurrencyLimit.getMinUsdc())) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrencyLimit.getMinUsdc() + " for exchange");
                }
            }
            case SOL -> {
                if (givenAmountDecimal.compareTo(BigDecimal.valueOf(exchangeCurrencyLimit.getMinSol())) < 0) {
                    throw new ExchangeCurrencyException("Min " + exchangeCurrencyLimit.getMinSol() + " for exchange");
                }
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

        givenAmount = existsOnDepositWaitingAmount(givenAmount, createExchangeRequest.getGivenToken());

        NodeFactoryReceiveResponse nodeFactoryReceiveResponse = nodeFactoryApi.receive(NodeFactoryReceiveRequest.builder()
                .network(createExchangeRequest.getGivenNetwork())
                .token(createExchangeRequest.getGivenToken())
                .amount(givenAmount)
                .timeout(60)
                .build());

        long createdAt = System.currentTimeMillis();

        switch (createExchangeRequest.getGivenToken()) {
            case BTC, BCH, LTC -> {
                givenAmountDecimal = new BigDecimal(givenAmount).divide(BigDecimal.valueOf(1e8));
            }
            case ETH, ETC, BNB, DAI, MATIC, AVAX -> {
                givenAmountDecimal = new BigDecimal(givenAmount).divide(BigDecimal.valueOf(1e18));
            }
            case TRX, USDT, USDC -> {
                givenAmountDecimal = new BigDecimal(givenAmount).divide(BigDecimal.valueOf(1e6));
            }
            case SOL -> {
                givenAmountDecimal = new BigDecimal(givenAmount).divide(BigDecimal.valueOf(1e9));
            }
        }

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

        Exchange finalNewExchange = newExchange;
        BigInteger finalObtainAmount = obtainAmount;
        executorService.submit(() -> {
            boolean isFounded = false;

            while (System.currentTimeMillis() < finalNewExchange.getExpiredAt()) {
                NodeFactoryGetStatusResponse nodeFactoryGetStatusResponse =
                        nodeFactoryApi.getStatus(nodeFactoryReceiveResponse.getId());

                if (nodeFactoryGetStatusResponse.getStatus().equals(NodeFactoryGetStatusStatus.SUCCESS)) {
                    NodeFactorySendRequest nodeFactorySendRequest = NodeFactorySendRequest.builder()
                                            .address(finalNewExchange.getGivenAddress())
                                            .network(finalNewExchange.getGivenNetwork())
                                            .token(finalNewExchange.getGivenToken())
                                            .amount(finalObtainAmount).build();

                    try {
                        nodeFactoryApi.send(nodeFactorySendRequest);
                    } catch (NodeFactoryException e) {
                        finalNewExchange.setStatus(ExchangeStatus.ERROR);
                        finalNewExchange.setUpdatedAt(System.currentTimeMillis());

                        exchangeRepository.save(finalNewExchange);

                        return;
                    }

                    finalNewExchange.setStatus(ExchangeStatus.COMPLETED);
                    finalNewExchange.setGivenTxId(nodeFactoryGetStatusResponse.getHash());
                    finalNewExchange.setUpdatedAt(System.currentTimeMillis());

                    exchangeRepository.save(finalNewExchange);
                    return;
                } else if (!isFounded &&
                        nodeFactoryGetStatusResponse.getStatus().equals(NodeFactoryGetStatusStatus.FOUND)) {
                    isFounded = true;

                    finalNewExchange.setStatus(ExchangeStatus.CONFIRMATION_WAITING);
                    finalNewExchange.setGivenTxId(nodeFactoryGetStatusResponse.getHash());
                    finalNewExchange.setUpdatedAt(System.currentTimeMillis());

                    exchangeRepository.save(finalNewExchange);
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            finalNewExchange.setStatus(ExchangeStatus.EXPIRED);
            finalNewExchange.setUpdatedAt(System.currentTimeMillis());

            exchangeRepository.save(finalNewExchange);
        });

        ExchangeDto exchangeDto = exchangeToExchangeDtoMapper.exchangeToExchangeDto(newExchange);

        exchangeDto.setGivenAmount(givenAmountDecimal.toPlainString());
        exchangeDto.setObtainAmount(obtainAmountDecimal.toPlainString());

        return exchangeDto;
    }

    /**
     * Get info about the exchange
     */

    @Override
    public ExchangeDto getInfo(String uuid) {
        Exchange exchange = exchangeRepository.findByUuid(uuid);

        if (exchange == null) {
            return null;
        }

        String givenAmountDecimal = null;
        String obtainAmountDecimal = null;

        switch (exchange.getGivenToken()) {
            case BTC, BCH, LTC -> {

                givenAmountDecimal = new BigDecimal(exchange.getGivenAmount()).divide(BigDecimal.valueOf(1e8)).toPlainString();
                obtainAmountDecimal = new BigDecimal(exchange.getObtainAmount()).divide(BigDecimal.valueOf(1e8)).toPlainString();
            }
            case ETH, ETC, BNB, DAI, MATIC, AVAX -> {
                givenAmountDecimal = new BigDecimal(exchange.getGivenAmount()).divide(BigDecimal.valueOf(1e18)).toPlainString();
                obtainAmountDecimal = new BigDecimal(exchange.getObtainAmount()).divide(BigDecimal.valueOf(1e18)).toPlainString();
            }
            case TRX, USDT, USDC -> {
                givenAmountDecimal = new BigDecimal(exchange.getGivenAmount()).divide(BigDecimal.valueOf(1e6)).toPlainString();
                obtainAmountDecimal = new BigDecimal(exchange.getObtainAmount()).divide(BigDecimal.valueOf(1e6)).toPlainString();
            }
            case SOL -> {
                givenAmountDecimal = new BigDecimal(exchange.getGivenAmount()).divide(BigDecimal.valueOf(1e9)).toPlainString();
                obtainAmountDecimal = new BigDecimal(exchange.getObtainAmount()).divide(BigDecimal.valueOf(1e9)).toPlainString();
            }
        }

        exchange.setGivenAmount(givenAmountDecimal);
        exchange.setObtainAmount(obtainAmountDecimal);

        return exchangeToExchangeDtoMapper.exchangeToExchangeDto(exchange);
    }

    /**
     * Create qrcode of the exchange
     */

    @Override
    public ResponseEntity<byte[]> createQr(String uuid) {
        String obtainAddress = exchangeRepository.findObtainAddressByUuid(uuid);
        if (obtainAddress == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] generatedAddress;

        try {
            generatedAddress = qrUtil.generateTextQRCode(obtainAddress, 256, 256);
        } catch (WriterException | IOException e) {
            log.warn(e.getMessage());
            generatedAddress = null;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(generatedAddress);
    }

    private String existOnUuid(String uuid) {
        if (exchangeRepository.existsByUuid(uuid)) {
            return existOnUuid(UUID.randomUUID().toString());
        } else {
            return uuid;
        }
    }

    private BigInteger existsOnDepositWaitingAmount(BigInteger givenAmount, CryptoToken token) {
        if (exchangeRepository.existsByStatusAndGivenAmountAndGivenToken(ExchangeStatus.DEPOSIT_WAITING, givenAmount.toString(), token)) {
            switch (token) {
                case ETH -> givenAmount = givenAmount.add(BigInteger.valueOf((long) 1e12));

                case ETC, AVAX -> givenAmount = givenAmount.add(BigInteger.valueOf((long) 1e14));

                case BNB -> givenAmount = givenAmount.add(BigInteger.valueOf((long) 1e13));

                case MATIC, DAI -> givenAmount = givenAmount.add(BigInteger.valueOf((long) 1e15));

                case BTC -> givenAmount = givenAmount.add(BigInteger.valueOf((long) 1e1));

                case LTC, BCH, USDT, USDC -> givenAmount = givenAmount.add(BigInteger.valueOf((long) 1e3));

                case TRX, SOL -> givenAmount = givenAmount.add(BigInteger.valueOf((long) 1e4));
            }

            return existsOnDepositWaitingAmount(givenAmount, token);
        } else {
            return givenAmount;
        }
    }
}
