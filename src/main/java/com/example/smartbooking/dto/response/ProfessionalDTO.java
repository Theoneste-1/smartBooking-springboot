package com.example.smartbooking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalDTO {
    private Long id;
    private String businessName;
    private String businessAddress;
    private String businessPhone;
    private String businessEmail;
    private String professionalTitle;
    private Integer yearsOfExperience;
    private Double hourlyRate;
    private boolean isVerified;
}
