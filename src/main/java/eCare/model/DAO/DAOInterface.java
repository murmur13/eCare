package eCare.model.DAO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by echerkas on 18.10.2017.
 */
public interface DAOInterface <T, Id extends Serializable>{

        public void persist(T entity);

        public void update(T entity);

        public T findById(Id id);

        public void delete(T entity);

        public List<T> findAll();

        public void deleteAll();

    }

