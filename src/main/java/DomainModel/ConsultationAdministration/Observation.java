package DomainModel.ConsultationAdministration;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@Entity
@Table(name = "OBSERVATIONS")
public class Observation implements Serializable {
    private Consultation consultation;
    private String description;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CONSULTATION_ID")
    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    @Id
    @Column(name = "OBSERVATION_DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String desccription) {
        this.description = desccription;
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Observation)
            return this.getConsultation().equals(((Observation) o).getConsultation()) &&
                   this.getDescription().equals(((Observation) o).getDescription());
        return false;
    }
}
