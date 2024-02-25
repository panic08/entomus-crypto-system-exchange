package by.panic.entomuscryptosystemexchange.controller;

import by.panic.entomuscryptosystemexchange.entity.enums.CryptoNetwork;
import by.panic.entomuscryptosystemexchange.entity.enums.CryptoToken;
import by.panic.entomuscryptosystemexchange.payload.GetExchangeRateResponse;
import by.panic.entomuscryptosystemexchange.service.ExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/exchange")
@Tag(name = "Exchanges", description = "This component describes the Exchange API")
@RequiredArgsConstructor
public class ExchangeController {
    private final ExchangeService exchangeService;

    @Operation(description = "We got exchange-rate of pair give_amount/obtain_amount")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "We successfully got exchange-rate data",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetExchangeRateResponse.class))}),
    })
    @GetMapping("/rate")
    public GetExchangeRateResponse getRate(@RequestParam("give_amount") String giveAmount,
                                           @RequestParam("give_token") CryptoToken giveToken,
                                           @RequestParam("obtain_token") CryptoToken obtainToken) {
        return exchangeService.getRate(giveAmount, giveToken, obtainToken);
    }
}
