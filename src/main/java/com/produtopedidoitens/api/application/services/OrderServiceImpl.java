package com.produtopedidoitens.api.application.services;

import com.produtopedidoitens.api.adapters.persistence.repositories.OrderRepository;
import com.produtopedidoitens.api.adapters.web.requests.OrderRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderResponse;
import com.produtopedidoitens.api.application.domain.entities.OrderEntity;
import com.produtopedidoitens.api.application.exceptions.BadRequestException;
import com.produtopedidoitens.api.application.exceptions.OrderNotFoundException;
import com.produtopedidoitens.api.application.mapper.OrderConverter;
import com.produtopedidoitens.api.application.port.OrderInputPort;
import com.produtopedidoitens.api.utils.MessagesConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<OrderResponse> list() {
        log.info("list:: Listando pedidos");
        List<OrderEntity> list = getOrderEntities();
        log.info("list:: Pedidos encontrados: {}", list);
        return list.stream().map(orderConverter::toResponse).toList();
    }

    @Override
    public OrderResponse read(UUID id) {
        log.info("read:: Buscando pedido por id: {}", id);
        OrderEntity entity = getOrderEntity(id);
        log.info("read:: Pedido encontrado: {}", entity);
        return orderConverter.toResponse(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse update(UUID id, OrderRequest orderRequest) {
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(UUID id) {

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
