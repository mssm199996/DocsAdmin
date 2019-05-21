package DomainModel.PatientAdministration;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "DISEASES")
@XmlRootElement(name = "disease")
public class Disease {
    private int diseaseId;
    private String diseaseName;

    @Id
    @Column(name = "DISEASE_ID")
    @GeneratedValue(generator = "diseaseIdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "diseaseIdGenerator", strategy = "increment")
    @XmlAttribute
    public int getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(int diseaseId) {
        this.diseaseId = diseaseId;
    }

    @Basic
    @Column(name = "DISEASE_NAME")
    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Disease)
            return this.getDiseaseName().equals(((Disease) o).getDiseaseName());
        return false;
    }
}
