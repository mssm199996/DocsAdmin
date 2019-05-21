package DomainModel.AppointmentAdministration;

import DomainModel.PatientAdministration.Patient;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "APPOINTMENTS")
@XmlRootElement
public class Appointment implements Serializable {
    private UUID appointmentId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private Patient patient;
    private List<Paraclinic> paraclinics = new ArrayList<Paraclinic>();
    private List<Reason> reasons = new ArrayList<Reason>();
    
    @Id
    @Column(name = "APPOINTMENT_ID")
    @GeneratedValue(generator = "appointmentIdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "appointmentIdGenerator", strategy = "uuid2")
    @XmlAttribute
    public UUID getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(UUID appointmentId) {
        this.appointmentId = appointmentId;
    }

    @Basic
    @Column(name = "APPOINTMENT_DAY")
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    @Basic
    @Column(name = "APPOINTMENT_TIME")
    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmeentTime) {
        this.appointmentTime = appointmeentTime;
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
    @OneToMany(mappedBy = "appointment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Paraclinic> getParaclinics() {
        return paraclinics;
    }

    public void setParaclinics(List<Paraclinic> paraclinics) {
        this.paraclinics = paraclinics;
    }
    
    public void addParaclinic(Paraclinic paraclinic){
        this.paraclinics.add(paraclinic);
        paraclinic.setAppointment(this);
    }
    
    public void removeParaclinic(Paraclinic paraclinic){
        this.paraclinics.remove(paraclinic);
        paraclinic.setAppointment(null);
    }

    @Basic
    @OneToMany(mappedBy = "appointment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Reason> getReasons() {
        return reasons;
    }

    public void setReasons(List<Reason> reasons) {
        this.reasons = reasons;
    }
    
    public void addReason(Reason reason){
        this.reasons.add(reason);
        reason.setAppointment(this);
    }
    
    public void removeReason(Reason reason){
        this.reasons.remove(this);
        reason.setAppointment(null);
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Appointment)
            return this.getAppointmentTime().equals(((Appointment) o).getAppointmentTime()) &&
                   this.getAppointmentDate().equals(((Appointment) o).getAppointmentDate()) &&
                   this.getPatient().equals(((Appointment) o).getPatient());
        return false;
    }
}
