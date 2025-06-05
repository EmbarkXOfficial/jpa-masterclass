package com.demo.healthcare.demo.jpql;

import com.demo.healthcare.model.Doctor;
import com.demo.healthcare.model.Gender;
import com.demo.healthcare.model.MedicalRecord;
import com.demo.healthcare.model.Patient;
import com.demo.healthcare.repository.DoctorRepository;
import com.demo.healthcare.repository.MedicalRecordRepository;
import com.demo.healthcare.repository.PatientRepository;
import com.demo.healthcare.repository.PersonRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BasicJPQLQueries implements CommandLineRunner {

    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private PersonRepository personRepository;
    private MedicalRecordRepository medicalRecordRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public BasicJPQLQueries(DoctorRepository doctorRepository, PatientRepository patientRepository, PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Doctor doctor = new Doctor();
        doctor.setName("Dr. Smith");
        doctor.setAge(45);
        doctor.setEmail("smith@hospital.com");
        doctor.setSpecialization("Cardiology");
        doctorRepository.save(doctor);

        Patient johnDoe = new Patient(
                "John Doe",
                30,
                "john@email.com",
                Gender.MALE
        );
        patientRepository.save(johnDoe);

        johnDoe.setDoctor(doctor);

        MedicalRecord record = new MedicalRecord();
        record.setDiagnosis("Hypertension");
        record.setPatient(johnDoe);
        medicalRecordRepository.save(record);

        johnDoe.setMedicalRecord(record);
        patientRepository.save(johnDoe);


        Patient alice = new Patient(
                "Alice",
                38,
                "alice@email.com",
                Gender.FEMALE
        );
        patientRepository.save(alice);


        // JPQL QUERIES
        List<Patient> patients = entityManager.createQuery(
                "SELECT p FROM Patient p", Patient.class
        ).getResultList();

        System.out.println("Patients: " + patients.size());
        for (Patient p : patients)
            System.out.println("Patient Name: " + p.getName());

        // FILTERING
        List<Patient> malePatients = entityManager.createQuery(
                "SELECT p FROM Patient p WHERE p.gender = :gender",
                Patient.class
        ).setParameter("gender", Gender.MALE)
                .getResultList();

        System.out.println("MALE Patients: " + malePatients.size());
        for (Patient p : malePatients)
            System.out.println("MALE Patient Name: " + p.getName());
    }
}
