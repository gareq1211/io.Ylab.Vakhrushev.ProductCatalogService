package io.ylab.productcatalogservice.ui;

import io.ylab.productcatalogservice.ui.command.Command;

import java.util.Map;
import java.util.HashMap;

public class CommandHandler {
    private final Map<Integer, Command> commands = new HashMap<>();

    public CommandHandler(Map<Integer, Command> commandMap) {
        this.commands.putAll(commandMap);
    }

    public void handle(int choice) {
        Command command = commands.get(choice);
        if (command != null) {
            command.execute();
        } else {
            System.out.println("Неверный выбор.");
        }
    }

    public boolean isExitCommand(int choice) {
        return choice == 8;
    }
}