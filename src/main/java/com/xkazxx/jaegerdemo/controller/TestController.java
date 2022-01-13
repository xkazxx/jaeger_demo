package com.xkazxx.jaegerdemo.controller;

import cn.hutool.http.HttpUtil;
import com.sun.deploy.net.HttpUtils;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author created by xkazxx
 * @version v0.0.1
 * description: com.xkazxx.jaegerdemo.controller
 * date:2022/1/11
 */
@RestController
public class TestController {
  @Autowired
  private OpenTelemetry openTelemetry;
  @Value("${spring.application.name}")
  private String applicationName;
  @GetMapping("")
  public String init() throws InterruptedException {

    Thread.sleep(1000);
    final String s = HttpUtil.get("http://localhost:8989/hello");
    System.out.println(s);
    return "this is index string";
  }


  @GetMapping("/hello")
  public String hello() throws InterruptedException {
    Span span = openTelemetry.getTracer(applicationName).spanBuilder("jaegerdemo").startSpan();
    span.setAttribute("http.method", "GET");
    Thread.sleep(1000);
    span.end();
    return "hello jaeger";
  }

}
