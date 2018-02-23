package eCare.model.PO;

import javax.persistence.*;
import java.util.List;

/**
 * Created by echerkas on 20.10.2017.
 */

@Entity
@Table(name = "tarif")
public class Tarif{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tarif_id")
    private Integer tarifId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy="tarif")
    private List<Contract> tarifContracts;

    public Tarif(){
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

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Tarif{" +
                "tarifId=" + tarifId +
                ", name='" + name + '\'' +
                ", tarifContracts=" + tarifContracts +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tarif tarif = (Tarif) o;

        if (tarifId != tarif.tarifId) return false;
        if (!name.equals(tarif.name)) return false;
        return tarifContracts != null ? tarifContracts.equals(tarif.tarifContracts) : tarif.tarifContracts == null;

    }

    @Override
    public int hashCode() {
        int result = tarifId;
        result = 31 * result + name.hashCode();
        result = 31 * result + (tarifContracts != null ? tarifContracts.hashCode() : 0);
        return result;
    }
}
