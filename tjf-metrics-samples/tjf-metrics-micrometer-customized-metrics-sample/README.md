# Metrics Micrometer Customized Metrics

## Contexto

O tjf-metrics-micrometer tem  a capacidade de criar métricas personalizadas com tags customizadas em uma aplicação. Isso pode ser alcançado de duas maneiras: globalmente, configurando propriedades específicas na aplicação, que se aplicam a todas as métricas criadas, ou localmente, passando tags personalizadas como parâmetros no momento da criação de uma métrica, tornando-as específicas para aquela métrica em particular. Essa flexibilidade permite que os desenvolvedores coletem dados de maneira altamente personalizada, tornando o monitoramento e análise de métricas mais adaptados às necessidades de sua aplicação.. 

No exemplo publicado, simplificamos ao máximo a inserção de métricas.

### Inserção de métricas via configuração:

```yml
management:
  endpoints:
    web:
      base-path: /_
      exposure:
        include: prometheus
  metrics:
    enable:
      all: true
    export:
      prometheus:
        enabled: true
  endpoint:
    metrics:
      enabled: true
    prometheus:
      enabled: true

tjf:
  metrics:
    app: app1
    context: context1
    element-type: business
    platform: totvs.apps
    service: backend
    version: 1.0.0
    custom-tags:
      custom: sampleValue
```

### Inserção de métricas via código:

```java
public MyMetricService(MetricsGauge metricsGauge, MetricsCounter metricsCounter, MicrometerConfiguration config) {
        this.metricsCounter = metricsCounter;

        var tags = Tags.of(Tag.of("application_id", "893ff0c3-8e8e-4392-88a8-27951dddfc1f"),
        Tag.of("Name", "APP 2"));

        metricsGauge.register("GaugeMetricSample", tags, this, value -> getMyMetricValue());
        this.counter = metricsCounter.register("CounterMetricSample", tags);
        }
```

### Prometheus

Agora você poderá utilizar suas métricas customizadas no Prometheus.

```
GaugeMetricSample{Name="APP 2", app="app1", application_id="893ff0c3-8e8e-4392-88a8-27951dddfc1f", context="context1", custom="sampleValue", element_type="business", instance="host.docker.internal:8080", job="metrics-micrometer", metric_class="instance", metric_type="tech", platform="totvs.apps", service="backend", version="1.0.0"}
```