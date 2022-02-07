package me.duelsol.springcloudseed.consumer;

import feign.*;
import feign.codec.ErrorDecoder;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author 冯奕骅
 */
@Aspect
@RestControllerAdvice
@Configuration
@Component
public class FeignDefaultErrorHandler implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        Default defaultErrorDecoder = new Default();
        try {
            String message = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            return new FeignBaseException(message, defaultErrorDecoder.decode(methodKey, response));
        } catch (IOException e) {
            return defaultErrorDecoder.decode(methodKey, response);
        }
    }

    @AfterThrowing(throwing = "cause", pointcut = "@annotation(feignClient) || @within(feignClient)")
    public void afterThrowing(Throwable cause, FeignClient feignClient) {
        throw new FeignBaseException(cause);
    }

    @ExceptionHandler(value = FeignBaseException.class)
    public String feignBaseExceptionHandler(FeignBaseException cause) {
        return cause.getCause().getMessage();
    }

    private static class FeignBaseException extends RuntimeException {

        public FeignBaseException(Throwable cause) {
            super(cause);
        }

        public FeignBaseException(String message, Throwable cause) {
            super(message, cause);
        }

    }

}
