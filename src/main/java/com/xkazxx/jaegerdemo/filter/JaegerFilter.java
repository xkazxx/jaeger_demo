package com.xkazxx.jaegerdemo.filter;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author created by xkazxx
 * @version v0.0.1
 * description: com.xkazxx.jaegerdemo.filter
 * date:2022/1/13
 */
@Component
public class JaegerFilter implements Filter {


  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    final String matchValue = JaegerFilter.class.getName();
    final Tracer tracer = GlobalTracer.get();
    Span span = tracer.buildSpan("hello").start();
    try(final Scope scope = tracer.activateSpan(span);){
      chain.doFilter(request,response);
    }finally {
      span.setTag("matchValue",matchValue);
      span.log("TestJaeger service say hello to " + matchValue);
      span.finish();
    }
  }
}
