package com.xkazxx.jaegerdemo.config;

import io.jaegertracing.Configuration;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @author created by xkazxx
 * @version v0.0.1
 * description: com.xkazxx.jaegerdemo
 * date:2022/1/10
 */
@org.springframework.context.annotation.Configuration
public class JaegerTraceConfig {

  @Value("${spring.application.name}")
  private String applicationName;

  @Bean
  public Tracer initTracer() {
    Configuration config = new Configuration(applicationName);
    Configuration.SenderConfiguration sender = new Configuration.SenderConfiguration();
    sender.withAgentHost("127.0.0.1");
    sender.withAgentPort(6831);
    config.withReporter(new Configuration.ReporterConfiguration().withSender(sender).withFlushInterval(100).withLogSpans(true));

    config.withSampler(new Configuration.SamplerConfiguration().withType("const").withParam(1));

    io.opentracing.Tracer tracer = config.getTracer();
    System.out.println(tracer.toString());
    GlobalTracer.registerIfAbsent(tracer);
    return tracer;
  }
}

