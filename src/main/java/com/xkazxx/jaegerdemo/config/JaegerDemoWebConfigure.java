package com.xkazxx.jaegerdemo.config;

import io.jaegertracing.thrift.internal.senders.UdpSender;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.jaeger.thrift.JaegerThriftSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import org.apache.thrift.transport.TTransportException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author created by xkazxx
 * @version v0.0.1
 * description: com.xkazxx.jaegerdemo.config
 * date:2022/1/11
 */
@Configuration
public class JaegerDemoWebConfigure {
  @Bean
  public OpenTelemetry initOpenTelemetry() throws TTransportException {
    JaegerThriftSpanExporter exporter =
            JaegerThriftSpanExporter.builder()
                    .setThriftSender(new UdpSender())
                    .setEndpoint("http://localhost:6831/api/traces")
                    .build();
    SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(BatchSpanProcessor.builder(exporter).build())
            .build();

    OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
            .setTracerProvider(sdkTracerProvider)
            .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
            .buildAndRegisterGlobal();
    Runtime.getRuntime().addShutdownHook(new Thread(sdkTracerProvider::close));
    return openTelemetry;
  }
}
