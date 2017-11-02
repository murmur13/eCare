package eCare.controllers;

/**
 * Created by echerkas on 02.11.2017.
 */
public class HelloWorld {

    private String name;

    public void setName(String name){
        this.name = name;
    }

    public void printHello(){
        System.out.println("Spring says hello " + name + "!");
    }
}
