package DomainModel.PatientAdministration;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "ANTECEDENTS")
@XmlRootElement(name = "antecedant")
public class Antecedent implements Serializable {
    private AntecedentType antecedentType;
    private String antecedentDescription;
    private LocalDate antecedentStartDate,
                      antecedentRecoveryDate;
    private Patient patient;
    
    @Id
    @Basic
    @Column(name = "ANTECEDENT_TYPE")
    public AntecedentType getAntecedentType() {
        return antecedentType;
    }

    public void setAntecedentType(AntecedentType antecedentType) {
        this.antecedentType = antecedentType;
    }

    @Basic
    @Column(name = "ANTECEDENT_DESCRIPTION")
    public String getAntecedentDescription() {
        return antecedentDescription;
    }

    public void setAntecedentDescription(String antecedentDescription) {
        this.antecedentDescription = antecedentDescription;
    }

    @Id
    @Column(name = "ANTECEDENT_START_DATE")
    public LocalDate getAntecedentStartDate() {
        return antecedentStartDate;
    }

    public void setAntecedentStartDate(LocalDate antecedentStartDate) {
        this.antecedentStartDate = antecedentStartDate;
    }

    @Id
    @Column(name = "ANTECEDENT_RECOVERY_DATE")
    public LocalDate getAntecedentRecoveryDate() {
        return antecedentRecoveryDate;
    }

    public void setAntecedentRecoveryDate(LocalDate antecedentRecoveryDate) {
        this.antecedentRecoveryDate = antecedentRecoveryDate;
    }

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PATIENT_ID")
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Antecedent)
            return this.getPatient().equals(((Antecedent) o).getPatient()) &&
                   this.getAntecedentStartDate().equals(((Antecedent) o).getAntecedentStartDate()) &&
                   this.getAntecedentRecoveryDate().equals(((Antecedent) o).getAntecedentRecoveryDate()) &&
                   this.getAntecedentType().equals(((Antecedent) o).getAntecedentType());
        return false;
    }
    
    public static enum AntecedentType {
        FAMILIAL,
        PERSONEL,
        MEDICAL,
        CHIRURGICAL
    }
}


