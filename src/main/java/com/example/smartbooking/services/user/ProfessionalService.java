package com.example.smartbooking.services.user;

import com.example.smartbooking.dto.response.AvailabilityResponse;
import com.example.smartbooking.dto.response.CategoryResponseDto;
import com.example.smartbooking.dto.response.ProfessionalDTO;
import com.example.smartbooking.models.*;
import com.example.smartbooking.repositories.ProfessionalRepository;
import com.example.smartbooking.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;

    private final UserRepository userRepository;

    public ProfessionalService(ProfessionalRepository professionalRepository, UserRepository userRepository) {
        this.professionalRepository = professionalRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Professional createProfessional(Professional professional) {
        // Get the currently logged-in user's authentication details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Assuming username is used to identify the Admin

        // Fetch the Admin entity based on the username (or another identifier)
        User user  = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Admin not found for username: " + username));

        // Set the Admin to the Professional entity
        professional.setUser(user);

        // Save the Professional entity
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



    public ProfessionalDTO getProfessionalDTOById(Long id) {
        Professional professional = professionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professional not found with ID: " + id));
        return mapToDTO(professional);
    }

    public Professional getSingleProfessional(Long id) {
        return professionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professional not found with ID: " + id));
    }

    public List<Professional> getVerifiedProfessionals() {
        return professionalRepository.findByIsVerified(true);
    }

    public Professional getProfessionalByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(()-> new RuntimeException("User not found"));
        return professionalRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Professional not found for Admin ID: " + user.getId()));
    }


    @Transactional
    public void verifyProfessional(Long id) {
        Professional professional = professionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professional not found for ID: " + id));
        professional.setVerified(true);
        professionalRepository.save(professional);
    }

  public Set<AvailabilityResponse> getAvailabilitiesByProfessionalId(Long professionalId) {
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new RuntimeException("Professional not found for ID: " + professionalId));

        return professional.getAvailabilities().stream()
                .map(this::toAvailabilityResponse)
                .collect(Collectors.toSet());
    }

    public Set<CategoryResponseDto> getCategoriesByProfessionalId(Long professionalId) {
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new RuntimeException("Professional not found for ID: " + professionalId));

        return professional.getCategories().stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toSet());
    }

    public Set<Appointment> getAppointmentsByProfessionalId(Long professionalId) {
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new RuntimeException("Professional not found for ID: " + professionalId));
        return professional.getAppointments();
    }



    public List<Professional> getAvailableProfessionals() {
        return professionalRepository.findAll();
    }


    public List<ProfessionalDTO> getAllProfessionalDTOs() {
        List<Professional> professionals = professionalRepository.findAll();
        return professionals.stream().map(this::mapToDTO).toList();
    }

    private ProfessionalDTO mapToDTO(Professional professional) {
        return new ProfessionalDTO(
                professional.getId(),
                professional.getBusinessName(),
                professional.getBusinessAddress(),
                professional.getBusinessPhone(),
                professional.getBusinessEmail(),
                professional.getProfessionalTitle(),
                professional.getYearsOfExperience(),
                professional.getHourlyRate(),
                professional.isVerified()
        );
    }


    // Mappers
    private AvailabilityResponse toAvailabilityResponse(Availability availability) {
        return new AvailabilityResponse(
                availability.getId(),
                availability.getProfessional().getId(),
                availability.getDayOfWeek(),
                availability.getStartTime(),
                availability.getEndTime(),
                availability.isAvailable()
        );
    }

    private CategoryResponseDto toCategoryResponse(Category category) {
        return new CategoryResponseDto(category.getId(), category.getName(), category.getDescription(), category.isActive(), category.getProfessional().getId(), category.getProfessional().getBusinessName());
    }
}