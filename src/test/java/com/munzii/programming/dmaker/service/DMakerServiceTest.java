package com.munzii.programming.dmaker.service;

import com.munzii.programming.dmaker.dto.DeveloperDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DMakerServiceTest {

    @Autowired
    private DMakerService dMakerService;

    @Test
    public void testSomething() {
        List<DeveloperDto> allEmployedDevelopers = dMakerService.getAllEmployedDevelopers();
        System.out.println(allEmployedDevelopers);

    }

}