package eCare.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by echerkas on 02.11.2017.
// */
public class HelloWorld {
    private String name;

    public void setName(String name){
        this.name = name;
    }

    public void printHello(){
        System.out.println("Spring says hello " + name + "!");
    }
}
