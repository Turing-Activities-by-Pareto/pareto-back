package com.pareto.activities.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pareto.activities.config.Constant;
import com.pareto.activities.enums.BusinessStatus;
import com.pareto.activities.exception.BusinessException;
import com.pareto.activities.redis.RedisRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Duration;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DuplicationAspect {

    private final RedisRepository<String, Object> redisRepository;
    private final ObjectMapper objectMapper;

    @Pointcut("@annotation(handleDuplication)")
    public void handleDuplicationPointcut(HandleDuplication handleDuplication) {
    }

    @Around(value = "handleDuplicationPointcut(handleDuplication)", argNames = "joinPoint,handleDuplication")
    public void checkForDuplicates(
            ProceedingJoinPoint joinPoint,
            HandleDuplication handleDuplication
    ) throws Throwable {

        Method method = ((org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature()).getMethod();
        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = method.getParameters();

        Object dto = null;

        for (int i = 0; i < parameters.length; i++) {
            if (AnnotationUtils.findAnnotation(parameters[i], RequestBody.class) != null) {
                dto = args[i];
                break;
            }
        }

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();

        String requestKey = generateRequestKey(request, dto);

        if (isDuplicate(requestKey)) {
            throw new BusinessException(BusinessStatus.DUPLICATE_REQUEST, HttpStatus.CONFLICT);
        }

        joinPoint.proceed();

        cacheRequest(requestKey);
    }

    private String generateRequestKey(
            HttpServletRequest request,
            Object dto
    ) throws IOException {

        StringBuilder requestDetails = new StringBuilder();

        requestDetails.append(request.getMethod());
        requestDetails.append(request.getRequestURI());
        requestDetails.append(objectMapper.writeValueAsString(dto));
        log.info("Cached key for request: {}", objectMapper.writeValueAsString(dto));
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

        return DigestUtils.md5DigestAsHex(
                requestDetails.toString()
                        .getBytes()
        );
    }

    private boolean isDuplicate(String requestKey) {
        return redisRepository.findByKey(requestKey)
                .isPresent();
    }

    private void cacheRequest(String requestKey) {
        redisRepository.putByKey(requestKey, Constant.EMPTY_STRING, Duration.ofSeconds(40));
    }
}

