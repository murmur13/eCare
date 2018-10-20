package eCare.model.services;

import eCare.model.po.UserProfile;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by echerkas on 15.11.2017.
 */
@Service
public interface UserProfileService {

    UserProfile findById(int id);

    UserProfile findByType(String type);

    List<UserProfile> findAll();

}

