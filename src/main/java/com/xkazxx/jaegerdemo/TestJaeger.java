//package com.xkazxx.jaegerdemo;
//
//import io.opentracing.Span;
//import io.opentracing.Tracer;
//import io.opentracing.util.GlobalTracer;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * @author created by xkazxx
// * @version v0.0.1
// * description: com.xkazxx.jaegerdemo
// * date:2022/1/10
// */
//
//public class TestJaeger {
//
//
//  public TestJaeger() {
//    JaegerTraceConfig.initTracer("JaegerDemo");
//  }
//
//  public void hello(String name) {
//    final Tracer tracer = GlobalTracer.get();
//    Span span = tracer.buildSpan("hello").start();
//    tracer.activateSpan(span);
//    System.out.println("Hello " + name);
//    try {
//      Thread.sleep(TimeUnit.SECONDS.toMillis(1));
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
//    this.process(name);
//    span.setTag("name", name);
//    span.log("TestJaeger service say hello to " + name);
//    span.finish();
//  }
//
//  private void process(String name) {
//    final Tracer tracer = GlobalTracer.get();
//    Span span = tracer.buildSpan("process").start();
//    tracer.activateSpan(span);
//    try {
//      Thread.sleep(TimeUnit.SECONDS.toMillis(2));
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
//    span.setTag("name2", name);
//    span.log("TestJaeger service say hello to 2 " + name);
//    span.finish();
//  }
//
//
//  public void dispatch(String cmd, String content) {
//    final Tracer tracer = GlobalTracer.get();
//    Span span = tracer.buildSpan("dispatch").start();
//    tracer.activateSpan(span);
//    if (cmd.equals("hello")) {
//      this.hello(content);
//    }
//    if (null != span) {
//      span.setTag("cmd", cmd);
//      span.finish();
//    }
//  }
//
//  public static void main(String[] args) {
//    new TestJaeger().dispatch("hello","test jaeger");
//  }
//
//}
