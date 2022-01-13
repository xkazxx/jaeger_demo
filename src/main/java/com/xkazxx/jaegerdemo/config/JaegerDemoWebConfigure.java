package com.xkazxx.jaegerdemo.config;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
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

//
//  @Bean
//  public FilterRegistrationBean<TracingFilter> corsFilterRegistrationBean() {
//    FilterRegistrationBean<TracingFilter> registrationBean = new FilterRegistrationBean<>();
//    registrationBean.setFilter(new TracingFilter());
//    registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//    return registrationBean;
//  }

  @Bean
  public OpenTelemetry initOpenTelemetry() {
    SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(BatchSpanProcessor.builder(OtlpGrpcSpanExporter.builder().setEndpoint("http://127.0.0.1:14268").build()).build())
            .build();

    OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
            .setTracerProvider(sdkTracerProvider)
            .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
            .buildAndRegisterGlobal();
    Runtime.getRuntime().addShutdownHook(new Thread(sdkTracerProvider::close));
    return openTelemetry;
  }

  public static void main(String[] args){
    OtlpGrpcSpanExporter grpcSpanExporter = OtlpGrpcSpanExporter.builder()
            .setEndpoint("http://127.0.0.1:6831")
            .build();

    SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(BatchSpanProcessor.builder(grpcSpanExporter).build())
            .setResource(Resource.create(Attributes.builder()
                    .put(AttributeKey.stringKey("service.name"), "payment")
                    .put(AttributeKey.stringKey("service.namespace"), "order")
                    .put(AttributeKey.stringKey("service.version"), "1.0")
                    .put(AttributeKey.stringKey("host.name"), "localhost")
                    .build()))
            .build();

    OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
            .setTracerProvider(tracerProvider)
            .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
            .build();

    Tracer tracer =
            openTelemetry.getTracer("instrumentation-library-name", "1.0.0");
    Span parentSpan = tracer.spanBuilder("parent").startSpan();

    try (final Scope scope = parentSpan.makeCurrent()){
//      Span childSpan = tracer.spanBuilder("child")
//              .setParent(Context.current().with(parentSpan))
//              .startSpan();
//      childSpan.setAttribute("test", "vllelel");
//      // do stuff
      Thread.sleep(1000);
//      childSpan.end();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      parentSpan.end();
    }
  }
}
