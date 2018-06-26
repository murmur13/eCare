package ejb.rest;

import eCare.model.PO.Tarif;
import eCare.model.services.TarifServiceImpl;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.interceptor.Interceptors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by echerkas on 26.06.2018.
 */
@Path("/ejbTarifs")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class REST {

    @Autowired
    TarifServiceImpl tarifService;

    @Path("/getTarifs")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
        public String getTarifs() {
        List<Tarif> tarifs = tarifService.findAll();
        JSONArray array = new JSONArray();
        array.put(tarifs);
        return array.toString();

    }

    @Path("/getTarifs/{tarifId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getTarif(@PathParam("tarifId") Integer tarifId){
        Tarif tarif = tarifService.findById(tarifId);
        JSONArray array = new JSONArray();
        array.put(tarif);
        return array.toString();
    }
}
