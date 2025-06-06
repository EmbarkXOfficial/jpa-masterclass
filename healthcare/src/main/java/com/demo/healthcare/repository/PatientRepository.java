package com.demo.healthcare.repository;

import com.demo.healthcare.demo.jpql.PatientSummary;
import com.demo.healthcare.model.Gender;
import com.demo.healthcare.model.Patient;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
//    @Query("SELECT p FROM Patient p WHERE p.name = :name")
    List<Patient> findByName(@Param("name") String name);

//    @Query("SELECT p FROM Patient p WHERE p.name = ?1 AND p.gender = ?2")
    List<Patient> findByNameAndGender(String name, Gender gender);

    boolean existsByEmail(String email);
    int countByAge(int age);

    // <> > < <= >= =
    @Query("SELECT p FROM Patient p WHERE p.age >= :minAge")
    List<Patient> findOrderThan(@Param("minAge") int age);

//    @Query("SELECT p FROM Patient p WHERE p.name NOT LIKE :prefix")
//    List<Patient> findByNameStartingWith(@Param("prefix") String prefix);

    @Query("SELECT p FROM Patient p WHERE p.gender NOT IN :genders")
    List<Patient> findByGenders(@Param("genders") List<Gender> genders);

    @Query("SELECT p FROM Patient p WHERE p.age BETWEEN :start AND :end")
    List<Patient> findByAgeRange(@Param("start") int start,
                                 @Param("end") int end);

    @Query("SELECT p FROM Patient p WHERE p.doctor is NOT NULL")
    List<Patient> findUnassignedPatients();

    // SORTING RESULTS
    @Query("SELECT p FROM Patient p ORDER BY p.age DESC, p.name ASC")
    List<Patient> sortByAge();

    @Query("SELECT p FROM Patient p WHERE p.gender = ?1 ORDER BY p.age DESC")
    List<Patient> findByGenderSortByAge(Gender gender);


    // JOINS
    @Query("SELECT p FROM Patient p INNER JOIN p.doctor d WHERE d.specialization = :spec")
    List<Patient> findPatientsWithDoctor(@Param("spec") String specialization);

    @Query("SELECT p FROM Patient p LEFT JOIN p.doctor d")
    List<Patient> findPatientsWithDoctorLeft();

    @Query("SELECT p FROM Patient p JOIN FETCH p.doctor d")
    List<Patient> findPatientsWithDoctorJoinFetch();


    // AGGREGATION
    @Query("SELECT p.gender, AVG(p.age) FROM Patient p GROUP BY p.gender")
    List<Object[]> averageAgeByGender();

    // BULK UPDATE / DELETE
    @Modifying
    @Transactional
    @Query("UPDATE Patient p SET p.age = p.age + 1")
    int bulkIncreaseAge();

    @Modifying
    @Transactional
    @Query("DELETE FROM Patient p WHERE p.age < ?1")
    int bulkDeleteByAge(int age);

    @Query("SELECT NEW com.demo.healthcare.demo.jpql.PatientSummary(p.name, p.age) FROM Patient p")
    List<PatientSummary> getPatientSummary();

    // NAMED QUERY
    List<Patient> findByNameStartingWith(@Param("prefix") String prefix);
}
