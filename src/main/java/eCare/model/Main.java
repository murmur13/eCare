package eCare.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by echerkas on 27.11.2017.
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String password = "abc125";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode(password));
    }

}

