package DomainModel.ConsultationAdministration;

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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;
import DomainModel.MedecineAdministration.Medecine;
import DomainModel.PatientAdministration.Patient;

@XmlRootElement
@Entity
@Table(name = "CONSULTATIONS")
public class Consultation {
    private UUID consultationId;
    private LocalDate consultationDate;
    private String consultationAnamnesis,
                   consultationPhex,
                   consultationDiag;
    private double consultationPrice;
    private Patient patient;
    private List<Observation> observations = new ArrayList<Observation>();
    private List<Medecine> prescriptions = new ArrayList<Medecine>();

    @Id
    @Column(name = "CONSULTATION_ID")
    @GeneratedValue(generator = "consultationIdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "consultationIdGenerator", strategy = "uuid2")
    @XmlAttribute
    public UUID getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(UUID consultationId) {
        this.consultationId = consultationId;
    }

    @Basic
    @Column(name = "CONSULTATION_DATE")
    @XmlElement
    public LocalDate getConsultationDate() {
        return consultationDate;
    }

    public void setConsultationDate(LocalDate consultationDate) {
        this.consultationDate = consultationDate;
    }

    @Basic
    @Column(name = "CONSULTATION_ANAMNESIS")
    public String getConsultationAnamnesis() {
        return consultationAnamnesis;
    }

    public void setConsultationAnamnesis(String consultationAnamnesis) {
        this.consultationAnamnesis = consultationAnamnesis;
    }
    
    @Basic
    @Column(name = "CONSULTATION_PHEX")
    public String getConsultationPhex() {
        return consultationPhex;
    }

    public void setConsultationPhex(String consultationPhex) {
        this.consultationPhex = consultationPhex;
    }

    @Basic
    @Column(name = "CONSULTATION_DIAG")
    public String getConsultationDiag() {
        return consultationDiag;
    }

    public void setConsultationDiag(String consultationDiag) {
        this.consultationDiag = consultationDiag;
    }

    @Basic
    @Column(name = "CONSULTATION_PRICE")
    public double getConsultationPrice() {
        return consultationPrice;
    }

    public void setConsultationPrice(double consultationPrice) {
        this.consultationPrice = consultationPrice;
    }

    @Basic
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PATIENT_ID")
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    
    @Basic
    @OneToMany(mappedBy = "consultation", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    public List<Observation> getObservations() {
        return observations;
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
    }
    
    public void addObservation(Observation observation){
        this.observations.add(observation);
        observation.setConsultation(this);
    }
    
    public void removeObservation(Observation observation){
        this.observations.remove(observation);
        observation.setConsultation(null);
    }
    
    @Basic
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "PRESCRIPTIONS",
        joinColumns = @JoinColumn(name = "CONSULTATION_ID"),
        inverseJoinColumns = @JoinColumn(name = "MEDECINE_ID")
    )
    public List<Medecine> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Medecine> prescriptions) {
        this.prescriptions = prescriptions;
    }
    
    public void addPrescription(Medecine prescription){
        this.prescriptions.add(prescription);
    }
    
    public void removePrescription(Medecine prescription){
        this.prescriptions.remove(prescription);
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Consultation)
            return this.getConsultationAnamnesis().equals(((Consultation) o).getConsultationAnamnesis()) &&
                   this.getConsultationDate().equals(((Consultation) o).getConsultationDate()) &&
                   this.getConsultationDiag().equals(((Consultation) o).getConsultationDiag()) &&
                   this.getConsultationPhex().equals(((Consultation) o).getConsultationPhex()) &&
                   this.getConsultationPrice() == ((Consultation) o).getConsultationPrice();
        return false;
    }
}
