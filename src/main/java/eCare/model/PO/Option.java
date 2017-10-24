package eCare.model.PO;

import javax.persistence.*;
import java.util.List;

/**
 * Created by echerkas on 24.10.2017.
 */

@Entity
@Table(name = "option")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private int optionId;

    @Column(name = "name")
    private String optionName;

    @Column(name = "price")
    private double optionPrice;

    @Column(name = "connection_cost")
    private double connectionCost;

    @ManyToMany
    @JoinTable(
            name="tarif_x_option",
            joinColumns=@JoinColumn(name="option_option_id", referencedColumnName="option_id"),
            inverseJoinColumns=@JoinColumn(name="tarif_tarif_id", referencedColumnName="tarif_id"))
    private List<Tarif> optionTarifs;

    @Override
    public String toString() {
        return "Option{" +
                "optionId=" + optionId +
                ", optionName='" + optionName + '\'' +
                ", optionPrice=" + optionPrice +
                ", connectionCost=" + connectionCost +
                '}';
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public double getOptionPrice() {
        return optionPrice;
    }

    public void setOptionPrice(double optionPrice) {
        this.optionPrice = optionPrice;
    }

    public double getConnectionCost() {
        return connectionCost;
    }

    public void setConnectionCost(double connectionCost) {
        this.connectionCost = connectionCost;
    }

    public List<Tarif> getOptionTarifs() {
        return optionTarifs;
    }

    public Option(String optionName, double optionPrice, double connectionCost) {
        this.optionName = optionName;
        this.optionPrice = optionPrice;
        this.connectionCost = connectionCost;
    }

    public void setOptionTarifs(List<Tarif> optionTarifs) {
        this.optionTarifs = optionTarifs;
    }

    public Option(String optionName, double optionPrice) {
        this.optionName = optionName;
        this.optionPrice = optionPrice;
    }

    public Option(String optionName) {

        this.optionName = optionName;
    }

    public Option() {

    }
}
