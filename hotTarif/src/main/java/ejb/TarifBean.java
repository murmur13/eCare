package ejb;

import eCare.model.po.Tarif;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by echerkas on 12.06.2018.
 */
@ManagedBean
@RequestScoped
public class TarifBean implements Serializable {

    @EJB
    TarifEjb tarifEjb;
    List<Tarif> cheapTarifs;
    List<Tarif> list;
    Tarif tarif;
    String id = null;

    public TarifEjb getTarifEjb() {
        return tarifEjb;
    }

    public void setTarifEjb(TarifEjb tarifEjb) {
        this.tarifEjb = tarifEjb;
    }

    public List<Tarif> getList() {
        return list;
    }

    public void setList(List<Tarif> list) {
        this.list = list;
    }

    public List<Tarif> getCheapTarifs() {
        return cheapTarifs;
    }

    public void setCheapTarifs(List<Tarif> cheapTarifs) {
        this.cheapTarifs = cheapTarifs;
    }

    public List<Tarif> getCheap() {
        try {
            cheapTarifs = tarifEjb.getCheapTarifs();
            return cheapTarifs;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cheapTarifs;
    }

    public String returnTarifFromQueue(String string){
        System.out.println("TarifId is received in Ejb = " + string);
        setId(string);
        return id;
    }

    public boolean isPollEnabled() {
        Boolean param = true;
        System.out.println("Returns " + param);
        return param;
    }

    public Date getDate() {

        return new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
