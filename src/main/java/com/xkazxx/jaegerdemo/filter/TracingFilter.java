package com.xkazxx.jaegerdemo.filter;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author created by xkazxx
 * @version v0.0.1
 * description: com.xkazxx.jaegerdemo.config
 * date:2022/1/11
 */
@Component
public class TracingFilter implements Filter {
  @Autowired
  private OpenTelemetry openTelemetry;
  @Value("${spring.application.name}")
  private String applicationName;

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//    Span span = openTelemetry.getTracer(applicationName).spanBuilder("jaegerdemo").startSpan();
    Span span = getServerSpan(openTelemetry.getTracer(applicationName), (HttpServletRequest)servletRequest);
    try (Scope scope = span.makeCurrent()) {
      filterChain.doFilter(servletRequest, servletResponse);
    } catch (Exception ex) {
      span.setStatus(StatusCode.ERROR, "HTTP Code: " + ((HttpServletResponse) servletResponse).getStatus());
      span.recordException(ex);
      throw ex;
    } finally {
      span.end();
    }
  }

  private Span getServerSpan(Tracer tracer, HttpServletRequest httpServletRequest) {
    TextMapPropagator textMapPropagator = openTelemetry.getPropagators().getTextMapPropagator();
    Context context = textMapPropagator.extract(Context.current(), httpServletRequest, new TextMapGetter<HttpServletRequest>() {
      @Override
      public Iterable<String> keys(HttpServletRequest request) {
        List<String> headers = new ArrayList<>();
        for (Enumeration<String> names = request.getHeaderNames(); names.hasMoreElements(); ) {
          String name =  names.nextElement();
          headers.add(name);
        }
        return headers;
      }

      @Override
      public String get(HttpServletRequest request, String s) {
        return request.getHeader(s);
      }
    });

    return tracer.spanBuilder("applicationName").setParent(context).setSpanKind(SpanKind.SERVER).setAttribute("http.method", httpServletRequest.getMethod()).startSpan();
  }
}