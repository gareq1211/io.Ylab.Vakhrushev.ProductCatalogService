package io.ylab.productcatalogservice.service;

import io.ylab.productcatalogservice.model.AuditLog;

import java.util.ArrayList;
import java.util.List;

public class InMemoryAuditService implements AuditService {
    private final List<AuditLog> logs = new ArrayList<>();

    @Override
    public void log(String action, String details) {
        logs.add(new AuditLog(action, details));
    }

    @Override
    public List<AuditLog> getLogs() {
        return new ArrayList<>(logs);
    }
}