package io.ylab.productcatalogservice.ui.command;

import io.ylab.productcatalogservice.service.MetricsService;

public class ShowMetricsCommand implements Command {
    private final MetricsService metrics;
    public ShowMetricsCommand(MetricsService metrics) { this.metrics = metrics; }
    public void execute() {
        System.out.println("Товаров: " + metrics.getTotalProducts());
        System.out.println("   Товаров в каталоге: " + metrics.getTotalProducts());
    }
}