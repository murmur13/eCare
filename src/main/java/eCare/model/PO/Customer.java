package eCare.model.PO;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by echerkas on 18.10.2017.
 */

@Entity
@Table(name = "customer")
public class Customer{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @NotEmpty
    @Column(name="sso_id", unique=true, nullable=false)
    private String ssoId;

    @NotEmpty
    @Column(name = "name")
    private String name;

    @NotEmpty
    @Column(name = "surname")
    private String surname;

    @NotEmpty
    @Column(name = "birth_date")
    private String birthDate;

    @Column(name = "passport_data")
    private String passportData;

    @Column(name = "address")
    private String address;

    @Column(name = "tel_number")
    private String telNumber;

    @NotEmpty
    @Column(name = "mail")
    private String mail;

    @NotEmpty
    @Column(name = "password")
    private String password;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="customer")
    private List<Contract> contracts;

    public String getSsoId() {
        return ssoId;
    }

    public void setSsoId(String ssoId) {
        this.ssoId = ssoId;
    }

    public Set<UserProfile> getUserProfiles() {
        return userProfiles;
    }

    public void setUserProfiles(Set<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }

//    @SuppressWarnings("JpaAttributeTypeInspection")

    @NotEmpty
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="customer_has_user_profile",
            joinColumns=@JoinColumn(name="user_id", referencedColumnName="customer_id"),
            inverseJoinColumns=@JoinColumn(name="user_profile_id", referencedColumnName="profile_id"))
    private Set<UserProfile> userProfiles = new HashSet<UserProfile>();

    public Customer(String name, String surname, String birthDate, String telNumber, String mail, String password) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.telNumber = telNumber;
        this.mail = mail;
        this.password = password;
    }

    public Customer(String name, String surname, String birthDate, String mail, String password) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.mail = mail;
        this.password = password;
    }

    public Customer() {
    }

    public Customer(String name, String surname, String telNumber, String birthDate) {
        this.name = name;
        this.surname = surname;
        this.telNumber = telNumber;
        this.birthDate = birthDate;
    }

    public Customer(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public Customer(String mail, String password, String name) {
        this.mail = mail;
        this.password = password;
        this.name = name;
    }

    public Integer getId() {
        return customerId;
    }

    public void setId(Integer id) {
        this.customerId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassportData() {
        return passportData;
    }

    public void setPassportData(String passportData) {
        this.passportData = passportData;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", ssoId='" + ssoId + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", passportData='" + passportData + '\'' +
                ", address='" + address + '\'' +
                ", telNumber='" + telNumber + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
        result = prime * result + ((ssoId == null) ? 0 : ssoId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Customer))
            return false;
        Customer other = (Customer) obj;
        if (customerId == null) {
            if (other.customerId != null)
                return false;
        } else if (!customerId.equals(other.customerId))
            return false;
        if (ssoId == null) {
            if (other.ssoId != null)
                return false;
        } else if (!ssoId.equals(other.ssoId))
            return false;
        return true;
    }
}

