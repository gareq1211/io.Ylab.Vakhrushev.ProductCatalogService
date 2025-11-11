package io.ylab.productcatalogservice.repository;

import io.ylab.productcatalogservice.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    boolean delete(Long id);
}