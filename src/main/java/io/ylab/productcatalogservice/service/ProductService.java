package io.ylab.productcatalogservice.service;

import io.ylab.productcatalogservice.exception.ValidationException;
import io.ylab.productcatalogservice.model.Product;
import io.ylab.productcatalogservice.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProductService {
    private final ProductRepository repository;
    private final AuditService auditService;

    public ProductService(ProductRepository repository, AuditService auditService) {
        this.repository = repository;
        this.auditService = auditService;
    }

    public Product add(String name, String category, String brand, double price, String description) {
        validateProduct(name, price);

        // Проверка дубликата
        boolean exists = repository.findAll().stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(name));
        if (exists) {
            throw new ValidationException("Товар с названием '" + name + "' уже существует");
        }

        // Получаем текущее количество
        int currentCount = repository.findAll().size();
        int newCount = currentCount + 1;

        Product p = new Product(null, name, category, brand, price, description);
        Product saved = repository.save(p);

        // Логируем с контекстом
        auditService.log("ADD", String.format("Product: %s (всего товаров: %d)", name, newCount));
        return saved;
    }
    private void validateProduct(String name, double price) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Название товара не может быть пустым");
        }
        if (name.length() > 100) {
            throw new ValidationException("Название не должно превышать 100 символов");
        }
        if (price <= 0) {
            throw new ValidationException("Цена должна быть больше 0");
        }
    }

    public boolean delete(Long id) {
        // Проверяем, существует ли товар
        if (repository.findById(id).isEmpty()) {
            return false;
        }

        int currentCount = repository.findAll().size();
        boolean result = repository.delete(id);

        if (result) {
            int newCount = currentCount - 1;
            auditService.log("DELETE", String.format("ID: %d (всего товаров: %d)", id, newCount));
        }
        return result;
    }

    public Product update(Long id, String name, String category, String brand, double price, String description) {
        return repository.findById(id)
                .map(product -> {
                    product.setName(name);
                    product.setCategory(category);
                    product.setBrand(brand);
                    product.setPrice(price);
                    product.setDescription(description);
                    Product updated = repository.save(product);

                    int totalCount = repository.findAll().size();
                    auditService.log("UPDATE", String.format("ID: %d (всего товаров: %d)", id, totalCount));
                    return updated;
                })
                .orElse(null);
    }

    public List<Product> findAll() {
        auditService.log("LIST", "All products");
        return repository.findAll();
    }

    public List<Product> search(Predicate<Product> filter, String description) {
        List<Product> results = repository.findAll().stream()
                .filter(filter)
                .collect(Collectors.toList());
        auditService.log("SEARCH", description + " → " + results.size() + " results");
        return results;
    }
    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }
}