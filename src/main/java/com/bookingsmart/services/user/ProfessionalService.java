package com.bookingsmart.services.user;

import com.bookingsmart.models.Professional;
import com.bookingsmart.repositories.ProfessionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;

    public ProfessionalService(ProfessionalRepository professionalRepository) {
        this.professionalRepository = professionalRepository;
    }

    @Transactional
    public Professional createProfessional(Professional professional) {
        return professionalRepository.save(professional);
    }

    @Transactional
    public Professional updateProfessional(Long id, Professional professionalDetails) {
        Professional professional = professionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professional not found"));

        professional.setBusinessName(professionalDetails.getBusinessName());
        professional.setBusinessAddress(professionalDetails.getBusinessAddress());
        professional.setBusinessPhone(professionalDetails.getBusinessPhone());
        professional.setBusinessEmail(professionalDetails.getBusinessEmail());
        professional.setProfessionalTitle(professionalDetails.getProfessionalTitle());
        professional.setYearsOfExperience(professionalDetails.getYearsOfExperience());
        professional.setHourlyRate(professionalDetails.getHourlyRate());

        return professionalRepository.save(professional);
    }

    @Transactional
    public void verifyProfessional(Long id) {
        Professional professional = professionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professional not found"));

        professional.setVerified(true);
        professionalRepository.save(professional);
    }

    public Optional<Professional> getProfessionalById(Long id) {
        return professionalRepository.findById(id);
    }

    public List<Professional> getAllProfessionals() {
        return professionalRepository.findAll();
    }

    public List<Professional> getVerifiedProfessionals() {
        return professionalRepository.findByIsVerified(true);
    }
}