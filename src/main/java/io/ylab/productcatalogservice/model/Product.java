package io.ylab.productcatalogservice.model;

public class Product {
    private Long id;
    private String name;
    private String category;
    private String brand;
    private double price;
    private String description;

    // Конструкторы, геттеры, сеттеры
    public Product() {}

    public Product(Long id, String name, String category, String brand, double price, String description) {
        // Валидация имени
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название товара не может быть пустым.");
        }
        this.name = name.trim();

        // Валидация категории
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Категория не может быть пустой.");
        }
        this.category = category.trim();

        // Валидация бренда
        if (brand == null || brand.trim().isEmpty()) {
            throw new IllegalArgumentException("Бренд не может быть пустым.");
        }
        this.brand = brand.trim();

        // Валидация цены
        if (price < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной.");
        }
        this.price = price;

        // Описание может быть пустым, но не null
        this.description = (description == null) ? "" : description.trim();

        // ID может быть null (для новых объектов)
        this.id = id;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }
}