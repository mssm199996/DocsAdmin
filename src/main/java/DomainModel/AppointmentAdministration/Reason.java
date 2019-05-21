package DomainModel.AppointmentAdministration;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "REASONS")
@XmlRootElement
public class Reason implements Serializable{
    private Appointment appointment;
    private String description;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "APPOINTMENT_ID")
    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    @Id
    @Column(name = "REASON_DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Reason)
            return this.getAppointment().equals(((Reason) o).getAppointment()) && this.getDescription().equals(((Reason) o).getDescription());
        return false;
    }
}
