package com.munzii.programming.dmaker.controller;

import com.munzii.programming.dmaker.dto.DeveloperDto;
import com.munzii.programming.dmaker.service.DMakerService;
import com.munzii.programming.dmaker.type.DeveloperLevel;
import com.munzii.programming.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@WebMvcTest(DMakerController.class)
class DMakerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DMakerService dMakerService;

    @Test
    void getAllDevelopers() throws Exception {
        DeveloperDto juniorDeveloperDto = DeveloperDto.builder()
                .developerSkillType(DeveloperSkillType.BACK_END)
                .developerLevel(DeveloperLevel.JUNIOR)
                .memberId("memberId1").build();
        DeveloperDto seniorDeveloperDto = DeveloperDto.builder()
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .developerLevel(DeveloperLevel.SENIOR)
                .memberId("memberId2").build();
        given(dMakerService.getAllEmployedDevelopers())
                .willReturn(Arrays.asList(juniorDeveloperDto, seniorDeveloperDto));
    }


}