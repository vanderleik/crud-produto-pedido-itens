package com.produtopedidoitens.api.adapters.persistence.repositories;

import com.produtopedidoitens.api.adapters.web.projections.OrderByOrderNumber;
import com.produtopedidoitens.api.domain.entities.QOrderEntity;
import com.produtopedidoitens.api.domain.entities.QOrderItemEntity;
import com.produtopedidoitens.api.domain.entities.QProductEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderItemRepositoryCustomImpl implements OrderItemRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<OrderByOrderNumber> findOrdersByOrderNumber(String orderNumber) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QOrderEntity order = QOrderEntity.orderEntity;
        QOrderItemEntity orderItem = QOrderItemEntity.orderItemEntity;
        QProductEntity product = QProductEntity.productEntity;

        return queryFactory
                .select(Projections.constructor(OrderByOrderNumber.class,
                        product.productName,
                        order.orderNumber,
                        order.status.stringValue(),
                        orderItem.quantity,
                        product.price))
                .from(orderItem)
                .innerJoin(orderItem.product, product)
                .innerJoin(orderItem.order, order)
                .where(order.orderNumber.eq(orderNumber))
                .fetch();
    }

}
