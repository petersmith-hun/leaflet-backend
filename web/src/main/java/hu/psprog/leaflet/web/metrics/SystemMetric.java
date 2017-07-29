package hu.psprog.leaflet.web.metrics;

import com.codahale.metrics.Gauge;

/**
 * Custom gauge-type system metrics.
 *
 * @author Peter Smith
 */
public interface SystemMetric<T> extends Gauge<T> {

    /**
     * Returns metric's name.
     *
     * @return metric name
     */
    String getMetricName();
}
