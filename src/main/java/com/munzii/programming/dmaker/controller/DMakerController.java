package com.munzii.programming.dmaker.controller;

import com.munzii.programming.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class DMakerController {
    private final DMakerService dMakerService;

    @GetMapping("/developers")
    public List<String> getAllDevelopers() {
        // GET /developers HTTP/1.1
        log.info("GET /developers HTTP/1.1");

        return Arrays.asList("snow", "elsa", "Olaf");
    }

    @GetMapping("/create-developer")
    public List<String> createDevelopers() {
        // GET /developers HTTP/1.1
        log.info("GET /create-developers HTTP/1.1");

        dMakerService.createdDeveloper();

        return Collections.singletonList("Olaf");
    }
}
