package com.xkazxx.jaegerdemo.util;

import cn.hutool.http.HttpBase;
import cn.hutool.http.HttpRequest;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapPropagator;

/**
 * @author created by xkazxx
 * @version v0.0.1
 * description: com.xkazxx.jaegerdemo.util
 * date:2022/1/14
 */
public class JaegerHttpUtil {


  public static String getRequest(String url, OpenTelemetry openTelemetry) {
    final HttpRequest request = HttpRequest.get(url);
    TextMapPropagator textMapPropagator = openTelemetry.getPropagators().getTextMapPropagator();
    textMapPropagator.inject(Context.current(), request, HttpBase::header);
    return request.execute().body();
  }
}
