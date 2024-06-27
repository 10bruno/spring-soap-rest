package br.com.jesus.soap.rest.endpoint;

import br.com.jesus.soap.rest.converter.ProductConverter;
import br.com.jesus.soap.rest.model.ProductModel;
import br.com.jesus.soap.rest.repository.ProductRepository;
import br.com.jesus.soap.rest.schema.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
@Slf4j
public class ProductEndpoint {

    private static final String NAMESPACE_URI = "http://www.example.com/demosoap/gen";
    private final ProductRepository productRepository;
    private final ProductConverter productConverter;

    @Autowired
    public ProductEndpoint(ProductRepository productRepository, ProductConverter productConverter) {
        this.productRepository = productRepository;
        this.productConverter = productConverter;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getProductRequest")
    @ResponsePayload
    public GetProductResponse getProduct(@RequestPayload GetProductRequest request) {
        GetProductResponse response = new GetProductResponse();
        ProductModel productModel = productRepository.findByName(request.getName());
        response.setProduct(productConverter.convertProductModelToProduct(productModel));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getProductsRequest")
    @ResponsePayload
    public GetProductsResponse getProducts(@RequestPayload GetProductsRequest request) {
        GetProductsResponse response = new GetProductsResponse();
        List<ProductModel> productModels = productRepository.findAll();
        List<Product> products = productConverter.convertProductModelsToProducts(productModels);
        response.getProducts().addAll(products);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "postProductRequest")
    @ResponsePayload
    public PostProductResponse postProducts(@RequestPayload PostProductRequest request) {
        PostProductResponse response = new PostProductResponse();
        ProductModel productModel = productConverter.convertProductToProductModel(request.getProduct());
        Product product = productConverter.convertProductModelToProduct(productRepository.save(productModel));
        response.setProduct(product);
        return response;
    }
}
