package DomainModel.PatientAdministration;

import DomainModel.AppointmentAdministration.Appointment;
import DomainModel.ConsultationAdministration.Consultation;
import DomainModel.UserAdministration.Doctor;
import DomainModel.UserAdministration.Doctor.Gender;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;


@XmlRootElement
@Entity
@Table(name = "PATIENTS")
public class Patient {
    private UUID patientId;
    private String patientFirstName,
                   patientLastName,
                   patientProfession,
                   patientPhoneNumber,
                   patientEmail,
                   patientAddress,
                   patientInsuranceCard;
    private Gender patientGender;
    private LocalDate patientBirthday;
    private Doctor doctor;
    private List<Disease> diseases = new ArrayList<Disease>();
    private List<Antecedent> antecedents = new ArrayList<Antecedent>();
    private List<Analysis> analysies = new ArrayList<Analysis>();
    private List<Media> medias = new ArrayList<Media>();
    private List<Consultation> consultations = new ArrayList<Consultation>();
    private List<Appointment> appointments = new ArrayList<Appointment>();
    
    @Id
    @Column(name = "PATIENT_ID")
    @GeneratedValue(generator = "patientIdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "patientIdGenerator", strategy = "uuid2")
    @XmlAttribute
    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    @Basic
    @Column(name = "PATIENT_FIRST_NAME")
    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    @Basic
    @Column(name = "PATIENT_LAST_NAME")
    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    @Basic
    @Column(name = "PATIENT_GENDER")
    public Gender getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(Gender patientGender) {
        this.patientGender = patientGender;
    }

    @Basic
    @Column(name = "PATIENT_PROFESSION")
    public String getPatientProfession() {
        return patientProfession;
    }

    public void setPatientProfession(String patientProfession) {
        this.patientProfession = patientProfession;
    }

    @Basic
    @Column(name = "PATIENT_PHONE_NUMBER")
    public String getPatientPhoneNumber() {
        return patientPhoneNumber;
    }

    public void setPatientPhoneNumber(String patientPhoneNumber) {
        this.patientPhoneNumber = patientPhoneNumber;
    }

    @Basic
    @Column(name = "PATIENT_EMAIL")
    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    @Basic
    @Column(name = "PATIENT_ADDRESS")
    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }

    @Basic
    @Column(name = "PATIENT_INSURANCE_CARD_NUMBER")
    public String getPatientInsuranceCard() {
        return patientInsuranceCard;
    }

    public void setPatientInsuranceCard(String patientInsuranceCard) {
        this.patientInsuranceCard = patientInsuranceCard;
    }
    
    @Basic
    @Column(name = "PATIENT_BIRTHDAY")
    public LocalDate getPatientBirthday(){
        return this.patientBirthday;
    }
    
    public void setPatientBirthday(LocalDate patientBirthday){
        this.patientBirthday = patientBirthday;
    }

    @Basic
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DOCTOR_ID")
    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    
    @Basic
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "DISEASE_PATIENT",
        joinColumns = @JoinColumn(name = "PATIENT_ID"),
        inverseJoinColumns = @JoinColumn(name = "DISEASE_ID")
    )
    public List<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<Disease> diseases) {
        this.diseases = diseases;
    }
    
    public void addDisease(Disease disease){
        this.diseases.add(disease);
    }
    
    public void removeDisease(Disease disease){
        this.diseases.remove(disease);
    }
    
    @Basic
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Antecedent> getAntecedents() {
        return antecedents;
    }

    public void setAntecedents(List<Antecedent> antecedents) {
        this.antecedents = antecedents;
    }
    
    public void addAntecedent(Antecedent antecedent){
        this.antecedents.add(antecedent);
        antecedent.setPatient(this);
    }
    
    public void removeAntecedent(Antecedent antecedent){
        this.antecedents.remove(antecedent);
        antecedent.setPatient(null);
    }
    
    @Basic
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient", cascade = CascadeType.ALL)
    public List<Analysis> getAnalysies() {
        return analysies;
    }

    public void setAnalysies(List<Analysis> analysies) {
        this.analysies = analysies;
    }
    
    public void addAnalysis(Analysis analysis){
        this.analysies.add(analysis);
        analysis.setPatient(this);
    }
    
    public void removeAnalysis(Analysis analysis){
        this.analysies.remove(analysis);
        analysis.setPatient(null);
    }
    
    @Basic
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient", cascade = CascadeType.ALL)
    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    public void addMedia(Media media){
        this.medias.add(media);
        media.setPatient(this);
    }
    
    public void removeMedia(Media media){
        this.medias.remove(media);
        media.setPatient(null);
    }
    
    @Basic
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient", cascade = CascadeType.ALL)
    public List<Consultation> getConsultations() {
        return consultations;
    }

    public void setConsultations(List<Consultation> consultations) {
        this.consultations = consultations;
    }

    public void addConsultation(Consultation consultation){
        this.consultations.add(consultation);
        consultation.setPatient(this);
    }
    
    public void removeConsultation(Consultation consultation){
        this.consultations.remove(consultation);
        consultation.setPatient(null);
    }
    
    @Basic
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient", cascade = CascadeType.ALL)
    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public void addAppointment(Appointment appointment){
        this.appointments.add(appointment);
        appointment.setPatient(this);
    }
    
    public void removeAppointment(Appointment appointment){
        this.appointments.remove(appointment);
        appointment.setPatient(null);
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Patient)
            return this.getPatientAddress().equals(((Patient) o).getPatientAddress()) &&
                   this.getPatientEmail().equals(((Patient) o).getPatientEmail()) &&
                   this.getPatientFirstName().equals(((Patient) o).getPatientFirstName()) &&
                   this.getPatientGender().equals(((Patient) o).getPatientGender()) &&
                   this.getPatientInsuranceCard().equals(((Patient) o).getPatientInsuranceCard()) &&
                   this.getPatientLastName().equals(((Patient) o).getPatientLastName()) &&
                   this.getPatientPhoneNumber().equals(((Patient) o).getPatientPhoneNumber()) &&
                   this.getPatientProfession().equals(((Patient) o).getPatientProfession());
        return false;
    }
}
