package DomainModel.UserAdministration;

import DomainModel.DocumentAdministration.Document;
import DomainModel.PatientAdministration.Patient;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "DOCTORS")
public class Doctor extends Person{
    private Gender doctorGender;
    private Speciality speciality;
    private List<Secretary> secretaries = new ArrayList<Secretary>();
    private List<Patient> patients = new ArrayList<Patient>();
    private List<Document> documents = new ArrayList<Document>();
    
    @Basic
    @Column(name = "DOCTOR_GENDER")
    public Gender getDoctorGender() {
        return doctorGender;
    }

    public void setDoctorGender(Gender doctorGender) {
        this.doctorGender = doctorGender;
    }

    @Basic
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DOCTOR_SPECIALITY_ID")
    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }
    
    @Basic
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "doctor", cascade = CascadeType.ALL)
    public List<Secretary> getSecretaries() {
        return secretaries;
    }

    public void setSecretaries(List<Secretary> secretaries) {
        this.secretaries = secretaries;
    }
    
    public void addSecretary(Secretary secretary){
        this.secretaries.add(secretary);
        secretary.setDoctor(this);
    }
    
    public void removeSecretary(Secretary secretary){
        this.secretaries.remove(secretary);
        secretary.setDoctor(null);
    }
    
    @Basic
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "doctor", cascade = CascadeType.ALL)
    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }
    
    public void addPatient(Patient patient){
        this.patients.add(patient);
        patient.setDoctor(this);
    }
    
    public void removePatient(Patient patient){
        this.patients.remove(patient);
        patient.setDoctor(null);
    }
    
    
    @Basic
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "doctor", cascade = CascadeType.ALL)
    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
    
    public void addDocument(Document document){
        this.documents.add(document);
        document.setDoctor(this);
    }
    
    public void removeDocument(Document document){
        this.patients.remove(document);
        document.setDoctor(null);
    }
    
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Doctor)
            return super.equals(o) && this.getSpeciality().equals(((Doctor) o).getSpeciality());
        return false;
    }
    
    public static enum Gender{
        MALE,
        FEMALE
    }
    
    @Override
    public String toString() {
    	return "@Doctor: " + this.getPersonAccount().getAccountEmail(); 
    }
}
