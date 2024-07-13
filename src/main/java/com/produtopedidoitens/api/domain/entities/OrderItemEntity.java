package com.produtopedidoitens.api.domain.entities;

import com.produtopedidoitens.api.utils.MessagesConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tborderitem")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    @Column(name = "idorderitem")
    private UUID id;

    @NotNull(message = MessagesConstants.ORDER_ITEM_QUANTITY_NOT_NULL)
    @Positive(message = MessagesConstants.ORDER_ITEM_QUANTITY_POSITIVE)
    @Column(name = "quantity")
    private Integer quantity;

    @NotNull(message = MessagesConstants.ORDER_ITEM_PRODUCT_NOT_NULL)
    @ManyToOne
    @JoinColumn(name = "idproduct")
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "idorder")
    private OrderEntity order;

    @CreationTimestamp
    @Column(name = "dthreg")
    private LocalDateTime dthreg;

    @UpdateTimestamp
    @Column(name = "dthalt")
    private LocalDateTime dthalt;

    @Version
    private Long version;

}
