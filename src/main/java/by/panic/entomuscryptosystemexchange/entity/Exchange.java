package by.panic.entomuscryptosystemexchange.entity;

import by.panic.entomuscryptosystemexchange.entity.enums.CryptoNetwork;
import by.panic.entomuscryptosystemexchange.entity.enums.CryptoToken;
import by.panic.entomuscryptosystemexchange.entity.enums.ExchangeStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "exchanges_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Exchange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false)
    private ExchangeStatus status;

    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "given_amount", nullable = false)
    private String givenAmount;

    @Column(name = "given_network", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CryptoNetwork givenNetwork;

    @Column(name = "given_token", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CryptoToken givenToken;

    @Column(name = "given_address", nullable = false)
    private String givenAddress;

    @Column(name = "given_tx_id", nullable = true)
    private String givenTxId;

    @Column(name = "obtain_amount", nullable = false)
    private String obtainAmount;

    @Column(name = "obtain_network", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CryptoNetwork obtainNetwork;

    @Column(name = "obtain_token", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CryptoToken obtainToken;

    @Column(name = "obtain_address", nullable = false)
    private String obtainAddress;

    @Column(name = "expired_at", nullable = false)
    private Long expiredAt;

    @Column(name = "updated_at", nullable = true)
    private Long updatedAt;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;
}
