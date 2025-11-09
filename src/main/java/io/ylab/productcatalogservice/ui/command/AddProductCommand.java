package io.ylab.productcatalogservice.ui.command;

import io.ylab.productcatalogservice.exception.ValidationException;
import io.ylab.productcatalogservice.model.Product;
import io.ylab.productcatalogservice.service.ProductService;
import io.ylab.productcatalogservice.ui.UserInput;

public class AddProductCommand implements Command {
    private final ProductService productService;
    private final UserInput input;

    public AddProductCommand(ProductService productService, UserInput input) {
        this.productService = productService;
        this.input = input;
    }

    @Override
    public void execute() {
        try {
            String name = input.readLine("Название: ");
            String category = input.readLine("Категория: ");
            String brand = input.readLine("Бренд: ");
            double price = input.readDouble("Цена: ");
            String description = input.readLine("Описание: ");
            Product add = productService.add(name, category, brand, price, description);
            System.out.println(" Добавлен: " + add);
        } catch (ValidationException e) {
            System.out.println("Ошибка валидации: " + e.getMessage());
        }
    }
}