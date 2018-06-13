package ejb;

import eCare.model.PO.Tarif;
import eCare.model.services.TarifService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by echerkas on 12.06.2018.
 */
@ManagedBean(name="tarifBean")
@RequestScoped
public class TarifBean {

    @EJB
    TarifEjb tarifEjb;
    List<Tarif> cheapTarifs;
    List<Tarif> list;

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
}
