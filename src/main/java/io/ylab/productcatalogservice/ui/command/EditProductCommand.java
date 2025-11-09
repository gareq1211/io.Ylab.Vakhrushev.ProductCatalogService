package io.ylab.productcatalogservice.ui.command;

import io.ylab.productcatalogservice.model.Product;
import io.ylab.productcatalogservice.service.ProductService;
import io.ylab.productcatalogservice.ui.UserInput;

import java.util.Optional;

public class EditProductCommand implements Command {
    private final ProductService productService;
    private final UserInput input;

    public EditProductCommand(ProductService productService, UserInput input) {
        this.productService = productService;
        this.input = input;
    }

    @Override
    public void execute() {
        long id = input.readLong("ID товара: ");
        Optional<Product> productServiceById = productService.findById(id);
        if (productServiceById.isEmpty()) {
            System.out.println("Товар не найден.");
            return;
        }

        Product current = productServiceById.get();
        System.out.println("Текущий: " + current);

        String name = readOrKeep("Новое название", current.getName());
        String category = readOrKeep("Новая категория", current.getCategory());
        String brand = readOrKeep("Новый бренд", current.getBrand());
        double price = readPriceOrKeep(current.getPrice());
        String description = readOrKeep("Новое описание", current.getDescription());

        Product updated = productService.update(id, name, category, brand, price, description);
        if (updated != null) {
            System.out.println("Обновлено: " + updated);
        } else {
            System.out.println("Ошибка обновления.");
        }
    }

    private String readOrKeep(String prompt, String current) {
        String fullPrompt = String.format("%s (текущее: '%s', Enter — оставить): ", prompt, current);
        String inputStr = input.readLine(fullPrompt);
        return inputStr.trim().isEmpty() ? current : inputStr;
    }

    private double readPriceOrKeep(double current) {
        String fullPrompt = String.format("Новая цена (текущая: %.2f, Enter — оставить): ", current);
        String string = input.readLine(fullPrompt);
        if (string.trim().isEmpty()) return current;
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат. Оставлена старая цена.");
            return current;
        }
    }
}