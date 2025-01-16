# Chapter 2 회고록

## Chapter 2 과정

**Chapter 2-1**

1. Milestone으로 일정 세우기
2. 요구사항 분석하기
3. 시퀀스 다이어그램 
4. ERD 작성하기
5. API 명세하기

**Chapter 2-2**

1. Swagger로 API 문서 작성하기
2. 주요 비즈니스 로직 개발 및 단위 테스트 작성
3. 비즈니스 유즈케이스 개발 및 통합 테스트 작성

**Chapter 2-3**

1. filter와 interceptor를 사용하여 스프링 aop 개선
2. 시나리오 별 동시성 통합테스트 작성(잔액 충전 / 좌석 예약 / 결제)

## Chapter 2를 진행하면서 고민 & 해결했던 부분

### **활성/ 만료 스케줄러를 사용한 대기열 시스템**

다수의 요청이 몰렸을 때 서버의 부하를 분산시키기 위해 대기열 구축하였다.

1. 대기열 토큰을 생성할 때 대기 상태로 생성 
2. 일정 갯수만큼 대기 → 활성 상태로 변환
3. 만료 시간이 지나면 토큰 제거 

위와 같이 세 과정을 지속적으로 체크하여야 했었다. 그래서 스프링 스케줄러를 사용하여 

1. 대기 → 활성,
2. 대기, 활성 → 만료

하는 과정에 스케줄러를 사용하였다.

presentation과 동일한 레이어에 스케줄러 컴포넌트를 생성하고 1분마다 실행되게 하였다.

QueueScheduler.java

```java
@Scheduled(fixedRate = 10000) // 1분마다 실행
public void processQueue() {
    // 대기 토큰 활성화
    queueService.activeToken();
}

@Scheduled(fixedRate = 10000) // 1분마다 실행
public void expiredQueue() {
    // 만료시간 지난 토큰 제거
    queueService.deleteToken();
}
```

QueueService.java

```java
@Transactional
public void activeToken() {
    List<Queue> pendingQueues = queueRepository.findTopNByWaitStatusOrderByCreatedAt("WAIT", 10);

    List<Long> queueIds = pendingQueues.stream()
            .map(Queue::getId)
            .collect(Collectors.toList());

    // 활성 상태로 업데이트
    queueRepository.updateQueueStatus(QueueStatus.ACTIVE, queueIds);
}

@Transactional
public int deleteToken() {
    LocalDateTime now = LocalDateTime.now();
    return queueRepository.deleteExpiredTokens(now);
}
```

.findTopNByWaitStatusOrderByCreatedAt("WAIT", 10) 를 통해 ‘WAIT’ 상태의 큐토큰 1분마다 10개씩 가져와서 ‘ACTIVE’ 시키고 만료시간이 지난 토큰은 제거하여 한꺼번에 많은 요청이 몰리지 않도록 제어한다.

### **Interceptor에서 대기열 토큰 검증**

유저 대기열 토큰을 발급하고 헤더에 담아서 전달하는데 그 이후로 모든 API 호출 시에 토큰 검증이 이루어져야 한다. 요청이 컨트롤러에 도달하기 전에 검증을 하기 위해서 interceptor의 prehandle()를 사용하였다.

```java
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
{
    String tokenQueueId = request.getHeader("Queue-Token-Queue-Id");

    //헤더에 토큰이 비어있지 않고 검증도 통과하면 토큰 발급 중지
    if (tokenQueueId != null && !tokenQueueId.isEmpty() && queueFacade.isQueueValidToken(tokenQueueId)) {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_CONFLICT); // 409 상태 반환
        // ApiResponse 객체 생성
        ApiResponse<Object> apiResponse = ApiResponse.failure("헤더에 동일한 사용자의 대기열 토큰이 있습니다.", HttpServletResponse.SC_CONFLICT);

        // JSON 변환 및 응답 쓰기
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(jsonResponse);
        return false; // 요청 중단
    }

    return true;
}
```

토큰 발급 받기 전 헤더에 이미 대기열 토큰이 있는지 검증하는 인터셉터이다.

```java
@Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
  {
      String tokenQueueId = request.getHeader("Queue-Token-Queue-Id");

      //헤더에 토큰이 비어있거나 검증에 통과하지 못하면 대기열 진입 불가
      if (tokenQueueId == null || tokenQueueId.isEmpty() || !queueFacade.isQueueValidToken(tokenQueueId)) {
          response.setCharacterEncoding("UTF-8");
          response.setContentType("text/plain; charset=UTF-8");
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 반환
          // ApiResponse 객체 생성
          ApiResponse<Object> apiResponse = ApiResponse.failure("해당 대기열 토큰은 접근할 수 없습니다.", HttpServletResponse.SC_UNAUTHORIZED);

          // JSON 변환 및 응답 쓰기
          ObjectMapper objectMapper = new ObjectMapper();
          String jsonResponse = objectMapper.writeValueAsString(apiResponse);
          response.getWriter().write(jsonResponse);
          return false; // 요청 중단

      }

      return true;
  }
```

토큰 발급 이후 API 진입 시 헤더에 든 토큰을 검증하는 인터셉터이다.
****

### **Filter에서 로깅**

API 호출할 때 요청값과 응답값을 로그로 남기려고 하는데 필터만이 유일하게 스프링 외부에서 동작하기에 Request와 Response 등을 직접 조작할 수가 있다는 이점있어 필터를 사용하여 로깅 처리를 하였다.

```java
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
```

추가적으로 API의 작업 시간을 구하기 위해서 요청 시작 시간과 요청 완료 후 현재 시간을 빼서 
얼마나 걸리는지도 로그로 남겨주었다.

### 회고

시퀀스 다이어그램을 그리면서 진행 과정을 미리 정리해 보고 그대로 참고하여 개발하였는데도 다른 예외 상황이 발생하였다. 예약 후에 임시 대기 5분을 설정하는 부분이 있었는데 처음에는 파사드에서 예약 만료 시간을 체크해서 조건에 따라 각 로직이 수행되게 하였는데 이렇게 하다 보니 코드도 길어지고 테스트하기도 어려웠다. <br>
그리고 결제 시스템은 금융과 관련한 부분이기 때문에 시간을 더 타이트하게 잡아서 상태를 지속적으로 확인해줘야 한다. 이 부분도 별도로 스케줄러를 걸어서 만료체크를 하고 좌석, 스케줄 상태를 변경해 주었다. 기능이 잘 동작하는 것을 확인하였지만 예외 상황의 단위 테스트를 예측 가능한 상황만 짠 것 같아서 조금 아쉬웠다. 시간이 조금 남을 때 실패 테스트를 더 짜놓으면 좋을 것 같다. <br>
Chapter-2를 진행하면서 Chapter-1보다 난이도가 급상승해서 따라가는 것에 어려움이 있었고, 과제를 모두 제출하지 못한 부분도 있었지만, 제출 이후에도 피드백 받은 부분에 대해 개선하면서 발제는 내용은 모두 진행해서 다행이다. 앞으로도 자투리 시간을 잘 활용해야겠다.
