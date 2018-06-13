package eCare.model.services;

import eCare.model.DAO.UserProfileDao;
import eCare.model.PO.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by echerkas on 16.11.2017.
 */

@Service("userProfileService")
@Transactional
    public class UserProfileServieImpl  implements UserProfileService{

    @Autowired
    UserProfileDao dao;

    public UserProfile findById(int id) {
        return dao.findById(id);
    }

    public UserProfile findByType(String type){
        return dao.findByType(type);
    }

    public List<UserProfile> findAll() {
        return dao.findAll();
    }
}
