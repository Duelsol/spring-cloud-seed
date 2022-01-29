package me.duelsol.springcloudseed.gateway;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 冯奕骅
 */
public class CustomExceptionHandler extends DefaultErrorWebExceptionHandler {

    public CustomExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
                                  ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> error = super.getErrorAttributes(request, super.getErrorAttributeOptions(request, MediaType.ALL));
        int status = super.getHttpStatus(error);
        String message = super.getError(request).getMessage();
        Map<String, Object> body = new HashMap<>(2);
        body.put("code", status);
        body.put("message", message);
        return ServerResponse.status(status).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(body));
    }

}
