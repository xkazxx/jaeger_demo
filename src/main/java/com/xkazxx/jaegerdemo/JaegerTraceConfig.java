//package com.xkazxx.jaegerdemo;
//
//import io.jaegertracing.Configuration;
//import io.jaegertracing.internal.JaegerTracer;
//import io.jaegertracing.internal.metrics.Metrics;
//import io.jaegertracing.internal.metrics.NoopMetricsFactory;
//import io.jaegertracing.internal.reporters.CompositeReporter;
//import io.jaegertracing.internal.reporters.LoggingReporter;
//import io.jaegertracing.internal.reporters.RemoteReporter;
//import io.jaegertracing.internal.samplers.ConstSampler;
//import io.jaegertracing.thrift.internal.senders.HttpSender;
//import io.opentracing.util.GlobalTracer;
//import org.apache.thrift.transport.TTransportException;
//
///**
// * @author created by xkazxx
// * @version v0.0.1
// * description: com.xkazxx.jaegerdemo
// * date:2022/1/10
// */
//public class JaegerTraceConfig {
//
//  public static void initTracer(String serviceName) {
//    Configuration config = new Configuration(serviceName);
//    Configuration.SenderConfiguration sender = new Configuration.SenderConfiguration();
//    sender.withAgentHost("127.0.0.1");
//    sender.withAgentPort(6831);
//    config.withReporter(new Configuration.ReporterConfiguration().withSender(sender).withFlushInterval(100).withLogSpans(true));
//
//    config.withSampler(new Configuration.SamplerConfiguration().withType("const").withParam(1));
//
//    io.opentracing.Tracer tracer = config.getTracer();
//    System.out.println(tracer.toString());
//    GlobalTracer.registerIfAbsent(tracer);
//  }
//}
//
