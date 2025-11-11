package io.ylab.productcatalogservice.ui;

import io.ylab.productcatalogservice.service.*;
import io.ylab.productcatalogservice.ui.command.*;

import java.util.HashMap;
import java.util.Map;

public class ConsoleApp {
    private final MenuRenderer renderer = new MenuRenderer();
    private final UserInput input = new UserInput();
    private final CommandHandler commandHandler;

    public ConsoleApp(ProductService productService, AuditService auditService, MetricsService metricsService) {
        // Создаём команды
        Map<Integer, Command> commands = new HashMap<>();
        commands.put(1, new AddProductCommand(productService, input));
        commands.put(2, new ListProductsCommand(productService));
        commands.put(3, new SearchCommand(productService, input, renderer));
        commands.put(4, new EditProductCommand(productService, input));
        commands.put(5, new DeleteProductCommand(productService, input));
        commands.put(6, new ShowAuditCommand(auditService));
        commands.put(7, new ShowMetricsCommand(metricsService));

        this.commandHandler = new CommandHandler(commands);
    }

    public void start() {
        // hook завершения
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nЗавершение работы...");
            System.out.println("Данные сохранены. До свидания!");
        }));

        System.out.println("=== Каталог товаров ===");
        while (true) {
            renderer.renderMainMenu();
            int choice = input.readInt("Выбор: ");
            if (commandHandler.isExitCommand(choice)) {
                System.out.println("Выход.");
                break;
            }
            commandHandler.handle(choice);
        }
    }
}