package eCare.model.po;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Created by echerkas on 13.04.2018.
 */

@Component
public class RandomPhoneNumber {

    String number;

    public String generateNumber(){
        int num1, num2, num3; //3 numbers in area code
        int set2, set3; //sequence 2 and 3 of the phone number

        Random generator = new Random();

        //Area code number; Will not print 8 or 9
        num1 = generator.nextInt(7) + 1; //add 1 so there is no 0 to begin
        num2 = generator.nextInt(8); //randomize to 8 becuase 0 counts as a number in the generator
        num3 = generator.nextInt(8);
        set2 = generator.nextInt(643) + 100;

        //Sequence 3 of numebr
        set3 = generator.nextInt(8999) + 1000;

        return number = "(" + num1 + "" + num2 + "" + num3 + ")" + "-" + set2 + "-" + set3;
    }
}
