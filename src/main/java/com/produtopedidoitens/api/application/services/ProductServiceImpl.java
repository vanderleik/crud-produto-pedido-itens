package com.produtopedidoitens.api.application.services;

import com.produtopedidoitens.api.adapters.persistence.repositories.ProductRepository;
import com.produtopedidoitens.api.adapters.web.requests.ProductRequest;
import com.produtopedidoitens.api.adapters.web.responses.ProductResponse;
import com.produtopedidoitens.api.application.domain.entities.ProductEntity;
import com.produtopedidoitens.api.application.exceptions.BadRequestException;
import com.produtopedidoitens.api.application.exceptions.ProductNotFoundException;
import com.produtopedidoitens.api.application.mapper.ProductConverter;
import com.produtopedidoitens.api.application.port.ProductInputPort;
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
        List<ProductEntity> list = productRepository.findAll();
        if (list.isEmpty()) {
            log.error("list:: Ocorreu um erro ao listar os produtos/serviços: {}", MessagesConstants.ERROR_PRODUCT_NOT_FOUND);
            throw new ProductNotFoundException(MessagesConstants.ERROR_PRODUCT_NOT_FOUND);
        }
        log.info("list:: Produtos/serviços listados com sucesso: {}", list);
        return list.stream().map(productConverter::toResponse).toList();
    }

    @Override
    public ProductResponse read(UUID id) {
        return null;
    }

    @Override
    public ProductResponse update(UUID id, ProductRequest productRequest) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }

    private ProductEntity getEntity(ProductRequest productRequest) {
        return productConverter.toEntity(productRequest);
    }

}
