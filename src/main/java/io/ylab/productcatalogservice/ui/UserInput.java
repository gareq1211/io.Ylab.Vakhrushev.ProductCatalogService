package io.ylab.productcatalogservice.ui;

import java.util.Scanner;

public class UserInput {
    private final Scanner scanner = new Scanner(System.in);

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число.");
            }
        }
    }

    public long readLong(String prompt) {
        while (true) {
            try {
                return Long.parseLong(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Введите число.");
            }
        }
    }

    public double readDouble(String prompt) {
        String input = readLine(prompt);
        if (input.trim().isEmpty()) return 0.0; // 0 = "не задано"
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат. Будет 0.");
            return 0.0;
        }
    }
}