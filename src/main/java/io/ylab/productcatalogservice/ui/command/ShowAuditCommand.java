package io.ylab.productcatalogservice.ui.command;

import io.ylab.productcatalogservice.service.AuditService;

public class ShowAuditCommand implements Command {
    private final AuditService auditService;
    public ShowAuditCommand(AuditService audit) { this.auditService = audit; }
    public void execute() {
        auditService.getLogs().forEach(System.out::println);
    }
}