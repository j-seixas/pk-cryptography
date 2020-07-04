/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lesson6;

import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author jnuno
 */
public class Lesson6 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SHA sha = new SHA();
        System.out.println(DatatypeConverter.printHexBinary(sha.sha512("".getBytes())));
    }
    
}
