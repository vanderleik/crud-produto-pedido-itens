package com.produtopedidoitens.api.adapters.persistence.repositories;

import com.produtopedidoitens.api.adapters.web.projections.OrderByOrderNumber;
import com.produtopedidoitens.api.domain.entities.QCatalogItemEntity;
import com.produtopedidoitens.api.domain.entities.QOrderEntity;
import com.produtopedidoitens.api.domain.entities.QOrderItemEntity;
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
        QCatalogItemEntity catalogItem = QCatalogItemEntity.catalogItemEntity;

        return queryFactory
                .select(Projections.constructor(OrderByOrderNumber.class,
                        catalogItem.catalogItemName,
                        order.orderNumber,
                        order.status.stringValue(),
                        orderItem.quantity,
                        catalogItem.price))
                .from(orderItem)
                .innerJoin(orderItem.catalogItem, catalogItem)
                .innerJoin(orderItem.order, order)
                .where(order.orderNumber.eq(orderNumber))
                .fetch();
    }

}
