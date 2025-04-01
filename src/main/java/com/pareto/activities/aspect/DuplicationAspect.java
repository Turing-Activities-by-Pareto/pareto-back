package com.pareto.activities.aspect;

import com.pareto.activities.enums.BusinessStatus;
import com.pareto.activities.exception.BusinessException;
import com.pareto.activities.redis.RedisRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;

@Aspect
@Component
@RequiredArgsConstructor
public class DuplicationAspect {

    private final RedisRepository<String, Object> redisRepository;


    @Pointcut("@annotation(handleDuplication)")
    public void handleDuplicationPointcut(HandleDuplication handleDuplication) {
    }

    @AfterReturning("handleDuplicationPointcut(handleDuplication)")
    public void checkForDuplicates(
            JoinPoint joinPoint,
            HandleDuplication handleDuplication
    ) throws Throwable {

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();

        String requestKey = generateRequestKey(request);

        if (isDuplicate(requestKey)) {
            throw new BusinessException(BusinessStatus.DUPLICATE_REQUEST, HttpStatus.CONFLICT);
        }

        cacheRequest(requestKey);
    }

    private String generateRequestKey(HttpServletRequest request) {
        StringBuilder requestDetails = new StringBuilder();

        requestDetails.append(request.getMethod());
        requestDetails.append(request.getRequestURI());
        request.getParameterMap()
                .forEach((key, value) -> {
                    for (String paramValue : value) {
                        requestDetails.append(key)
                                .append("=")
                                .append(paramValue)
                        ;
                    }
                });

        requestDetails.append(request.getHeader("Content-Type"));
        requestDetails.append(request.getHeader("Authorization"));

        return DigestUtils.md5DigestAsHex(requestDetails.toString()
                .getBytes());
    }

    private boolean isDuplicate(String requestKey) {
        return redisRepository.findByKey(requestKey)
                .isPresent();
    }

    private void cacheRequest(String requestKey) {
        redisRepository.putByKey(requestKey, true, Duration.ofSeconds(40));
    }
}

