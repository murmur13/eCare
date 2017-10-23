package eCare.model.DAO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by echerkas on 20.10.2017.
 */

@Entity
@Table(name = "tarif")
public class Tarif implements Serializable {

//    public List<Contract> getContractsTarif() {
//        return contractsTarif;
//    }
//
//    public void setContractsTarif(List<Contract> contractsTarif) {
//        this.contractsTarif = contractsTarif;
//    }

    @Override
    public String toString() {
        return "Tarif{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contractsTarif=" + contractsTarif +
                '}';
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tarif_id")
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy="tarif", fetch = FetchType.EAGER)
    private List<Contract> contractsTarif;

    public Tarif(){
    }

    public Tarif(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
