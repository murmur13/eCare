package eCare.model.PO;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by echerkas on 20.10.2017.
 */

@Entity
@Table(name = "tarif")
public class Tarif implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tarif_id")
    private Integer tarifId;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    public List<Contract> getTarifContracts() {
        return tarifContracts;
    }

    public void setTarifContracts(List<Contract> tarifContracts) {
        this.tarifContracts = tarifContracts;
    }

    @OneToMany(mappedBy="tarif")
    private List<Contract> tarifContracts;

    public Tarif(){
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Tarif(String name) {
        this.name = name;
    }

    public int getTarifId() {
        return tarifId;
    }

    public void setTarifId(Integer tarifId) {
        this.tarifId = tarifId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Tarif{" +
                "tarifId=" + tarifId +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tarif tarif = (Tarif) o;

        return tarifId.equals(tarif.tarifId);

    }

    @Override
    public int hashCode() {
        return tarifId.hashCode();
    }
}
