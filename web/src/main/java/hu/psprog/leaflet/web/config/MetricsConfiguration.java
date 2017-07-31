package hu.psprog.leaflet.web.config;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import hu.psprog.leaflet.web.metrics.SystemMetricSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Graphite metrics configuration.
 *
 * @author Peter Smith
 */
@Configuration
@EnableMetrics
@ConfigurationProperties(prefix = "metrics")
public class MetricsConfiguration extends MetricsConfigurerAdapter {

    private boolean enabled;
    private String prefix;
    private String host;
    private int port;
    private long period;
    private TimeUnit unit;

    @Autowired
    private SystemMetricSet systemMetricSet;

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        if (enabled) {
            metricRegistry.registerAll(new MemoryUsageGaugeSet());
            metricRegistry.registerAll(new GarbageCollectorMetricSet());
            metricRegistry.registerAll(systemMetricSet);

            registerReporter(buildGraphiteReporter(metricRegistry))
                    .start(period, unit);
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    private GraphiteReporter buildGraphiteReporter(MetricRegistry metricRegistry) {
        return GraphiteReporter
                .forRegistry(metricRegistry)
                .prefixedWith(prefix)
                .build(new Graphite(host, port));
    }
}
