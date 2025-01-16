package kr.hhplus.be.server.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
public class LogFilter extends OncePerRequestFilter {
    private static final String TRACE_ID = "traceId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        // Trace ID 설정 (UUID 사용, 분산 환경용)
        String traceId = UUID.randomUUID().toString();
        MDC.put(TRACE_ID, traceId);

        // 요청 시작 시간 계산
        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(requestWrapper, responseWrapper); // 요청을 필터에 전달
            log.info("[{}] request : {}", traceId, new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8)); // request body값 출력
        } finally {
            // 요청 처리 시간 계산
            long duration = System.currentTimeMillis() - startTime;
            // 요청 URI 및 처리 시간 로그 기록
            String method = request.getMethod();
            String uri = request.getRequestURI();
            log.info("[{}] - Request [{} {}] completed in {} ms", traceId, method, uri, duration);
            log.info("[{}] response : {} ", traceId, new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8)); // response body값 출력
            responseWrapper.copyBodyToResponse(); // 요청을 전달
            MDC.clear();
        }
    }

}

