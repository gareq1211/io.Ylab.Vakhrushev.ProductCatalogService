package io.ylab.productcatalogservice.service;

import io.ylab.productcatalogservice.model.AuditLog;

import java.util.List;

public interface AuditService {
    void log(String action, String details);
    List<AuditLog> getLogs();
}