package io.ylab.productcatalogservice;

import io.ylab.productcatalogservice.repository.FileProductRepository;
import io.ylab.productcatalogservice.service.FileAuditService;
import io.ylab.productcatalogservice.service.MetricsService;
import io.ylab.productcatalogservice.service.ProductService;
import io.ylab.productcatalogservice.ui.ConsoleApp;

public class Main {
    public static void main(String[] args) {
        var productRepository = new FileProductRepository();
        var audit = new FileAuditService(); //
        var metrics = new MetricsService(productRepository);
        var productService = new ProductService(productRepository, audit);
        var app = new ConsoleApp(productService, audit, metrics);
        app.start();
    }
}