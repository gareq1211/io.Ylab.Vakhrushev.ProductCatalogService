package io.ylab.productcatalogservice.ui.command;

import io.ylab.productcatalogservice.service.ProductService;

public class ListProductsCommand implements Command {
    private final ProductService service;
    public ListProductsCommand(ProductService service) { this.service = service; }
    public void execute() {
        var products = service.findAll();
        if (products.isEmpty()) System.out.println("Каталог пуст.");
        else products.forEach(System.out::println);
    }
}