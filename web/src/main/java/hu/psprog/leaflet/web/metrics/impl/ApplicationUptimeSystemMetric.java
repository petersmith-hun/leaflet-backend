package hu.psprog.leaflet.web.metrics.impl;

import hu.psprog.leaflet.web.metrics.SystemMetric;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * Gauge metric to retrieve application uptime.
 *
 * @author Peter Smith
 */
@Component
public class ApplicationUptimeSystemMetric implements SystemMetric<Long> {

    private static final String METRIC_NAME = "uptime";

    private final RuntimeMXBean runtimeMXBean;

    public ApplicationUptimeSystemMetric() {
        this.runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    }

    @Override
    public String getMetricName() {
        return METRIC_NAME;
    }

    @Override
    public Long getValue() {
        return runtimeMXBean.getUptime();
    }
}
