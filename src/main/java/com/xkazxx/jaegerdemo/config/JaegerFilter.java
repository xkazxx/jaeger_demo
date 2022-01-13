//package com.xkazxx.jaegerdemo.config;
//
//import io.opentelemetry.api.GlobalOpenTelemetry;
//import io.opentelemetry.api.trace.Span;
//import io.opentelemetry.api.trace.SpanKind;
//import io.opentelemetry.context.Context;
//import io.opentelemetry.context.Scope;
//import io.opentelemetry.context.propagation.TextMapGetter;
//import io.opentelemetry.context.propagation.TextMapSetter;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.xml.ws.spi.http.HttpExchange;
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.URLConnection;
//
///**
// * @author created by xkazxx
// * @version v0.0.1
// * description: com.xkazxx.jaegerdemo.config
// * date:2022/1/11
// */
//
//public class JaegerFilter implements Filter {
//  private static final String SERVER_SPAN_CONTEXT = "SERVER_SPAN_CONTEXT";
//  private static TextMapGetter<HttpExchange> getter = new TextMapGetter<HttpExchange>() {
//    @Override
//    public String get(HttpExchange carrier, String key) {
//      if (carrier != null && carrier.getRequestHeaders().containsKey(key)) {
//        return carrier.getRequestHeaders().get(key).get(0);
//      }
//      return null;
//    }
//
//    @Override
//    public Iterable<String> keys(HttpExchange carrier) {
//      return carrier.getRequestHeaders().keySet();
//    }
//  };
//
//  // Insert the context as Header
//  private static TextMapSetter<HttpURLConnection> setter = URLConnection::setRequestProperty;
//
//  @Override
//  public void init(FilterConfig filterConfig) throws ServletException {
//    Filter.super.init(filterConfig);
//  }
//
//  @Override
//  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//    final HttpServletRequest servletRequest = (HttpServletRequest) request;
//    if (request.getAttribute(SERVER_SPAN_CONTEXT) != null) {
//      GlobalOpenTelemetry.getTracer(servletRequest.getRequestURL().toString()).spanBuilder(servletRequest.getRequestURI());
//
//      chain.doFilter(request, response);
//    } else {
//      Span outGoing = GlobalOpenTelemetry.getTracer(servletRequest.getRequestURL().toString()).spanBuilder("/resource").setSpanKind(SpanKind.CLIENT).startSpan();
//      try (Scope scope = outGoing.makeCurrent()) {
//        // Use the Semantic Conventions.
//        // (Note that to set these, Span does not *need* to be the current instance in Context or Scope.)
//        outGoing.setAttribute(SemanticAttributes.HTTP_METHOD, "GET");
//        outGoing.setAttribute(SemanticAttributes.HTTP_URL, url.toString());
//        HttpURLConnection transportLayer = (HttpURLConnection) url.openConnection();
//        // Inject the request with the *current*  Context, which contains our current Span.
//        openTelemetry.getPropagators().getTextMapPropagator().inject(Context.current(), transportLayer, setter);
//        // Make outgoing call
//      } finally {
//        outGoing.end();
//      }
//    }
//  }
//
//  @Override
//  public void destroy() {
//    Filter.super.destroy();
//  }
//}
