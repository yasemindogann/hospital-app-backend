package com.lenora.service;

import com.lenora.repository.ExaminationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExaminationService {

    private final ExaminationRepository examinationRepository;

}
