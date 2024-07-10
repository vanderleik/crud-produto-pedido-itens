package com.produtopedidoitens.api.application.services;

import com.produtopedidoitens.api.adapters.persistence.repositories.OrderRepository;
import com.produtopedidoitens.api.adapters.web.requests.OrderRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderResponse;
import com.produtopedidoitens.api.application.mapper.OrderConverter;
import com.produtopedidoitens.api.application.port.OrderInputPort;
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

        return null;
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
