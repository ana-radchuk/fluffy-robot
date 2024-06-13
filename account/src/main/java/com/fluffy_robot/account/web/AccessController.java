package com.fluffy_robot.account.web;

import com.fluffy_robot.account.domain.request.RegistrationRequest;
import com.fluffy_robot.account.service.AccessService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class AccessController {

    private final AccessService accessService;

    @Operation(summary = "Create account")
    @PostMapping("/register")
    public String register(@RequestBody RegistrationRequest request) {
        return accessService.register(request);
    }

    @Operation(summary = "Verify email")
    @GetMapping(path = "/confirm")
    public String verify(@RequestParam("token") String token) {
        return accessService.confirmToken(token);
    }

    @Operation(summary = "Sign in")
    @GetMapping("/login")
    public String login() {
        return "Login";
    }

}
