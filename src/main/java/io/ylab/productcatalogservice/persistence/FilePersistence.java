// io.ylab.productcatalogservice.persistence.FilePersistence.java
package io.ylab.productcatalogservice.persistence;

import io.ylab.productcatalogservice.model.Product;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Класс для сохранения и загрузки данных каталога товаров в/из JSON-файла.
 * Использует простой ручной парсинг и сериализацию без сторонних библиотек.
 */
public class FilePersistence {
    /** Путь к файлу, в котором хранятся данные о товарах в формате JSON. */
    private static final String FILE_PATH = "products.json";

    /**
     * Сохраняет все товары из переданного хранилища в JSON-файл.
     * Файл перезаписывается полностью при каждом вызове.
     *
     * @param storage карта товаров, где ключ — уникальный идентификатор товара (ID),
     *                а значение — объект {@link Product}
     */
    public void save(ConcurrentHashMap<Long, Product> storage) {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(FILE_PATH))) {
            printWriter.println("[");
            boolean first = true;
            for (Product p : storage.values()) {
                if (!first) {
                    printWriter.println(","); // перенос между объектами
                }
                printWriter.print(productToJson(p));
                first = false;
            }
            if (!storage.isEmpty()) {
                printWriter.println(); // завершающий перенос
            }
            printWriter.println("]");
        } catch (IOException e) {
            System.err.println("Ошибка сохранения: " + e.getMessage());
        }
    }
    /**
     * Загружает товары из JSON-файла в память.
     * Если файл не существует или пуст, возвращает пустую карту.
     * При ошибках парсинга некорректные записи пропускаются, и выводится сообщение об ошибке.
     *
     * @return карта загруженных товаров в формате {@code ConcurrentHashMap<Long, Product>}.
     *         Возвращает пустую карту, если файл отсутствует или повреждён.
     */
    public ConcurrentHashMap<Long, Product> load() {
        ConcurrentHashMap<Long, Product> storage = new ConcurrentHashMap<>();
        if (!Files.exists(Paths.get(FILE_PATH))) {
            return storage;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String content = sb.toString().trim();
            if (content.startsWith("[") && content.endsWith("]")) {
                String arrayContent = content.substring(1, content.length() - 1).trim();
                if (!arrayContent.isEmpty()) {
                    String[] objects = arrayContent.split("(?<=\\}),\\s*(?=\\{)");
                    for (String obj : objects) {
                        Product product = jsonToProduct(obj.trim());
                        if (product != null) {
                            storage.put(product.getId(), product);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка загрузки: " + e.getMessage());
        }
        return storage;
    }
    /**
     * Преобразует объект {@link Product} в корректную JSON-строку формата объекта.
     * Все строковые поля экранируются для корректного отображения в JSON.
     *
     * @param p товар для сериализации
     * @return строковое представление товара в формате JSON (без завершающей запятой)
     */
    private String productToJson(Product p) {
        return "  {\n" +
                "    \"id\": " + p.getId() + ",\n" +
                "    \"name\": \"" + escapeJson(p.getName()) + "\",\n" +
                "    \"category\": \"" + escapeJson(p.getCategory()) + "\",\n" +
                "    \"brand\": \"" + escapeJson(p.getBrand()) + "\",\n" +
                "    \"price\": " + String.format("%.2f", p.getPrice()) + ",\n" +
                "    \"description\": \"" + escapeJson(p.getDescription()) + "\"\n" +
                "  }";
    }
    /**
     * Экранирует специальные символы в строке для корректной записи в JSON.
     * Заменяет обратные слеши {@code \} на {@code \\} и кавычки {@code "} на {@code \"}.
     *
     * @param s исходная строка (может быть {@code null})
     * @return экранированная строка; если входная строка {@code null}, возвращается пустая строка
     */
    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
    /**
     * Преобразует JSON-строку (представление одного товара) обратно в объект {@link Product}.
     * Выполняет базовый парсинг: извлекает поля по ключам и приводит их к нужным типам.
     * При любых ошибках (неверный формат, отсутствующие поля, исключения парсинга)
     * метод выводит сообщение об ошибке и возвращает {@code null}.
     *
     * @param json JSON-строка, представляющая один объект товара (в фигурных скобках)
     * @return объект {@link Product}, если парсинг прошёл успешно; {@code null} в случае ошибки
     */
    private Product jsonToProduct(String json) {
        try {
            if (json.isEmpty() || !json.startsWith("{") || !json.endsWith("}")) {
                return null;
            }
            json = json.substring(1, json.length() - 1); // убираем {}
            String[] fields = json.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); // умный split по запятым вне кавычек
            long id = 0;
            String name = "", category = "", brand = "", description = "";
            double price = 0.0;

            for (String field : fields) {
                String[] keyValue = field.split(":", 2);
                if (keyValue.length != 2) continue;
                String key = unquote(keyValue[0].trim());
                String value = unquote(keyValue[1].trim());

                switch (key) {
                    case "id":
                        id = Long.parseLong(value);
                        break;
                    case "name":
                        name = value;
                        break;
                    case "category":
                        category = value;
                        break;
                    case "brand":
                        brand = value;
                        break;
                    case "price":
                        price = Double.parseDouble(value);
                        break;
                    case "description":
                        description = value;
                        break;
                }
            }
            return new Product(id, name, category, brand, price, description);
        } catch (Exception e) {
            System.err.println("Ошибка парсинга JSON: " + json);
            return null;
        }
    }
    /**
     * Удаляет внешние кавычки из строки (если они есть) и восстанавливает экранированные символы.
     * Обратные слеши и кавычки, экранированные как {@code \\} и {@code \"}, преобразуются обратно.
     *
     * @param string строка, возможно заключённая в двойные кавычки
     * @return строка без внешних кавычек и с восстановленными экранированными символами
     */
    private String unquote(String string) {
        if (string.startsWith("\"") && string.endsWith("\"")) {
            return string.substring(1, string.length() - 1)
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\");
        }
        return string;
    }
}