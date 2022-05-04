package com.munzii.programming.dmaker.service;

import com.munzii.programming.dmaker.code.StatusCode;
import com.munzii.programming.dmaker.dto.CreateDeveloper;
import com.munzii.programming.dmaker.dto.DeveloperDetailDto;
import com.munzii.programming.dmaker.dto.DeveloperDto;
import com.munzii.programming.dmaker.dto.EditDeveloper;
import com.munzii.programming.dmaker.entity.Developer;
import com.munzii.programming.dmaker.entity.RetiredDeveloper;
import com.munzii.programming.dmaker.exception.DMakerException;
import com.munzii.programming.dmaker.repository.DeveloperRepository;
import com.munzii.programming.dmaker.repository.RetiredDeveloperRepository;
import com.munzii.programming.dmaker.type.DeveloperLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.munzii.programming.dmaker.exception.DMakerErrorCode.*;

@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;
    private final RetiredDeveloperRepository retiredDeveloperRepository;

    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
        validateCreateDeveloperRequest(request);

        Developer developer = Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .memberId(request.getMemberId())
                .statusCode(StatusCode.EMPLOYED)
                .name(request.getName())
                .age(request.getAge())
                .build();

        developerRepository.save(developer);
        return CreateDeveloper.Response.fromEntity(developer);
    }

    private void validateCreateDeveloperRequest(@NonNull CreateDeveloper.Request request) {
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent((developer -> {throw new DMakerException(DUPLICATED_MEMBER_ID);
        }));

        throw new ArrayIndexOutOfBoundsException();
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<DeveloperDto> getAllEmployedDevelopers() {
        return developerRepository.findDevelopersByStatusCodeEquals(StatusCode.EMPLOYED)
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        return developerRepository.findByMemberId(memberId)
                .map(DeveloperDetailDto::fromEntity).orElseThrow(() -> new DMakerException(NO_DEVELOPER));
    }

    @Transactional
    public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request) {
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new DMakerException(NO_DEVELOPER));

        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return DeveloperDetailDto.fromEntity(developer);

    }

    private void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {
        if(developerLevel == DeveloperLevel.SENIOR && experienceYears <10) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if(developerLevel == DeveloperLevel.JUNGNIOR && (experienceYears <4 || experienceYears >10)) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if(developerLevel == DeveloperLevel.JUNIOR && experienceYears >4) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }

    @Transactional
    public DeveloperDetailDto deleteDevelope(String memberId) {
        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new DMakerException(NO_DEVELOPER));
        developer.setStatusCode(StatusCode.RETIRED);

        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
                .memberId(memberId)
                .name(developer.getName())
                .build();
        retiredDeveloperRepository.save(retiredDeveloper);
        return DeveloperDetailDto.fromEntity(developer);

    }
}
