package by.panic.entomuscryptosystemexchange.controller;

import by.panic.entomuscryptosystemexchange.dto.ExchangeDto;
import by.panic.entomuscryptosystemexchange.entity.enums.CryptoToken;
import by.panic.entomuscryptosystemexchange.payload.CreateExchangeRequest;
import by.panic.entomuscryptosystemexchange.payload.ExceptionHandler;
import by.panic.entomuscryptosystemexchange.payload.GetExchangeRateResponse;
import by.panic.entomuscryptosystemexchange.service.ExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exchange")
@Tag(name = "Exchanges", description = "This component describes the Exchange API")
@RequiredArgsConstructor
public class ExchangeController {
    private final ExchangeService exchangeService;

    @Operation(description = "Get an exchange-rate of pair give_amount/obtain_amount")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "We successfully got an exchange-rate data",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetExchangeRateResponse.class))}),
            @ApiResponse(responseCode = "200", description = "Min {} for exchange",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionHandler.class))})
    })
    @GetMapping("/rate")
    public GetExchangeRateResponse getRate(@RequestParam("given_amount") String giveAmount,
                                           @RequestParam("given_token") CryptoToken giveToken,
                                           @RequestParam("obtain_token") CryptoToken obtainToken) {
        return exchangeService.getRate(giveAmount, giveToken, obtainToken);
    }

    @Operation(description = "Create an exchange")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "We successfully created an exchange",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExchangeDto.class))}),
            @ApiResponse(responseCode = "400", description = "You have entered an invalid Network-Token pair || NodeFactory service system error" +
                    " || Min {} for exchange",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionHandler.class))}),
    })
    @PostMapping
    public ExchangeDto create(@RequestBody CreateExchangeRequest createExchangeRequest) {
        return exchangeService.create(createExchangeRequest);
    }


    @Operation(description = "Get info about the exchange")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "We successfully got info about the exchange",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExchangeDto.class))})
    })
    @GetMapping("/info")
    public ExchangeDto getInfo(@RequestParam("uuid") String uuid) {
        return exchangeService.getInfo(uuid);
    }

    @Operation(description = "Generate a QR-code for the exchange")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "We have got QR-code for the exchange"),
            @ApiResponse(responseCode = "404", description = "Exchange not found")
    })
    @GetMapping("/qr")
    public ResponseEntity<byte[]> createQr(@RequestParam("uuid") String uuid) {
        return exchangeService.createQr(uuid);
    }
}
