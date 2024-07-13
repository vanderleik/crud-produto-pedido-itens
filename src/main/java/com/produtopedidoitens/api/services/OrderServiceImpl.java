package com.produtopedidoitens.api.services;

import com.produtopedidoitens.api.adapters.persistence.repositories.OrderRepository;
import com.produtopedidoitens.api.adapters.web.filters.OrderFilter;
import com.produtopedidoitens.api.adapters.web.projections.OrderProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderResponse;
import com.produtopedidoitens.api.domain.entities.OrderEntity;
import com.produtopedidoitens.api.domain.enums.EnumOrderStatus;
import com.produtopedidoitens.api.application.exceptions.BadRequestException;
import com.produtopedidoitens.api.application.exceptions.OrderNotFoundException;
import com.produtopedidoitens.api.application.mapper.EnumConverter;
import com.produtopedidoitens.api.application.mapper.OrderConverter;
import com.produtopedidoitens.api.application.port.OrderInputPort;
import com.produtopedidoitens.api.utils.MessagesConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderInputPort {

    private final OrderRepository orderRepository;
    private final OrderConverter orderConverter;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse create(OrderRequest orderRequest) {
        log.info("create:: Recebendo OrderRequest: {}", orderRequest);
        try {
            OrderEntity entitySaved = orderRepository.save(getEntity(orderRequest));
            OrderResponse response = orderConverter.toResponse(entitySaved);
            log.info("create:: Salvando pedido: {}", response);
            return response;
        } catch (Exception e) {
            log.error("create:: Ocorreu um erro ao salvar pedido: {}");
            throw new BadRequestException(MessagesConstants.ERROR_SAVE_ORDER);
        }
    }

    @Override
    public Page<OrderProjection> list(Pageable pageable) {
        log.info("list:: Listando pedidos");
        List<OrderEntity> list = getOrderEntities();
        List<OrderProjection> orderProjectionList = list.stream().map(orderConverter::toProjection).toList();
        log.info("list:: Pedidos encontrados: {}", orderProjectionList);

        return new PageImpl<>(orderProjectionList, pageable, orderProjectionList.size());
    }

    @Override
    public OrderProjection read(UUID id) {
        log.info("read:: Buscando pedido por id: {}", id);
        OrderEntity entity = getOrderEntity(id);
        OrderProjection orderProjection = orderConverter.toProjection(entity);
        log.info("read:: Pedido encontrado: {}", orderProjection);
        return orderProjection;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse update(UUID id, OrderRequest orderRequest) {
        log.info("update:: Recebendo requisição para atualizar pedido: {}", orderRequest);
        OrderEntity entity = getOrderEntity(id);
        updateEntity(entity, orderRequest);
        try {
            OrderEntity entitySaved = orderRepository.save(entity);
            OrderResponse response = orderConverter.toResponse(entitySaved);
            log.info("update:: Atualizando pedido: {}", response);
            return response;
        } catch (Exception e) {
            log.error("update:: Ocorreu um erro ao atualizar pedido");
            throw new BadRequestException(MessagesConstants.ERROR_UPDATE_ORDER);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(UUID id) {
        log.info("delete:: Recebendo requisição para deletar pedido por id: {}", id);
        OrderEntity entity = getOrderEntity(id);
        try {
            orderRepository.delete(entity);
            log.info("delete:: Deletando pedido da base: {}", entity);
        } catch (Exception e) {
            log.error("delete:: Ocorreu um erro ao deletar pedido");
            throw new BadRequestException(MessagesConstants.ERROR_DELETE_ORDER);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<OrderProjection> getItemsWithFilters(String orderNumber, String status, Pageable pageable) {
        log.info("getItemsWithFilters:: Recebendo requisição para buscar pedidos com filtros");
        Specification<OrderEntity> specification = OrderFilter.filterByCriteria(orderNumber, status);
        Page<OrderEntity> orderPage = orderRepository.findAll(specification, pageable);
        return orderPage.map(order -> new OrderProjection(
                order.getId(),
                order.getOrderNumber(),
                order.getOrderDate(),
                order.getStatus().toString(),
                null, //TODO order.getItems(),
                order.getGrossTotal(),
                order.getDiscount(),
                order.getNetTotal()
        ));
    }

    private static void updateEntity(OrderEntity entity, OrderRequest orderRequest) {
        entity.setOrderDate(orderRequest.orderDate() == null ? entity.getOrderDate() : orderRequest.orderDate());
        entity.setStatus(orderRequest.status() == null ? entity.getStatus() : EnumConverter.fromString(orderRequest.status(), EnumOrderStatus.class));
//        entity.setItems(orderRequest.items() == null ? entity.getItems() : new ArrayList<>());// TODO
//        entity.setGrossTotal(orderRequest.grossTotal() == null ? entity.getGrossTotal() : new BigDecimal(orderRequest.grossTotal()));
        entity.setDiscount(orderRequest.discount() == null ? entity.getDiscount() : new BigDecimal(orderRequest.discount()));
//        entity.setNetTotal(orderRequest.netTotal() == null ? entity.getNetTotal() : new BigDecimal(orderRequest.netTotal()));
    }

    private OrderEntity getOrderEntity(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("read:: Ocorreu um erro ao buscar pedido por id: {}", MessagesConstants.ERROR_NOT_FOUND_ORDER);
                    throw new OrderNotFoundException(MessagesConstants.ERROR_NOT_FOUND_ORDER);
                });
    }

    private OrderEntity getEntity(OrderRequest orderRequest) {
        return orderConverter.toEntity(orderRequest);
    }

    private List<OrderEntity> getOrderEntities() {
        List<OrderEntity> list = orderRepository.findAll();
        if (list.isEmpty()) {
            log.error("list:: Nenhum pedido encontrado");
            throw new OrderNotFoundException(MessagesConstants.ERROR_NOT_FOUND_ORDER);
        }
        return list;
    }

}
