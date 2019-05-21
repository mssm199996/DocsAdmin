package DomainModel.UserAdministration;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "SPECIALITIES")
public class Speciality {
    private int specialityId;
    private String specialityName;

    @Id
    @Column(name = "SPECIALITY_ID")
    @GeneratedValue(generator = "specialityIdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "specialityIdGenerator", strategy = "increment")
    public int getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(int specialityId) {
        this.specialityId = specialityId;
    }

    @Basic
    @Column(name = "SPECIALITY_NAME")
    public String getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }    
}
