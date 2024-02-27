package by.panic.entomuscryptosystemexchange.mapper;

import by.panic.entomuscryptosystemexchange.dto.ExchangeDto;
import by.panic.entomuscryptosystemexchange.entity.Exchange;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ExchangeToExchangeDtoMapper {
    @Mappings({
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "uuid", source = "uuid"),
            @Mapping(target = "givenAmount", source = "givenAmount"),
            @Mapping(target = "givenNetwork", source = "givenNetwork"),
            @Mapping(target = "givenToken", source = "givenToken"),
            @Mapping(target = "obtainAmount", source = "obtainAmount"),
            @Mapping(target = "obtainNetwork", source = "obtainNetwork"),
            @Mapping(target = "obtainToken", source = "obtainToken"),
            @Mapping(target = "obtainAddress", source = "obtainAddress"),
            @Mapping(target = "expiredAt", source = "expiredAt"),
            @Mapping(target = "createdAt", source = "createdAt"),
    })
    ExchangeDto exchangeToExchangeDto(Exchange exchange);

}
