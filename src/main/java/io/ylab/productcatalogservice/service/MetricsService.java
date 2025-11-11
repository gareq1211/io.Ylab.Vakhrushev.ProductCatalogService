// io.ylab.productcatalogservice.service.MetricsService.java
package io.ylab.productcatalogservice.service;

import io.ylab.productcatalogservice.repository.ProductRepository;

import java.util.concurrent.atomic.AtomicLong;

public class MetricsService {
    private final ProductRepository repository;
    private final AtomicLong totalRequestTimeNs = new AtomicLong(0);
    private final AtomicLong requestCount = new AtomicLong(0);

    public MetricsService(ProductRepository repository) {
        this.repository = repository;
    }

    public int getTotalProducts() {
        return repository.findAll().size();
    }

    public long getAverageResponseTime() {
        long count = requestCount.get();
        if (count == 0) return 0;
        return totalRequestTimeNs.get() / count / 1_000_000; // нс → мс
    }

    // Для измерения — вызывать в сервисах
    public <T> T measure(String operation, java.util.concurrent.Callable<T> action) throws Exception {
        long start = System.nanoTime();
        try {
            T result = action.call();
            return result;
        } finally {
            long duration = System.nanoTime() - start;
            totalRequestTimeNs.addAndGet(duration);
            requestCount.incrementAndGet();
        }
    }
}