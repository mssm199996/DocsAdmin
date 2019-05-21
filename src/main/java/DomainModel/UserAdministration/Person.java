package DomainModel.UserAdministration;

import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Person {
    private UUID personId;
    private String personFirstName,
                   personLastName,
                   personPhoneNumber;
    private Account personAccount;

    @Id
    @Column(name = "PERSON_ID")
    @GeneratedValue(generator = "personIdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "personIdGenerator", strategy = "uuid2")
    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
    }

    @Basic
    @Column(name = "PERSON_FIRST_NAME")
    public String getPersonFirstName() {
        return personFirstName;
    }

    public void setPersonFirstName(String personFirstName) {
        this.personFirstName = personFirstName;
    }

    @Basic
    @Column(name = "PERSON_LAST_NAME")
    public String getPersonLastName() {
        return personLastName;
    }

    public void setPersonLastName(String personLastName) {
        this.personLastName = personLastName;
    }

    @Basic
    @Column(name = "PERSON_PHONE_NUMBER")
    public String getPersonPhoneNumber() {
        return personPhoneNumber;
    }

    public void setPersonPhoneNumber(String personPhoneNumber) {
        this.personPhoneNumber = personPhoneNumber;
    }
    
    @Basic
    @OneToOne(mappedBy = "person", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Account getPersonAccount() {
        return personAccount;
    }

    public void setPersonAccount(Account personAccount) {
        this.personAccount = personAccount;
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Person)
            return this.getPersonFirstName().equals(((Person) o).getPersonFirstName()) &&
                   this.getPersonLastName().equals(((Person) o).getPersonLastName()) &&
                   this.getPersonPhoneNumber().equals(((Person) o).getPersonPhoneNumber());
        return false;
    }
}
