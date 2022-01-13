package com.xkazxx.jaegerdemo;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;

import java.util.concurrent.TimeUnit;

/**
 * @author created by xkazxx
 * @version v0.0.1
 * description: com.xkazxx.jaegerdemo
 * date:2022/1/10
 */
public class TestJaegerVersion2 {
  Tracer tracer = config.getOpenTelemetry().getTracer("instrumentation-library-name", "1.0.0");


  public void hello(String name) {

    Span span = tracer.spanBuilder("hello").startSpan();
    System.out.println("Hello " + name);
    try {
      Thread.sleep(TimeUnit.SECONDS.toMillis(1));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    this.process(name);
    span.end();
  }

  private void process(String name) {

    Span span = tracer.spanBuilder("process").startSpan();

    try {
      Thread.sleep(TimeUnit.SECONDS.toMillis(2));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    span.setStatus(StatusCode.ERROR, "Change it to your error message");
    span.end();
  }


  public void dispatch(String cmd, String content) {
    Span span = tracer.spanBuilder("dispatch").startSpan();
    if (cmd.equals("hello")) {
      this.hello(content);
    }
    if (null != span) {
      span.end();
    }
  }

  public static void main(String[] args) {
    new TestJaegerVersion2().dispatch("hello", "test jaeger");
  }


  static class config {


    public static OpenTelemetry getOpenTelemetry() {
      SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
              .addSpanProcessor(BatchSpanProcessor.builder(
                      OtlpGrpcSpanExporter.builder().setEndpoint("http://localhost:6831").build()).build())
              .build();

      OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
              .setTracerProvider(sdkTracerProvider)
              .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
              .buildAndRegisterGlobal();
      return openTelemetry;
    }
  }
}
