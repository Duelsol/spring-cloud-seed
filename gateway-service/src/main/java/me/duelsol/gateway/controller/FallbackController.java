package me.duelsol.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 冯奕骅
 */
@RestController
public class FallbackController {

    @GetMapping("/fallback")
    public String fallback() {
        return "fallback";
    }

}
