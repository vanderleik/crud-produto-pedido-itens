package com.produtopedidoitens.api.application.domain.entities;

import com.produtopedidoitens.api.application.domain.enums.EnumOrderStatus;
import com.produtopedidoitens.api.utils.MessagesConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tborder")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    @Column(name = "idorder")
    private UUID id;

    @NotNull(message = MessagesConstants.ORDER_NUMBER_NOT_NULL)
    @Column(name = "ordernumber")
    private String orderNumber;

    @NotNull(message = MessagesConstants.ORDER_DATE_NOT_NULL)
    @Column(name = "orderdate")
    private LocalDate orderDate;

    @NotNull(message = MessagesConstants.ORDER_STATUS_NOT_NULL)
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EnumOrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItemEntity> items = new ArrayList<>();

    @Column(name = "grosstotal")
    private BigDecimal grossTotal = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", inclusive = false, message = MessagesConstants.ORDER_DISCOUNT_MIN)
    @DecimalMax(value = "100.0", inclusive = true, message = MessagesConstants.ORDER_DISCOUNT_MAX)
    @Column(name = "discount")
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name = "nettotal")
    private BigDecimal netTotal = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "dthreg")
    private LocalDateTime dthreg;

    @UpdateTimestamp
    @Column(name = "dthalt")
    private LocalDateTime dthalt;

    @Version
    private Long version;

}
