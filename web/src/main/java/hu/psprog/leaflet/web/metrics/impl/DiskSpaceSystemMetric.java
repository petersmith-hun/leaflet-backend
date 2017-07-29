package hu.psprog.leaflet.web.metrics.impl;

import hu.psprog.leaflet.web.metrics.SystemMetric;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Custom system metric to return percentage of used disk space.
 *
 * @author Peter Smith
 */
@Component
public class DiskSpaceSystemMetric implements SystemMetric<Double> {

    private static final String METRIC_NAME = "disk.usage";
    private static final File MEASURED_PATH = new File(".");

    @Override
    public String getMetricName() {
        return METRIC_NAME;
    }

    @Override
    public Double getValue() {
        return calculateUsedSpacePercentage();
    }

    private Double calculateUsedSpacePercentage() {
        double usedSpace = (double) MEASURED_PATH.getTotalSpace() - MEASURED_PATH.getFreeSpace();
        return usedSpace / MEASURED_PATH.getTotalSpace();
    }
}
