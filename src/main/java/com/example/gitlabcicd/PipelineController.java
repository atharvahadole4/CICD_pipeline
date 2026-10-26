package com.example.gitlabcicd;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@RestController
public class PipelineController {

    @GetMapping("/")
    public String home() {
        return "GitLab CI/CD home program";
    }

    @GetMapping("/pipeline/{action}")
    public String pipelineAction(@PathVariable String action) {
        return switch (action.toLowerCase(Locale.ROOT)) {
            case "start" -> "CI/CD pipeline started";
            case "stop" -> "CI/CD pipeline stopped";
            case "hold" -> "CI/CD pipeline on hold";
            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Action must be start, stop, or hold");
        };
    }
}
