package com.produtopedidoitens.api.application.services;

import com.produtopedidoitens.api.adapters.persistence.repositories.CatalogItemRepository;
import com.produtopedidoitens.api.adapters.persistence.repositories.OrderItemRepository;
import com.produtopedidoitens.api.adapters.persistence.repositories.OrderRepository;
import com.produtopedidoitens.api.adapters.web.projections.OrderByOrderNumber;
import com.produtopedidoitens.api.adapters.web.projections.OrderItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderItemRequest;
import com.produtopedidoitens.api.adapters.web.requests.OrderItemUpdateRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderItemResponse;
import com.produtopedidoitens.api.application.domain.entities.CatalogItemEntity;
import com.produtopedidoitens.api.application.domain.entities.OrderEntity;
import com.produtopedidoitens.api.application.domain.entities.OrderItemEntity;
import com.produtopedidoitens.api.application.domain.enums.EnumCatalogItemType;
import com.produtopedidoitens.api.application.domain.enums.EnumOrderStatus;
import com.produtopedidoitens.api.application.exceptions.BadRequestException;
import com.produtopedidoitens.api.application.exceptions.OrderItemNotFoundException;
import com.produtopedidoitens.api.application.exceptions.OrderNotFoundException;
import com.produtopedidoitens.api.application.exceptions.ProductNotFoundException;
import com.produtopedidoitens.api.application.mapper.OrderItemConverter;
import com.produtopedidoitens.api.application.port.OrderItemInputPort;
import com.produtopedidoitens.api.application.validators.OrderItemValidator;
import com.produtopedidoitens.api.utils.MessagesConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderItemIServiceImpl implements OrderItemInputPort {
    
    private final OrderItemRepository orderItemRepository;
    private final CatalogItemRepository catalogItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemConverter orderItemConverter;
    private final OrderItemValidator orderItemValidator;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderItemResponse createOrderItem(OrderItemRequest orderItemRequest) {
        log.info("createOrderItem:: Recebendo orderItemRequest: {}", orderItemRequest);
        orderItemValidator.validate(orderItemRequest, getOrderEntity(orderItemRequest.orderId()));

        try {
            OrderItemEntity orderItemEntity = getOrderItemEntity(orderItemRequest);
            setGrossTotal(orderItemEntity);
            setNetTotal(orderItemRequest, orderItemEntity);

            OrderItemEntity entitySaved = orderItemRepository.save(orderItemEntity);
            OrderItemResponse response = orderItemConverter.toResponse(entitySaved);
            log.info("createOrderItem:: Salvando item do pedido: {}", response);
            return response;

        } catch (Exception e) {
         log.error("createOrderItem:: Ocorreu um erro ao salvar o item do pedido: {}", e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public Page<OrderItemProjection> listAllOrderItems(Pageable pageable) {
        log.info("listAllOrderItems:: Listando itens do pedido");
        List<OrderItemEntity> list = getOrderItemEntities();
        log.info("listAllOrderItems:: Itens do pedido encontrados");
        List<OrderItemProjection> orderItemProjectionList = list.stream().map(orderItemConverter::toProjection).toList();
        return new PageImpl<>(orderItemProjectionList, pageable, orderItemProjectionList.size());
    }

    @Override
    public OrderItemProjection getOrderItemById(UUID id) {
        log.info("getOrderItemById:: Buscando item do pedido por id: {}", id);
        OrderItemEntity entity = getOrderItemEntity(id);
        log.info("getOrderItemById:: Item do pedido encontrado");
        return orderItemConverter.toProjection(entity);
    }

    @Override
    public Page<OrderByOrderNumber> getOrdersByOrderNumber(String orderNumber) {
        log.info("getOrdersByOrderNumber:: Buscando itens do pedido pelo número do pedido: {}", orderNumber);
        List<OrderByOrderNumber> ordersByOrderNumber = orderItemRepository.findOrdersByOrderNumber(orderNumber);
        if (ordersByOrderNumber.isEmpty()) {
            log.error("getOrdersByOrderNumber:: Nenhum item do pedido encontrado pelo número do pedido: {}", orderNumber);
            throw new OrderItemNotFoundException(MessagesConstants.ERROR_ORDER_ITEM_NOT_FOUND_BY_ORDER_NUMBER);
        }
        log.info("getOrdersByOrderNumber:: Itens do pedido encontrados pelo número do pedido: {}", ordersByOrderNumber);
        return new PageImpl<>(ordersByOrderNumber, Pageable.unpaged(), ordersByOrderNumber.size());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderItemResponse updateOrderItem(UUID id, OrderItemUpdateRequest orderItemUpdateRequest) {
        log.info("updateOrderItem:: Recebendo requisição para atualizar item do pedido: {}", orderItemUpdateRequest);
        OrderItemEntity entity = getOrderItemEntity(id);
        updateEntity(entity, orderItemUpdateRequest);
        try {
            OrderItemEntity entitySaved = orderItemRepository.save(entity);
            OrderItemResponse response = orderItemConverter.toResponse(entitySaved);
            log.info("updateOrderItem:: Atualizando item do pedido: {}", response);
            return response;
        } catch (Exception e) {
            log.error("updateOrderItem:: Ocorreu um erro ao atualizar o item do pedido");
            throw new BadRequestException(MessagesConstants.ERROR_UPDATE_ORDER_ITEM);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteOrderItem(UUID id) {
        log.info("delete:: Recebendo requisição para deletar item do pedido por id: {}", id);
        OrderItemEntity entity = getOrderItemEntity(id);
        try {
            orderItemRepository.delete(entity);
            log.info("deleteOrderItem:: Deletando item do pedido");
        } catch (Exception e) {
            log.error("deleteOrderItem:: Ocorreu um erro ao deletar o item do pedido");
            throw new BadRequestException(MessagesConstants.ERROR_DELETE_ORDER_ITEM);
        }
    }

    private static void setGrossTotal(OrderItemEntity orderItemEntity) {
        log.info("setGrossTotal:: Calculando total bruto do item adicionado ao pedido");
        BigDecimal grossTotal = orderItemEntity.getOrder().getGrossTotal();
        if (grossTotal == null) {
            grossTotal = BigDecimal.ZERO;
        }
        BigDecimal price = orderItemEntity.getCatalogItem().getPrice();
        Integer quantity = orderItemEntity.getQuantity();
        BigDecimal result = price.multiply(BigDecimal.valueOf(quantity));
        log.info("setGrossTotal:: Total bruto do item adicionado ao pedido: {}", result);
        orderItemEntity.getOrder().setGrossTotal(grossTotal.add(result));
    }

    private void setNetTotal(OrderItemRequest orderItemRequest, OrderItemEntity orderItemEntity) {
        log.info("createOrderItem:: Total bruto do pedido calculado: {}", orderItemEntity.getOrder().getGrossTotal());
        OrderEntity orderEntity = getOrderEntity(orderItemRequest.orderId());
        log.info("createOrderItem:: Total bruto do pedido calculado: {}", orderEntity.getGrossTotal());

        CatalogItemEntity catalogItemEntity = getProdutoEntity(orderItemRequest.catalogItemId());

        if (EnumOrderStatus.OPEN.equals(orderEntity.getStatus())) {
            if (EnumCatalogItemType.PRODUCT.equals(catalogItemEntity.getType())) {
                BigDecimal discountPercent = orderEntity.getDiscount().divide(BigDecimal.valueOf(100.00), 4, RoundingMode.HALF_EVEN);
                BigDecimal price = catalogItemEntity.getPrice();
                Integer quantity = orderItemEntity.getQuantity();
                BigDecimal grossTotalItem = price.multiply(BigDecimal.valueOf(quantity).setScale(2, RoundingMode.HALF_EVEN));

                BigDecimal discountAmount = grossTotalItem.multiply(discountPercent).setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal netTotalItem = grossTotalItem.subtract(discountAmount).setScale(2, RoundingMode.HALF_EVEN);
                log.info("createOrderItem:: Percentual de desconto: {}", discountPercent);
                log.info("createOrderItem:: Preço: {}", price);
                log.info("createOrderItem:: Quantidade: {}", quantity);
                log.info("createOrderItem:: Valor Total Bruto: {}", grossTotalItem);
                log.info("createOrderItem:: Desconto do item: {}", discountAmount);
                log.info("createOrderItem:: Total líquido do item calculado: {}", netTotalItem);
                BigDecimal netTotal = orderEntity.getNetTotal();
                log.info("createOrderItem:: Total líquido já adicionao ao pedido calculado: {}", netTotal);
                if (netTotal == null) {
                    netTotal = BigDecimal.ZERO;
                }
                orderEntity.setNetTotal(netTotal.add(netTotalItem));
            }
        }

        if (EnumOrderStatus.OPEN.equals(orderEntity.getStatus())) {
            if (EnumCatalogItemType.SERVICE.equals(catalogItemEntity.getType())) {
                BigDecimal price = catalogItemEntity.getPrice();
                Integer quantity = orderItemEntity.getQuantity();
                BigDecimal grossTotalItem = price.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_EVEN);

                BigDecimal netTotalItem = grossTotalItem;
                log.info("createOrderItem:: Preço: {}", price);
                log.info("createOrderItem:: Quantidade: {}", quantity);
                log.info("createOrderItem:: Valor Total Bruto: {}", grossTotalItem);
                log.info("createOrderItem:: Total líquido do item calculado: {}", netTotalItem);
                BigDecimal netTotal = orderEntity.getNetTotal();
                log.info("createOrderItem:: Total líquido já adicionao ao pedido calculado: {}", netTotal);
                orderEntity.setNetTotal(netTotal.add(netTotalItem));
                netTotal = orderEntity.getNetTotal();
                log.info("createOrderItem:: Total líquido adicionando os serviços: {}", netTotal);
            }
        }
    }

    private void updateEntity(OrderItemEntity entity, OrderItemUpdateRequest orderItemUpdateRequest) {
        entity.setQuantity(orderItemUpdateRequest.quantity() == null ? entity.getQuantity() : Integer.parseInt(orderItemUpdateRequest.quantity()));
    }

    private CatalogItemEntity getProdutoEntity(String catalogItemId) {
        return catalogItemRepository.findById(UUID.fromString(catalogItemId)).orElseThrow(() -> {
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
        CatalogItemEntity catalogItemEntity = getProdutoEntity(orderItemRequest.catalogItemId());
        if (Boolean.FALSE.equals(catalogItemEntity.getIsActive())) {
            log.error("getOrderItemEntity:: O produto/serviço não está ativo: {}", MessagesConstants.ERROR_PRODUCT_NOT_ACTIVE);
            throw new BadRequestException(MessagesConstants.ERROR_PRODUCT_NOT_ACTIVE);
        }
        OrderEntity orderEntity = getOrderEntity(orderItemRequest.orderId());
        return orderItemConverter.requestToEntity(orderItemRequest, catalogItemEntity, orderEntity);
    }

    private OrderEntity getOrderEntity(String orderId) {
        if (orderId == null || orderId.isEmpty()) {
            log.error("getOrderEntity:: O id do pedido não pode ser nulo ou vazio: {}", MessagesConstants.ERROR_ORDER_ID_NOT_NULL);
            throw new OrderNotFoundException(MessagesConstants.ERROR_ORDER_ID_NOT_NULL);
        }
        return orderRepository.findById(UUID.fromString(orderId)).orElseThrow(() -> {
            log.error("getOrderEntity:: Ocorreu um erro ao buscar o pedido por id: {}", MessagesConstants.ERROR_NOT_FOUND_ORDER);
            return new OrderNotFoundException(MessagesConstants.ERROR_NOT_FOUND_ORDER);
        });
    }

}
