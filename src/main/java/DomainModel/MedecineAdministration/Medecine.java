package DomainModel.MedecineAdministration;

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

@XmlRootElement(name = "medecine")
@Entity
@Table(name = "MEDECINES")
public class Medecine {
    private int medecineId;
    private String medecineDci,
                   medecineMark,
                   medecineForm,
                   medecineDosage;
    private boolean medecineRedeemability;

    @Id
    @Column(name = "MEDECINE_ID")
    @GeneratedValue(generator = "medecineIdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "medecineIdGenerator", strategy = "increment")
    @XmlAttribute
    public int getMedecineId() {
        return medecineId;
    }

    public void setMedecineId(int medecineId) {
        this.medecineId = medecineId;
    }

    @Basic
    @Column(name = "MEDECINE_DCI")
    public String getMedecineDci() {
        return medecineDci;
    }

    public void setMedecineDci(String medecineDci) {
        this.medecineDci = medecineDci;
    }

    @Basic
    @Column(name = "MEDECINE_MARK")
    public String getMedecineMark() {
        return medecineMark;
    }

    public void setMedecineMark(String medecineMark) {
        this.medecineMark = medecineMark;
    }

    @Basic
    @Column(name = "MEDECINE_FORM")
    public String getMedecineForm() {
        return medecineForm;
    }

    public void setMedecineForm(String medecineForm) {
        this.medecineForm = medecineForm;
    }

    @Basic
    @Column(name = "MEDECINE_DOSAGE")
    public String getMedecineDosage() {
        return medecineDosage;
    }

    public void setMedecineDosage(String medecineDosage) {
        this.medecineDosage = medecineDosage;
    }

    @Basic
    @Column(name = "MEDECINE_REDEEMABILITY")
    public boolean isMedecineRedeemability() {
        return medecineRedeemability;
    }

    public void setMedecineRedeemability(boolean medecineRedeemability) {
        this.medecineRedeemability = medecineRedeemability;
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Medecine)
            return this.getMedecineDci().equals(((Medecine) o).getMedecineDci()) &&
                   this.getMedecineDosage().equals(((Medecine) o).getMedecineDosage()) &&
                   this.getMedecineForm().equals(((Medecine) o).getMedecineForm()) &&
                   this.getMedecineMark().equals(((Medecine) o).getMedecineMark());
        return false;
    }
    
    @Override
    public String toString(){
        return "DCI: " + this.getMedecineDci() + "\n" +
               "Name: " + this.getMedecineMark() + "\n" +
               "Dosage: " + this.getMedecineDosage() + "\n" +
               "Form: " + this.getMedecineForm() + "\n" +
               "Remb: " + this.isMedecineRedeemability() + "\n";
    }
}
