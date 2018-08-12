package eCare.converter;

import eCare.model.po.UserProfile;
import eCare.model.services.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by echerkas on 15.11.2017.
 */
@Component
public class RoleToUserProfileConverter implements Converter<Object, UserProfile> {

        static final Logger logger = LoggerFactory.getLogger(RoleToUserProfileConverter.class);

        @Autowired
        private UserProfileService userProfileService;

        /**
         * Gets UserProfile by Id
         * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
         */
    public UserProfile convert(Object element) {
        Integer id = Integer.parseInt((String)element);
        UserProfile profile= userProfileService.findById(id);
        logger.info("Profile : {}",profile);
        return profile;
    }
}
