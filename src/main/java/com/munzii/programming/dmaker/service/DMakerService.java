package com.munzii.programming.dmaker.service;

import com.munzii.programming.dmaker.dto.CreateDeveloper;
import com.munzii.programming.dmaker.dto.DeveloperDetailDto;
import com.munzii.programming.dmaker.dto.DeveloperDto;
import com.munzii.programming.dmaker.dto.EditDeveloper;
import com.munzii.programming.dmaker.entity.Developer;
import com.munzii.programming.dmaker.exception.DMakerErrorCode;
import com.munzii.programming.dmaker.exception.DMakerException;
import com.munzii.programming.dmaker.repository.DeveloperRepository;
import com.munzii.programming.dmaker.type.DeveloperLevel;
import com.munzii.programming.dmaker.type.DeveloperSkillType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.munzii.programming.dmaker.exception.DMakerErrorCode.*;

@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;

    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
        validateCreateDeveloperRequest(request);

        Developer developer = Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .name(request.getName())
                .age(request.getAge())
                .build();

        developerRepository.save(developer);
        return CreateDeveloper.Response.fromEntity(developer);
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        DeveloperLevel developerLevel = request.getDeveloperLevel();
        Integer experienceYears = request.getExperienceYears();

        if(request.getDeveloperLevel() == DeveloperLevel.SENIOR && request.getExperienceYears() <10) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if(request.getDeveloperLevel() == DeveloperLevel.JUNGNIOR && (request.getExperienceYears() <4 || request.getExperienceYears() >10)) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if(request.getDeveloperLevel() == DeveloperLevel.JUNIOR && request.getExperienceYears() >4) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }

        developerRepository.findByMemberId(request.getMemberId()).ifPresent((developer -> {throw new DMakerException(DUPLICATED_MEMBER_ID);
        }));
    }

    public List<DeveloperDto> getAllDevelopers() {
        return developerRepository.findAll().stream().map(DeveloperDto::fromEntity).collect(Collectors.toList());
    }

    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        return developerRepository.findByMemberId(memberId)
                .map(DeveloperDetailDto::fromEntity).orElseThrow(() -> new DMakerException(NO_DEVELOPER));
    }

    public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request) {
        validateEditDeveloperRequest(request);
    }

    private void validateEditDeveloperRequest(EditDeveloper.Request request) {
        DeveloperLevel developerLevel = request.getDeveloperLevel();
        Integer experienceYears = request.getExperienceYears();

    }
}
