package DomainModel.UserAdministration;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;

@Entity
@Table(name = "SECRETARIES")
public class Secretary extends Person {

    private Doctor doctor;
    private boolean confirmed;
    
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
    @Column(name = "CONFIRMED")
    @XmlAttribute
    public boolean getConfirmed() {
        return confirmed;
    }
    public void setConfirmed(boolean confirmed) {
    	this.confirmed = confirmed;
    }
    
   
    @Override
    public boolean equals(Object o){
        if(o instanceof Secretary)
            return super.equals(o) && this.getDoctor().equals(((Secretary) o).getDoctor());
        return false;
    }
}
