package io.ylab.productcatalogservice.ui.command;

import io.ylab.productcatalogservice.service.ProductService;
import io.ylab.productcatalogservice.ui.UserInput;

public class DeleteProductCommand implements Command {
    private final ProductService service;
    private final UserInput input;
    public DeleteProductCommand(ProductService service, UserInput input) {
        this.service = service; this.input = input;
    }
    public void execute() {
        long id = input.readLong("ID: ");
        if (service.delete(id)) System.out.println("Удалён.");
        else System.out.println("Не найден.");
    }
}