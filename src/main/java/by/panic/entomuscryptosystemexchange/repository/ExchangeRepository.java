package by.panic.entomuscryptosystemexchange.repository;

import by.panic.entomuscryptosystemexchange.entity.Exchange;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends CrudRepository<Exchange, Long> {
    boolean existsByUuid(String uuid);
}
