package eCare.model.PO;

import javax.persistence.*;
import java.util.List;

/**
 * Created by echerkas on 18.10.2017.
 */

@Entity
@Table(name = "customer")
public class Customer{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int customerId;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "birth_date")
    private String birthDate;

    @Column(name = "passport_data")
    private String passportData;

    @Column(name = "address")
    private String address;

    @Column(name = "tel_number")
    private String telNumber;

    @Column(name = "mail")
    private String mail;

    @Column(name = "password")
    private String password;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="customer")
    private List<Contract> contracts;

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

    public int getId() {
        return customerId;
    }

    public void setId(int id) {
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

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }
}

