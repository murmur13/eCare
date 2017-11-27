package eCare.model.PO;

import java.io.Serializable;

/**
 * Created by echerkas on 15.11.2017.
 */
public enum UserProfileType implements Serializable {
        USER("USER"),
        DBA("DBA"),
        ADMIN("ADMIN");

        String userProfileType;

        private UserProfileType(String userProfileType){
            this.userProfileType = userProfileType;
        }

        public String getUserProfileType(){
            return userProfileType;
        }

    }

