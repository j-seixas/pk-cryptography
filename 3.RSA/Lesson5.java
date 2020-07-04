/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lesson5;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 *
 * @author jnuno
 */
public class Lesson5 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        
        // Ex1
        /*RSA rsa = new RSA();    
        byte[] cipher = rsa.generateRSAKEM();
        byte[] key = rsa.receiveRSAKEM(cipher);
        System.out.println(DatatypeConverter.printHexBinary(key));
        
        // Ex 2
        String m = "message";
        byte[] EM = rsa.createEMSAPSS(m.getBytes());
        System.out.println(rsa.verifyEMSAPSS(EM, m.getBytes()));
        */
        // Ex 3
        String m = "message";
        RSA rsa = new RSA();
        byte[] signature = rsa.createRSAPSS(m.getBytes());
        System.out.println(rsa.verifyRSAPSS(m.getBytes(), signature));
                
        /*String msg = "message";
        RSA rsa = new RSA();
        byte[] cipher = rsa.OAEPEncoding(msg.getBytes());
        byte[] decoded = rsa.OAEPDecoding(cipher);
        System.out.println(new String(decoded));
        */
}
    

      
}
