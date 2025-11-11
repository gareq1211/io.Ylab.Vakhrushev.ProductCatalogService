package io.ylab.productcatalogservice.ui;

public class MenuRenderer {
    public void renderMainMenu() {
        System.out.println("\n=== Каталог товаров ===");
        System.out.println("1. Добавить товар");
        System.out.println("2. Показать все");
        System.out.println("3. Поиск");
        System.out.println("4. Редактировать");
        System.out.println("5. Удалить");
        System.out.println("6. Аудит");
        System.out.println("7. Метрики");
        System.out.println("8. Выход");
    }

    public void renderSearchMenu() {
        System.out.println("\nПоиск по:");
        System.out.println("1. Категории");
        System.out.println("2. Бренду");
        System.out.println("3. Цене");
        System.out.println("4. Названию");
    }
}