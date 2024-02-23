package com.tjf.sample.github.service;

import com.totvs.tjf.metrics.MetricsCounter;
import com.totvs.tjf.metrics.MetricsGauge;
import com.totvs.tjf.metrics.config.MicrometerConfiguration;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import org.springframework.stereotype.Service;

@Service
public class MyMetricService {

    private double myMetricValue = 0;
    private final Counter counter;

    private final MetricsCounter metricsCounter;

    public MyMetricService(MetricsGauge metricsGauge, MetricsCounter metricsCounter, MicrometerConfiguration config) {
        this.metricsCounter = metricsCounter;

        var tags = Tags.of(Tag.of("application_id", "893ff0c3-8e8e-4392-88a8-27951dddfc1f"),
                Tag.of("Name", "APP 2"));

        metricsGauge.register("GaugeMetricSample", tags, this, value -> getMyMetricValue());
        this.counter = metricsCounter.register("CounterMetricSample", tags);
    }

    public void clientIncrement(String clientId) {
        metricsCounter.incrementBusinessMetric("countByClient", Tags.of(Tag.of("client_id", clientId)));
    }

    public double getMyMetricValue() {
        return myMetricValue;
    }

    public void setMyMetricValue(double value) {
        this.myMetricValue = value;
    }
}