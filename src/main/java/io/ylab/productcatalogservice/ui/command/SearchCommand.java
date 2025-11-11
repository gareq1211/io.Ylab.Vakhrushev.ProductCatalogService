package io.ylab.productcatalogservice.ui.command;

import io.ylab.productcatalogservice.model.Product;
import io.ylab.productcatalogservice.service.ProductService;
import io.ylab.productcatalogservice.ui.MenuRenderer;
import io.ylab.productcatalogservice.ui.UserInput;

import java.util.List;

public class SearchCommand implements Command {
    private final ProductService productService;
    private final UserInput input;
    private final MenuRenderer renderer;

    public SearchCommand(ProductService productService, UserInput input, MenuRenderer renderer) {
        this.productService = productService;
        this.input = input;
        this.renderer = renderer;
    }

    @Override
    public void execute() {
        renderer.renderSearchMenu();
        int choice = input.readInt("Выбор: ");
        List<Product> results = switch (choice) {
            case 1 -> {
                String cat = input.readLine("Категория: ");
                yield productService.search(product -> product.getCategory().equalsIgnoreCase(cat), "category='" + cat + "'");
            }
            case 2 -> {
                String brand = input.readLine("Бренд: ");
                yield productService.search(product -> product.getBrand().equalsIgnoreCase(brand), "brand='" + brand + "'");
            }
            case 3 -> {
                double min = input.readDouble("Мин. цена: ");
                double max = input.readDouble("Макс. цена: ");
                yield productService.search(price -> price.getPrice() >= min && price.getPrice() <= max, "price in [" + min + "," + max + "]");
            }
            case 4 -> {
                String name = input.readLine("Часть названия: ");
                yield productService.search(product -> product.getName().toLowerCase().contains(name.toLowerCase()), "name contains '" + name + "'");
            }
            default -> {
                System.out.println("Отмена.");
                yield List.of();
            }
        };

        if (results.isEmpty()) {
            System.out.println("🔍 Ничего не найдено.");
        } else {
            results.forEach(System.out::println);
        }
    }
}