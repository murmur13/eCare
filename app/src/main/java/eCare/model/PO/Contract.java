package eCare.model.PO;

import javax.persistence.*;
import java.util.List;

/**
 * Created by echerkas on 20.10.2017.
 */

@Entity
@Table(name = "contract")
public class Contract{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id")
    private Integer contractId;

    @Column(name = "t_number")
    private String tNumber;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="customer_customer_id")
    private Customer customer;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="tarif_tarif_id")
    private Tarif tarif;

    public Contract(){
    }

    public Contract(String tNumber, Customer customer, Tarif tarif) {
        this.tNumber = tNumber;
        this.customer = customer;
        this.tarif = tarif;
    }

    public Contract(String tNumber, Tarif tarif) {

        this.tNumber = tNumber;
        this.tarif = tarif;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public String gettNumber() {
        return tNumber;
    }

    public void settNumber(String tNumber) {
        this.tNumber = tNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Tarif getTarif() {
        return tarif;
    }

    public void setTarif(Tarif tarif) {
        this.tarif = tarif;
    }

    public Contract(String tNumber) {
        this.tNumber = tNumber;

    }


    @Override
    public String toString() {
        return "Contract{" +
                "contractId=" + contractId +
                ", tNumber='" + tNumber + '\'' +
                ", customer=" + customer +
                ", tarif=" + tarif +
                '}';

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contract contract = (Contract) o;

        return contractId.equals(contract.contractId);

    }

    @Override
    public int hashCode() {
        return contractId.hashCode();
    }
}
