package eCare.model.DAO;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by echerkas on 20.10.2017.
 */

@Entity
@Table(name = "contract")
public class Contract implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id", insertable = false, updatable = false)
    private int id;

    @Column(name = "t_number")
    private String tNumber;

    @Column(name = "tarif_tarif_id")
    private int tarifId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_customer_id", nullable = false)
    @Id
    private Customer customer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tarif_tarif_id", nullable = false, insertable = false, updatable = false)
    @Id
    private Tarif tarif;

    public int getTarifId() {
        return tarifId;
    }

    public void setTarifId(int tarifId) {
        this.tarifId = tarifId;
    }

    public Contract(){
    }

    public int getId() {
        return id;
    }

    public Contract(String tNumber, int tarifId) {
        this.tNumber = tNumber;
        this.tarifId = tarifId;
    }

    @Override
    public String toString() {

        return "Contract{" +
                "id=" + id +
                ", tNumber='" + tNumber + '\'' +
                ", tarifId=" + tarifId +
                ", customer=" + customer +
                ", tarif=" + tarif +
                '}';
    }

    public Tarif getTarif() {
        return tarif;
    }

    public void setTarif(Tarif tarif) {
        this.tarif = tarif;
    }

    public Customer getCustomer() {

        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String gettNumber() {
        return tNumber;
    }

    public void settNumber(String tNumber) {
        this.tNumber = tNumber;
    }

    public Contract(String tNumber) {
        this.tNumber = tNumber;

    }
}
