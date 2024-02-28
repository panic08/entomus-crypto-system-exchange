package by.panic.entomuscryptosystemexchange.repository;

import by.panic.entomuscryptosystemexchange.entity.Exchange;
import by.panic.entomuscryptosystemexchange.entity.enums.CryptoToken;
import by.panic.entomuscryptosystemexchange.entity.enums.ExchangeStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends CrudRepository<Exchange, Long> {
    boolean existsByUuid(String uuid);
    boolean existsByStatusAndGivenAmountAndGivenToken(ExchangeStatus exchangeStatus, String givenAmount, CryptoToken givenToken);

    @Query("SELECT e.obtainAddress FROM exchanges_table e WHERE e.uuid = :uuid")
    String findObtainAddressByUuid(@Param("uuid") String uuid);
    Exchange findByUuid(String uuid);
}
