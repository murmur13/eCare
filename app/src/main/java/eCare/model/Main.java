package eCare.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by echerkas on 27.11.2017.
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(Main.class);
        String password = "abc125";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        logger.info(passwordEncoder.encode(password));
    }

}

