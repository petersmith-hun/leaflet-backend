package hu.psprog.leaflet.web.metrics;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Metric set for custom system metrics.
 *
 * @author Peter Smith
 */
@Component
public class SystemMetricSet implements MetricSet {

    private List<SystemMetric> systemMetrics;

    @Autowired
    public SystemMetricSet(List<SystemMetric> systemMetrics) {
        this.systemMetrics = systemMetrics;
    }

    @Override
    public Map<String, Metric> getMetrics() {
        return systemMetrics.stream()
                .collect(Collectors.toMap(SystemMetric::getMetricName, Function.identity()));
    }
}
