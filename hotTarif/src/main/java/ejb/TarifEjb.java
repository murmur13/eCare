package ejb;

import eCare.model.DAO.TarifDAO;
import eCare.model.PO.Tarif;
import eCare.model.services.TarifServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by echerkas on 12.06.2018.
 */
@Stateless(name = "tarifEjb")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class TarifEjb {

    public TarifEjb() {
    }

    @Autowired
    TarifServiceImpl tarifService;

    @Autowired
    TarifDAO tarifDAO;

    public List<Tarif> getCheapTarifs(){
        List<Tarif> allTarifs = tarifService.findAll();
        List<Tarif> cheapTarifs;

        if (allTarifs.size() > 0) {
            Collections.sort(allTarifs, new Comparator<Tarif>() {
                public int compare(final Tarif object1, final Tarif object2) {
                    return object1.getPrice().compareTo(object2.getPrice());
                }
            });
        }
        return allTarifs;
    }
}
