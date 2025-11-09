package io.ylab.productcatalogservice.service;

import io.ylab.productcatalogservice.model.AuditLog;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileAuditService implements AuditService {
    /** Имя файла, в который записываются логи аудита. */
    private static final String LOG_FILE = "audit.log";
    /** Форматтер для преобразования {@link LocalDateTime} в строку и обратно. */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Записывает новую запись в журнал аудита (лог-файл) на диск.
     * Каждая запись содержит временную метку, действие и детали.
     * Метод синхронизирован для обеспечения потокобезопасности при записи из нескольких потоков.
     * Файл открывается в режиме добавления (append), поэтому новые записи дописываются в конец.
     *
     * @param action  тип действия (например, "ADD", "UPDATE", "DELETE", "SEARCH")
     * @param details дополнительная информация о действии (например, "Product: Название товара")
     */
    @Override
    public synchronized void log(String action, String details) {
        AuditLog log = new AuditLog(action, details);
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            writer.println(log.getTimestamp().format(FORMATTER) + " | " + action + " | " + details);
        } catch (IOException e) {
            System.err.println("Не удалось записать аудит: " + e.getMessage());
        }
    }
    /**
     * Считывает все записи аудита из лог-файла и возвращает их в виде списка объектов {@link AuditLog}.
     * Формат ожидаемой строки в файле: "ГГГГ-ММ-ДД ЧЧ:ММ:СС | ДЕЙСТВИЕ | ДЕТАЛИ".
     * Если файл не существует, возвращает пустой список.
     * При ошибках парсинга отдельных строк они пропускаются, а в консоль выводится сообщение об ошибке.
     *
     * @return список объектов {@link AuditLog}, отсортированный по времени (в порядке чтения из файла);
     *         пустой список, если файл отсутствует или пуст
     */
    @Override
    public List<AuditLog> getLogs() {
        List<AuditLog> logs = new ArrayList<>();
        if (!Files.exists(Paths.get(LOG_FILE))) return logs;
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Формат: 2025-11-08 12:30:45 | ADD | Product: ...
                String[] parts = line.split(" \\| ", 3);
                if (parts.length == 3) {
                    LocalDateTime ts = LocalDateTime.parse(parts[0], FORMATTER);
                    logs.add(new AuditLog(ts, parts[1], parts[2]));
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка чтения аудита: " + e.getMessage());
        }
        return logs;
    }
}