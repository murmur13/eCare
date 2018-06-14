package ejb;

import eCare.model.DAO.AbstractDao;
import eCare.model.DAO.TarifDAO;
import eCare.model.PO.Tarif;
import eCare.model.services.TarifServiceImpl;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by echerkas on 12.06.2018.
 */
@Singleton(name = "tarifEjb")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class TarifEjb {

    public TarifEjb() {
    }

    @PostConstruct
    public void test() {
        System.out.println(applicationContext);
    }

    @Autowired
    ApplicationContext applicationContext;

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

    public TarifServiceImpl getTarifService() {
        return tarifService;
    }

    public TarifDAO getTarifDAO() {
        return tarifDAO;
    }
}
