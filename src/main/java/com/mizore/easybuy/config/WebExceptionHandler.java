package com.mizore.easybuy.config;

import com.mizore.easybuy.model.vo.BaseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Configuration
@RestControllerAdvice
@Slf4j
public class WebExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public BaseVO<String> handleRuntimeException(RuntimeException e) {
        log.error(e.toString());
        BaseVO<String> runtimeExceptionBaseVO = new BaseVO<>();
        return runtimeExceptionBaseVO.failure();
    }
}
