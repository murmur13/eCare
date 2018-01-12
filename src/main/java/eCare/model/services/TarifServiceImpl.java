package eCare.model.services;

import eCare.model.DAO.TarifDAO;
import eCare.model.PO.Customer;
import eCare.model.PO.Tarif;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by echerkas on 06.12.2017.
 */
@Service("tarifService")
@Transactional
public class TarifServiceImpl implements  TarifService{

        @Autowired
        private TarifDAO tarifDAO;

        public Tarif findById(Integer id) {
            return tarifDAO.findById(id);
        }

        public void persist(Tarif tarif) {
            tarifDAO.persist(tarif);
        }

        public void delete(Integer id){
            Tarif tarif = tarifDAO.findById(id);
            tarifDAO.delete(tarif);
        }

        public void update(Tarif tarif) {
            tarifDAO.update(tarif);
        }

        public List<Tarif> findAll() {
            return tarifDAO.findAll();
        }

        public void deleteAll(){
            tarifDAO.deleteAll();
        }

        public boolean isTarifUnique(String name){
            boolean isUnique = false;
            List<Tarif> tarif = findByName(name);
            if(tarif == null || tarif.isEmpty()){
                isUnique=true;
            }
            return isUnique;
        }

    public List<Tarif> findByName(String name) {
        List<Tarif> tarif = tarifDAO.findByName(name);
        return tarif;
    }
    }
