package ptit.hospitalmanagementsystem.service;

import org.springframework.stereotype.Service;
import ptit.hospitalmanagementsystem.dto.request.PatientCreationRequest;
import ptit.hospitalmanagementsystem.dto.respond.PatientCreationResponse;
import ptit.hospitalmanagementsystem.repository.PatientRepository;

@Service
public class PatientService {
    PatientRepository patientRepository;
    PatientCreationResponse createPatient(PatientCreationRequest request){
        if(patientRepository.existsBy)

    }
    updatePatient()
    deletePatient()

}
