package com.xkazxx.jaegerdemo.persistent;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author created by xkazxx
 * @version v0.0.1
 * description: com.xkazxx.jaegerdemo.persistent
 * date:2022/1/14
 */
@Component
public class UserRepository {

  @Value("${spring.application.name}")
  private String applicationName;
  @Autowired
  private OpenTelemetry openTelemetry;

  public String findUserNameById(String id) throws InterruptedException {
    Span childSpan = openTelemetry.getTracer(applicationName).spanBuilder("child").setAttribute("query db", "findUserNameById").startSpan();
    try (Scope scope = childSpan.makeCurrent()) {
      Thread.sleep(1000);
      return "张三";
    } finally {

      childSpan.end();
    }
  }

}
