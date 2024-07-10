package com.produtopedidoitens.api.application.services;

import com.produtopedidoitens.api.adapters.persistence.repositories.ProductRepository;
import com.produtopedidoitens.api.adapters.web.requests.ProductRequest;
import com.produtopedidoitens.api.adapters.web.responses.ProductResponse;
import com.produtopedidoitens.api.application.domain.entities.ProductEntity;
import com.produtopedidoitens.api.application.domain.enums.EnumProductType;
import com.produtopedidoitens.api.application.exceptions.BadRequestException;
import com.produtopedidoitens.api.application.exceptions.ProductNotFoundException;
import com.produtopedidoitens.api.application.mapper.EnumConverter;
import com.produtopedidoitens.api.application.mapper.ProductConverter;
import com.produtopedidoitens.api.application.port.ProductInputPort;
import com.produtopedidoitens.api.utils.MessagesConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductInputPort {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductResponse create(ProductRequest productRequest) {
        log.info("create:: Recebendo productRequest: {}", productRequest);
        try {
            ProductEntity entitySaved = productRepository.save(getEntity(productRequest));
            ProductResponse response = productConverter.toResponse(entitySaved);
            log.info("create:: Salvando produto/serviço: {}", response);
            return response;
        } catch (Exception e) {
            log.error("create:: Ocorreu um erro ao salvar o produto/serviço");
            throw new BadRequestException(MessagesConstants.ERROR_SAVE_PRODUCT);
        }
    }

    @Override
    public List<ProductResponse> list() {
        log.info("list:: Listando produtos/serviços");
        List<ProductEntity> list = getProductEntities();
        log.info("list:: Produtos/serviços listados com sucesso: {}", list);
        return list.stream().map(productConverter::toResponse).toList();
    }

    @Override
    public ProductResponse read(UUID id) {
        log.info("read:: Buscando produto/serviço pelo id: {}", id);
        ProductEntity entity = getProductEntity(id);
        log.info("read:: Produto/serviço encontrado: {}", entity);
        return productConverter.toResponse(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductResponse update(UUID id, ProductRequest productRequest) {
        log.info("update:: Recebendo requisição para atualizar produto/serviço: {}", productRequest);
        ProductEntity entity = getProductEntity(id);
        updateEntity(productRequest, entity);
        try {
            ProductEntity entitySaved = productRepository.save(entity);
            ProductResponse response = productConverter.toResponse(entitySaved);
            log.info("update:: Atualizando produto/serviço na base: {}", response);
            return response;
        } catch (Exception e) {
            log.error("update:: Ocorreu um erro ao atualizar o produto/serviço");
            throw new BadRequestException(MessagesConstants.ERROR_UPDATE_PRODUCT);
        }
    }


    @Override
    public void delete(UUID id) {

    }

    private static void updateEntity(ProductRequest productRequest, ProductEntity entity) {
        entity.setProductName(productRequest.productName() == null ? entity.getProductName() : productRequest.productName());
        entity.setPrice(productRequest.price() == null ? entity.getPrice() : new BigDecimal(productRequest.price()));
        entity.setType(productRequest.type() == null ? entity.getType() : EnumConverter.fromString(productRequest.type(), EnumProductType.class));
        entity.setActive(productRequest.active() == null ? entity.getActive() : Boolean.parseBoolean(productRequest.active()));
    }

    private ProductEntity getProductEntity(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("read:: Ocorreu um erro ao buscar o produto/serviço pelo id: {}", MessagesConstants.ERROR_PRODUCT_NOT_FOUND);
                    throw new ProductNotFoundException(MessagesConstants.ERROR_PRODUCT_NOT_FOUND);
                });
    }

    private ProductEntity getEntity(ProductRequest productRequest) {
        return productConverter.toEntity(productRequest);
    }

    private List<ProductEntity> getProductEntities() {
        List<ProductEntity> list = productRepository.findAll();
        if (list.isEmpty()) {
            log.error("list:: Ocorreu um erro ao listar os produtos/serviços: {}", MessagesConstants.ERROR_PRODUCT_NOT_FOUND);
            throw new ProductNotFoundException(MessagesConstants.ERROR_PRODUCT_NOT_FOUND);
        }
        return list;
    }

}
