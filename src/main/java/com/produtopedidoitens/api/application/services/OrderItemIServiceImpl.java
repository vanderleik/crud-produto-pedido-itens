package com.produtopedidoitens.api.application.services;

import com.produtopedidoitens.api.adapters.persistence.repositories.OrderItemRepository;
import com.produtopedidoitens.api.adapters.persistence.repositories.OrderRepository;
import com.produtopedidoitens.api.adapters.persistence.repositories.ProductRepository;
import com.produtopedidoitens.api.adapters.web.projections.OrderItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderItemRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderItemResponse;
import com.produtopedidoitens.api.application.domain.entities.OrderEntity;
import com.produtopedidoitens.api.application.domain.entities.OrderItemEntity;
import com.produtopedidoitens.api.application.domain.entities.ProductEntity;
import com.produtopedidoitens.api.application.exceptions.BadRequestException;
import com.produtopedidoitens.api.application.exceptions.OrderItemNotFoundException;
import com.produtopedidoitens.api.application.exceptions.OrderNotFoundException;
import com.produtopedidoitens.api.application.exceptions.ProductNotFoundException;
import com.produtopedidoitens.api.application.mapper.OrderItemConverter;
import com.produtopedidoitens.api.application.port.OrderItemInputPort;
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
public class OrderItemIServiceImpl implements OrderItemInputPort {
    
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemConverter orderItemConverter;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderItemResponse create(OrderItemRequest orderItemRequest) {
        log.info("create:: Recebendo orderItemRequest: {}", orderItemRequest);
        try {
            OrderItemEntity entitySaved = orderItemRepository.save(getOrderItemEntity(orderItemRequest));
            OrderItemResponse response = orderItemConverter.toResponse(entitySaved);
            log.info("create:: Salvando item do pedido: {}", response);
            return response;

        } catch (Exception e) {
         log.error("create:: Ocorreu um erro ao salvar o item do pedido");
            throw new BadRequestException(MessagesConstants.ERROR_SAVE_ORDER_ITEM);
        }
    }

    @Override
    public List<OrderItemProjection> list() {
        log.info("list:: Listando itens do pedido");
        List<OrderItemEntity> list = getOrderItemEntities();
        log.info("list:: Itens do pedido encontrados: {}", list);
        return list.stream().map(orderItemConverter::toProjection).toList();
    }

    @Override
    public OrderItemProjection read(UUID id) {
        log.info("read:: Buscando item do pedido por id: {}", id);
        OrderItemEntity entity = getOrderItemEntity(id);
        log.info("read:: Item do pedido encontrado: {}", entity);
        return orderItemConverter.toProjection(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderItemResponse update(UUID id, OrderItemRequest orderItemRequest) {
        log.info("update:: Recebendo requisição para atualizar item do pedido: {}", orderItemRequest);
        OrderItemEntity entity = getOrderItemEntity(id);
        updateEntity(entity, orderItemRequest);
        try {
            OrderItemEntity entitySaved = orderItemRepository.save(entity);
            OrderItemResponse response = orderItemConverter.toResponse(entitySaved);
            log.info("update:: Atualizando item do pedido: {}", response);
            return response;
        } catch (Exception e) {
            log.error("update:: Ocorreu um erro ao atualizar o item do pedido");
            throw new BadRequestException(MessagesConstants.ERROR_UPDATE_ORDER_ITEM);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(UUID id) {
        log.info("delete:: Recebendo requisição para deletar item do pedido por id: {}", id);
        OrderItemEntity entity = getOrderItemEntity(id);
        try {
            orderItemRepository.delete(entity);
            log.info("delete:: Deletando item do pedido: {}", entity);
        } catch (Exception e) {
            log.error("delete:: Ocorreu um erro ao deletar o item do pedido");
            throw new BadRequestException(MessagesConstants.ERROR_DELETE_ORDER_ITEM);
        }
    }

    private void updateEntity(OrderItemEntity entity, OrderItemRequest orderItemRequest) {
        entity.setQuantity(orderItemRequest.quantity() == null ? entity.getQuantity() : orderItemRequest.quantity());
        entity.setProduct(orderItemRequest.productId() == null ? entity.getProduct() : getProdutoEntity(orderItemRequest.productId()));
    }

    private ProductEntity getProdutoEntity(String productId) {
        return productRepository.findById(UUID.fromString(productId)).orElseThrow(() -> {
            log.error("getProdutoEntity:: Ocorreu um erro ao buscar o produto por id: {}", MessagesConstants.ERROR_PRODUCT_NOT_FOUND);
            return new ProductNotFoundException(MessagesConstants.ERROR_PRODUCT_NOT_FOUND);
        });
    }

    private OrderItemEntity getOrderItemEntity(UUID id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("getOrderItemEntity:: Ocorreu um erro ao buscar o item do pedido por id: {}", MessagesConstants.ERROR_ORDER_ITEM_NOT_FOUND);
                    return new OrderItemNotFoundException(MessagesConstants.ERROR_ORDER_ITEM_NOT_FOUND);
                });
    }

    private List<OrderItemEntity> getOrderItemEntities() {
        List<OrderItemEntity> list = orderItemRepository.findAll();
        if (list.isEmpty()) {
            log.error("getOrderItemEntities:: Nenhum item do pedido encontrado");
            throw new OrderItemNotFoundException(MessagesConstants.ERROR_ORDER_ITEM_NOT_FOUND);
        }
        return list;
    }

    private OrderItemEntity getOrderItemEntity(OrderItemRequest orderItemRequest) {
        ProductEntity productEntity = getProdutoEntity(orderItemRequest.productId());
        OrderEntity orderEntity = getOrderEntity(orderItemRequest.orderId());//nulo?
        return orderItemConverter.requestToEntity(orderItemRequest, productEntity, orderEntity);
    }

    private OrderEntity getOrderEntity(String orderId) {
        return orderRepository.findById(UUID.fromString(orderId)).orElseThrow(() -> {
            log.error("getOrderEntity:: Ocorreu um erro ao buscar o pedido por id: {}", MessagesConstants.ERROR_NOT_FOUND_ORDER);
            return new OrderNotFoundException(MessagesConstants.ERROR_NOT_FOUND_ORDER);
        });
    }

}
