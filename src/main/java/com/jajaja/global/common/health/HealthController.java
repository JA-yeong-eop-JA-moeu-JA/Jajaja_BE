package com.jajaja.global.common.health;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health API", description = "헬스 체킹 API")
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "I'm healthy!";
    }
}
