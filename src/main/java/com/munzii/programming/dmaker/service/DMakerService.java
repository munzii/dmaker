package com.munzii.programming.dmaker.service;

import com.munzii.programming.dmaker.dto.CreateDeveloper;
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

import java.util.Optional;

import static com.munzii.programming.dmaker.exception.DMakerErrorCode.DUPLICATED_MEMBER_ID;
import static com.munzii.programming.dmaker.exception.DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED;

@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;

    @Transactional
    public void createdDeveloper(CreateDeveloper.Request request) {
        validateCreateDeveloperRequest(request);

        Developer developer = Developer.builder()
                .developerLevel(DeveloperLevel.JUNIOR)
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .experienceYears(2)
                .name("Olaf")
                .age(5)
                .build();

        developerRepository.save(developer);
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
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

}
