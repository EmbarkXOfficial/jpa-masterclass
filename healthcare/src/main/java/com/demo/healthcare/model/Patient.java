package com.demo.healthcare.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
//@Table(name = "new_patients")
@NamedQuery(
        name = "Patient.findByNameStartingWith",
        query = "SELECT p FROM Patient p WHERE p.name NOT LIKE :prefix"
)
public class Patient extends Person{

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Transient
    private String ageGroup;

//    @Lob
//    private byte[] profilePicture;
    // CLOB - Character Large Object
//    BLOB - Binary Large Object

    @OneToOne (cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord;

    @ManyToOne( cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @OneToMany(mappedBy = "patientId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prescription> prescriptions;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

//    @Version
//    private int version;

    public Patient() {
    }

    public Patient(String name, int age, String email, Gender gender) {
        super(name, age, email);
        this.gender = gender;
    }

    public Patient(String name, int age) {
//        this.name = name;
//        this.age = age;
        this.ageGroup = calculateAgeGroup(age);
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    //    public byte[] getProfilePicture() {
//        return profilePicture;
//    }
//
//    public void setProfilePicture(byte[] profilePicture) {
//        this.profilePicture = profilePicture;
//    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getAge() {
//        return age;
//    }

    public void setAge(int age) {
//        this.age = age;
        this.ageGroup = calculateAgeGroup(age);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    private String calculateAgeGroup(int age) {
        if (age <= 12) return "Child";
        else if (age <= 19) return "Teen";
        else if (age <= 59) return "Adult";
        else return "Senior";
    }
}
