package com.munzii.programming.dmaker.controller;

import com.munzii.programming.dmaker.dto.*;
import com.munzii.programming.dmaker.exception.DMakerException;
import com.munzii.programming.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class DMakerController {
    private final DMakerService dMakerService;

    @GetMapping("/developers")
    public List<DeveloperDto> getAllDevelopers() {
        log.info("GET /developers HTTP/1.1");

        return dMakerService.getAllEmployedDevelopers();
    }

    @GetMapping("/developer/{memberId}")
    public DeveloperDetailDto getDeveloperDetail(
            @PathVariable final String memberId
    ) {
        log.info("GET /developers HTTP/1.1");

        return dMakerService.getDeveloperDetail(memberId);
    }

    @PostMapping("/create-developer")
    public CreateDeveloper.Response createDevelopers(
            @Valid @RequestBody final CreateDeveloper.Request request
    ) {
        log.info("request : {}", request);

        return dMakerService.createDeveloper(request);

    }

    @PutMapping("/developer/{memberId}")
    public DeveloperDetailDto editDeveloperDetail(
            @PathVariable final String memberId,
            @Valid @RequestBody final EditDeveloper.Request request
    ) {
        log.info("GET /developers HTTP/1.1");

        return dMakerService.editDeveloper(memberId, request);
    }

    @DeleteMapping("/developer/{memberId}")
    public DeveloperDetailDto deleteDeveloper(
        @PathVariable final String memberId
    ) {
        return dMakerService.deleteDevelope(memberId);
    }

    @ExceptionHandler(DMakerException.class)
    public DMakerErrorResponse handleException(DMakerException e, HttpServletRequest request) {
        log.error("errorCode: {}, url: {}, message: {}", e.getDMakerErrorCode(), request.getRequestURI(), e.getDetailMessage());

        return DMakerErrorResponse.builder().errorCode(e.getDMakerErrorCode())
                .errorMessage(e.getDetailMessage())
                .build();
    }
}
