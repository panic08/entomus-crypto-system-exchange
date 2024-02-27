package by.panic.entomuscryptosystemexchange.controller;

import by.panic.entomuscryptosystemexchange.dto.ExchangeDto;
import by.panic.entomuscryptosystemexchange.entity.enums.CryptoToken;
import by.panic.entomuscryptosystemexchange.payload.CreateExchangeRequest;
import by.panic.entomuscryptosystemexchange.payload.GetExchangeRateResponse;
import by.panic.entomuscryptosystemexchange.service.ExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exchange")
@Tag(name = "Exchanges", description = "This component describes the Exchange API")
@RequiredArgsConstructor
public class ExchangeController {
    private final ExchangeService exchangeService;

    @Operation(description = "Get exchange-rate of pair give_amount/obtain_amount")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "We successfully got exchange-rate data",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetExchangeRateResponse.class))}),
    })
    @GetMapping("/rate")
    public GetExchangeRateResponse getRate(@RequestParam("given_amount") String giveAmount,
                                           @RequestParam("given_token") CryptoToken giveToken,
                                           @RequestParam("obtain_token") CryptoToken obtainToken) {
        return exchangeService.getRate(giveAmount, giveToken, obtainToken);
    }

    @Operation(description = "Create exchange")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "We successfully created exchange",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetExchangeRateResponse.class))}),
    })
    @PostMapping("")
    public ExchangeDto create(@RequestBody CreateExchangeRequest createExchangeRequest) {
        return exchangeService.create(createExchangeRequest);
    }
}
