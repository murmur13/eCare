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
    private int tarifId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy="tarif")
    private List<Contract> tarifContracts;

    public Tarif(){
    }

    public Tarif(String name) {
        this.name = name;
    }

    public int getId() {
        return tarifId;
    }

    public void setId(int id) {
        this.tarifId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
