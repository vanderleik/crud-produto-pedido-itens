package com.produtopedidoitens.api.application.services;

import com.produtopedidoitens.api.adapters.persistence.repositories.OrderRepository;
import com.produtopedidoitens.api.adapters.web.requests.OrderRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderResponse;
import com.produtopedidoitens.api.application.domain.entities.OrderEntity;
import com.produtopedidoitens.api.application.exceptions.BadRequestException;
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

    private OrderEntity getEntity(OrderRequest orderRequest) {
        return orderConverter.toEntity(orderRequest);
    }

    @Override
    public List<OrderResponse> list() {
        return List.of();
    }

    @Override
    public OrderResponse read(UUID id) {
        return null;
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
}
