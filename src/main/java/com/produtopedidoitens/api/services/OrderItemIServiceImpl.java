package com.produtopedidoitens.api.services;

import com.produtopedidoitens.api.adapters.persistence.repositories.OrderItemRepository;
import com.produtopedidoitens.api.adapters.persistence.repositories.OrderRepository;
import com.produtopedidoitens.api.adapters.persistence.repositories.ProductRepository;
import com.produtopedidoitens.api.adapters.web.projections.OrderByOrderNumber;
import com.produtopedidoitens.api.adapters.web.projections.OrderItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderItemRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderItemResponse;
import com.produtopedidoitens.api.domain.entities.OrderEntity;
import com.produtopedidoitens.api.domain.entities.OrderItemEntity;
import com.produtopedidoitens.api.domain.entities.ProductEntity;
import com.produtopedidoitens.api.domain.enums.EnumOrderStatus;
import com.produtopedidoitens.api.domain.enums.EnumProductType;
import com.produtopedidoitens.api.application.exceptions.BadRequestException;
import com.produtopedidoitens.api.application.exceptions.OrderItemNotFoundException;
import com.produtopedidoitens.api.application.exceptions.OrderNotFoundException;
import com.produtopedidoitens.api.application.exceptions.ProductNotFoundException;
import com.produtopedidoitens.api.application.mapper.OrderItemConverter;
import com.produtopedidoitens.api.application.port.OrderItemInputPort;
import com.produtopedidoitens.api.utils.MessagesConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
            OrderItemEntity orderItemEntity = getOrderItemEntity(orderItemRequest);
            setGrossTotal(orderItemEntity);
            setNetTotal(orderItemRequest, orderItemEntity);

            OrderItemEntity entitySaved = orderItemRepository.save(orderItemEntity);
            OrderItemResponse response = orderItemConverter.toResponse(entitySaved);
            log.info("create:: Salvando item do pedido: {}", response);
            return response;

        } catch (Exception e) {
         log.error("create:: Ocorreu um erro ao salvar o item do pedido");
            throw new BadRequestException(MessagesConstants.ERROR_SAVE_ORDER_ITEM);
        }
    }

    @Override
    public Page<OrderItemProjection> list(Pageable pageable) {
        log.info("list:: Listando itens do pedido");
        List<OrderItemEntity> list = getOrderItemEntities();
        log.info("list:: Itens do pedido encontrados: {}", list);
        List<OrderItemProjection> orderItemProjectionList = list.stream().map(orderItemConverter::toProjection).toList();
        return new PageImpl<>(orderItemProjectionList, pageable, orderItemProjectionList.size());
    }

    @Override
    public OrderItemProjection read(UUID id) {
        log.info("read:: Buscando item do pedido por id: {}", id);
        OrderItemEntity entity = getOrderItemEntity(id);
        log.info("read:: Item do pedido encontrado: {}", entity);
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

    private static void setGrossTotal(OrderItemEntity orderItemEntity) {
        log.info("setGrossTotal:: Calculando total bruto do item adicionado ao pedido");
        BigDecimal grossTotal = orderItemEntity.getOrder().getGrossTotal();
        if (grossTotal == null) {
            grossTotal = BigDecimal.ZERO;
        }
        BigDecimal price = orderItemEntity.getProduct().getPrice();
        Integer quantity = orderItemEntity.getQuantity();
        BigDecimal result = price.multiply(BigDecimal.valueOf(quantity));
        log.info("setGrossTotal:: Total bruto do item adicionado ao pedido: {}", result);
        orderItemEntity.getOrder().setGrossTotal(grossTotal.add(result));
    }

    private void setNetTotal(OrderItemRequest orderItemRequest, OrderItemEntity orderItemEntity) {
        log.info("create:: Total bruto do pedido calculado: {}", orderItemEntity.getOrder().getGrossTotal());
        OrderEntity orderEntity = getOrderEntity(orderItemRequest.orderId());
        log.info("create:: Total bruto do pedido calculado: {}", orderEntity.getGrossTotal());

        ProductEntity produtoEntity = getProdutoEntity(orderItemRequest.productId());

        if (EnumOrderStatus.OPEN.equals(orderEntity.getStatus())) {
            if (EnumProductType.PRODUCT.equals(produtoEntity.getType())) {
                BigDecimal discountPercent = orderEntity.getDiscount().divide(BigDecimal.valueOf(100)).setScale(4, BigDecimal.ROUND_HALF_EVEN);
                BigDecimal price = produtoEntity.getPrice();
                Integer quantity = orderItemEntity.getQuantity();
                BigDecimal grossTotalItem = price.multiply(BigDecimal.valueOf(quantity)).setScale(2, BigDecimal.ROUND_HALF_EVEN);

                BigDecimal discountAmount = grossTotalItem.multiply(discountPercent).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                BigDecimal netTotalItem = grossTotalItem.subtract(discountAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                log.info("create:: Percentual de desconto: {}", discountPercent);
                log.info("create:: Preço: {}", price);
                log.info("create:: Quantidade: {}", quantity);
                log.info("create:: Valor Total Bruto: {}", grossTotalItem);
                log.info("create:: Desconto do item: {}", discountAmount);
                log.info("create:: Total líquido do item calculado: {}", netTotalItem);
                BigDecimal netTotal = orderEntity.getNetTotal();
                log.info("create:: Total líquido já adicionao ao pedido calculado: {}", netTotal);
                if (netTotal == null) {
                    netTotal = BigDecimal.ZERO;
                }
                orderEntity.setNetTotal(netTotal.add(netTotalItem));
            }
        }

        if (EnumOrderStatus.OPEN.equals(orderEntity.getStatus())) {
            if (EnumProductType.SERVICE.equals(produtoEntity.getType())) {
                BigDecimal price = produtoEntity.getPrice();
                Integer quantity = orderItemEntity.getQuantity();
                BigDecimal grossTotalItem = price.multiply(BigDecimal.valueOf(quantity)).setScale(2, BigDecimal.ROUND_HALF_EVEN);

                BigDecimal netTotalItem = grossTotalItem;
                log.info("create:: Preço: {}", price);
                log.info("create:: Quantidade: {}", quantity);
                log.info("create:: Valor Total Bruto: {}", grossTotalItem);
                log.info("create:: Total líquido do item calculado: {}", netTotalItem);
                BigDecimal netTotal = orderEntity.getNetTotal();
                log.info("create:: Total líquido já adicionao ao pedido calculado: {}", netTotal);
                orderEntity.setNetTotal(netTotal.add(netTotalItem));
                netTotal = orderEntity.getNetTotal();
                log.info("create:: Total líquido adicionando os serviços: {}", netTotal);
            }
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
        if (Boolean.FALSE.equals(productEntity.getActive())) {
            log.error("getOrderItemEntity:: O produto/serviço não está ativo: {}", MessagesConstants.ERROR_PRODUCT_NOT_ACTIVE);
            throw new BadRequestException(MessagesConstants.ERROR_PRODUCT_NOT_ACTIVE);
        }
        OrderEntity orderEntity = getOrderEntity(orderItemRequest.orderId());
        return orderItemConverter.requestToEntity(orderItemRequest, productEntity, orderEntity);
    }

    private OrderEntity getOrderEntity(String orderId) {
        return orderRepository.findById(UUID.fromString(orderId)).orElseThrow(() -> {
            log.error("getOrderEntity:: Ocorreu um erro ao buscar o pedido por id: {}", MessagesConstants.ERROR_NOT_FOUND_ORDER);
            return new OrderNotFoundException(MessagesConstants.ERROR_NOT_FOUND_ORDER);
        });
    }

}
