package com.munzii.programming.dmaker.service;

import com.munzii.programming.dmaker.dto.CreateDeveloper;
import com.munzii.programming.dmaker.dto.DeveloperDetailDto;
import com.munzii.programming.dmaker.entity.Developer;
import com.munzii.programming.dmaker.exception.DMakerErrorCode;
import com.munzii.programming.dmaker.exception.DMakerException;
import com.munzii.programming.dmaker.repository.DeveloperRepository;
import com.munzii.programming.dmaker.repository.RetiredDeveloperRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.munzii.programming.dmaker.code.StatusCode.EMPLOYED;
import static com.munzii.programming.dmaker.exception.DMakerErrorCode.DUPLICATED_MEMBER_ID;
import static com.munzii.programming.dmaker.type.DeveloperLevel.SENIOR;
import static com.munzii.programming.dmaker.type.DeveloperSkillType.FRONT_END;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DMakerServiceTest {

    @Mock
    private DeveloperRepository developerRepository;

    @Mock
    private RetiredDeveloperRepository retiredDeveloperRepository;

    @InjectMocks
    private DMakerService dMakerService;

    private final Developer defaultDeveloper = Developer.builder()
            .developerLevel(SENIOR)
            .developerSkillType(FRONT_END)
            .experienceYears(12)
            .statusCode(EMPLOYED)
            .name("name")
            .age(12)
            .build();

    private final CreateDeveloper.Request defaultCreateRequest = CreateDeveloper.Request.builder()
            .developerLevel(SENIOR)
            .developerSkillType(FRONT_END)
            .experienceYears(12)
            .memberId("memberId")
            .name("name")
            .age(32)
            .build();

    @Test
    public void testSomething() {
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        DeveloperDetailDto developerDetail = dMakerService.getDeveloperDetail("memberId");

        assertEquals(SENIOR, developerDetail.getDeveloperLevel());
        assertEquals(FRONT_END, developerDetail.getDeveloperSkillType());
        assertEquals(12, developerDetail.getExperienceYears());
    }

    @Test
    void createDevelopertest_success() {
        given(developerRepository.findByMemberId(anyString())).willReturn(Optional.ofNullable(null));
        ArgumentCaptor<Developer> captor = ArgumentCaptor.forClass(Developer.class);

        CreateDeveloper.Response developer = dMakerService.createDeveloper(defaultCreateRequest);

        verify(developerRepository, times(1)).save(captor.capture());
        Developer savedDeveloper = captor.getValue();
        assertEquals(SENIOR, savedDeveloper.getDeveloperLevel());
        assertEquals(FRONT_END, savedDeveloper.getDeveloperSkillType());
        assertEquals(12, savedDeveloper.getExperienceYears());

        dMakerService.createDeveloper(defaultCreateRequest);
    }

    @Test
    void createDevelopertest_failed_with_duplicated() {
        given(developerRepository.findByMemberId(anyString())).willReturn(Optional.ofNullable(null));

        DMakerException dMakerException = assertThrows(DMakerException.class, () ->
                dMakerService.createDeveloper(defaultCreateRequest));

        assertEquals(DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());
    }

}