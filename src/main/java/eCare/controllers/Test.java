package eCare.controllers;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by echerkas on 02.11.2017.
 */
public class Test {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("SpringBeans.xml");
        HelloWorld obj = (HelloWorld) context.getBean("helloBean");
        obj.printHello();
    }
}
