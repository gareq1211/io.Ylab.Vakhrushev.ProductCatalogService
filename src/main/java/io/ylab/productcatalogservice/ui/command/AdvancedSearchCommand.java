package io.ylab.productcatalogservice.ui.command;

import io.ylab.productcatalogservice.model.Product;
import io.ylab.productcatalogservice.service.ProductService;
import io.ylab.productcatalogservice.ui.UserInput;

import java.util.List;
import java.util.function.Predicate;

public class AdvancedSearchCommand implements Command {
    private final ProductService productService;
    private final UserInput input;

    public AdvancedSearchCommand(ProductService productService, UserInput input) {
        this.productService = productService;
        this.input = input;
    }

    @Override
    public void execute() {
        System.out.println("\n=== Расширенный поиск ===");
        String category = input.readLine("Категория (Enter — пропустить): ");
        double minPrice = input.readDouble("Мин. цена (Enter — пропустить): ");
        double maxPrice = input.readDouble("Макс. цена (Enter — пропустить): ");

        Predicate<Product> predicate = p -> true;

        if (!category.trim().isEmpty()) {
            predicate = predicate.and(p -> p.getCategory().equalsIgnoreCase(category));
        }
        if (minPrice > 0) {
            predicate = predicate.and(p -> p.getPrice() >= minPrice);
        }
        if (maxPrice > 0) {
            predicate = predicate.and(p -> p.getPrice() <= maxPrice);
        }

        List<Product> results = productService.search(predicate, "advanced search");
        if (results.isEmpty()) {
            System.out.println(" Ничего не найдено.");
        } else {
            results.forEach(System.out::println);
        }
    }
}