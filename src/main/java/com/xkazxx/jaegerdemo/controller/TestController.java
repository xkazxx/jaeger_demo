package com.xkazxx.jaegerdemo.controller;

import cn.hutool.http.HttpRequest;
import com.xkazxx.jaegerdemo.persistent.UserRepository;
import com.xkazxx.jaegerdemo.util.JaegerHttpUtil;
import io.opentelemetry.api.OpenTelemetry;
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

  @Autowired
  private UserRepository userRepository;

  @GetMapping("")
  public String init() throws InterruptedException {
    Thread.sleep(1000);
    String url = "http://localhost:8989/hello";
    String result = JaegerHttpUtil.getRequest(url, openTelemetry); // 添加请求上线文信息，形成调用链
    System.out.println(result);
    return userRepository.findUserNameById(result);
  }


  @GetMapping("/hello")
  public String hello() throws InterruptedException {
    Thread.sleep(1000);
    return "hello jaeger";
  }

}
