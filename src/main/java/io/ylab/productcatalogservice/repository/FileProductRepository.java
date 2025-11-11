// io.ylab.productcatalogservice.repository.FileProductRepository.java
package io.ylab.productcatalogservice.repository;

import io.ylab.productcatalogservice.model.Product;
import io.ylab.productcatalogservice.persistence.FilePersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class FileProductRepository implements ProductRepository {
    private final ConcurrentHashMap<Long, Product> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final FilePersistence persistence = new FilePersistence();

    public FileProductRepository() {
        ConcurrentHashMap<Long, Product> loaded = persistence.load();
        storage.putAll(loaded);
        // Восстанавливаем ID-генератор
        if (!loaded.isEmpty()) {
            long maxId = loaded.keySet().stream().mapToLong(Long::longValue).max().orElse(0);
            idGenerator.set(maxId + 1);
        }
    }

    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(idGenerator.getAndIncrement());
        }
        storage.put(product.getId(), product);
        persistence.save(storage);
        return product;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean delete(Long id) {
        boolean removed = storage.remove(id) != null;
        if (removed) persistence.save(storage);
        return removed;
    }
}