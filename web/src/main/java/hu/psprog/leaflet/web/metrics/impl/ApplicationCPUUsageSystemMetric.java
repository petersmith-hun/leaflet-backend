package hu.psprog.leaflet.web.metrics.impl;

import com.sun.management.OperatingSystemMXBean;
import hu.psprog.leaflet.web.metrics.SystemMetric;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.management.ManagementFactory;

/**
 * Gauge metric to measure application CPU load.
 *
 * @author Peter Smith
 */
@Component
public class ApplicationCPUUsageSystemMetric implements SystemMetric<Double> {

    private static final String METRIC_NAME = "cpu.process";

    private OperatingSystemMXBean operatingSystemMXBean;

    @PostConstruct
    public void setup() {
        operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    @Override
    public String getMetricName() {
        return METRIC_NAME;
    }

    @Override
    public Double getValue() {
        return operatingSystemMXBean.getProcessCpuLoad();
    }
}
