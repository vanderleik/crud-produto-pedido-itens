package com.produtopedidoitens.api.application.domain.entities;

import com.produtopedidoitens.api.application.domain.enums.EnumProductType;
import com.produtopedidoitens.api.utils.MessagesConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tbproduct")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    @Column(name = "idproduct")
    private UUID id;

    @NotBlank(message = MessagesConstants.PRODUCT_NAME_NOT_BLANK)
    @Size(max = 60)
    @Column(name = "productname")
    private String productName;

    @NotNull(message = MessagesConstants.PRODUCT_PRICE_NOT_NULL)
    @Positive(message = MessagesConstants.PRODUCT_PRICE_POSITIVE)
    @Positive
    @Column(name = "price")
    private BigDecimal price = BigDecimal.ZERO;

    @NotNull(message = MessagesConstants.PRODUCT_TYPE_NOT_NULL)
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EnumProductType type;

    @NotNull(message = MessagesConstants.PRODUCT_ACTIVE_NOT_NULL)
    @Column(name = "active")
    private Boolean active;

    @CreationTimestamp
    @Column(name = "dthreg")
    private LocalDateTime dthreg;

    @UpdateTimestamp
    @Column(name = "dthalt")
    private LocalDateTime dthalt;

    @Version
    private Long version;

}
