package eCare.model.DAO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by echerkas on 18.10.2017.
 */
public interface DAOInterface <T, Id extends Serializable>{

        void persist(T entity);

        void update(T entity);

         T findById(Id id);

//        void deleteById(Id id);

        void delete(T entity);

        List<T> findAll();

         void deleteAll();

    }

