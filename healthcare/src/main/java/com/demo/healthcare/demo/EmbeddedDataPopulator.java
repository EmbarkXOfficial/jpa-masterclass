package com.demo.healthcare.demo;

import com.demo.healthcare.model.Address;
import com.demo.healthcare.model.Patient;
import com.demo.healthcare.repository.DoctorRepository;
import com.demo.healthcare.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EmbeddedDataPopulator implements CommandLineRunner {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public EmbeddedDataPopulator(DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Address address = new Address("101 Main St.", "SpringField", "IL", "873643");
        Patient patient = new Patient("John", 30);
        patient.setAddress(address);
        patientRepository.save(patient);

    }
}
