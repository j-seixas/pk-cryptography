/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lesson1;
import java.math.BigInteger;
import java.util.BitSet;
import java.util.Random;

/**
 *
 * @author student
 */
public class Lesson1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BigInteger p = BigInteger.probablePrime(512, new Random());
        BigInteger q = BigInteger.probablePrime(512, new Random());
       
        Boolean loop = true;
        while(loop) {
            loop = false;
            if(p.equals(q)){
                loop = true;
                q = BigInteger.probablePrime(512, new Random());
            }
            if(!p.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))){
                loop = true;
                p = BigInteger.probablePrime(512, new Random());
            }
            if(!q.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))){
                loop = true;
                q = BigInteger.probablePrime(512, new Random());
            }
        }
       
        BigInteger n = p.multiply(q);
        System.out.println(n);
       
        int lenS = n.bitLength() / 4;
        System.out.println(lenS);
        
        BigInteger s;
        do {
            s = new BigInteger(lenS, new Random());
        } while(!s.gcd(n).equals(BigInteger.ONE));
       
        System.out.println("n:" + n + " s:" + s);
       
        String msg = "aaaaaaaaa";
        byte[] msgBytes = msg.getBytes();
       
        BitSet keyBitSet = new BitSet(msgBytes.length);
        
        BigInteger X0 = s.pow(2).mod(n);
        BigInteger lastX = X0;
        keyBitSet.set(0, functionK(lastX));
        
        for(int i = 1; i < msgBytes.length * 8; i++){
            lastX = lastX.pow(2).mod(n);
            keyBitSet.set(i, functionK(lastX));
        }
       
        // Cypher
        BigInteger message = new BigInteger(msgBytes);
        BigInteger key = new BigInteger(keyBitSet.toByteArray());
        
        BigInteger cypher = message.xor(key);
        System.out.println("Message cyphered: " + new String(cypher.toByteArray()));
        System.out.println("cypher: " + cypher);
        
        BigInteger decypheredMsg = cypher.xor(key);
        System.out.println("Message decyphered: " + new String(decypheredMsg.toByteArray()));
        System.out.println("Decyphered value: " + decypheredMsg);
    }
    
    public static boolean functionK(BigInteger value){
        return value.mod(BigInteger.valueOf(2)).equals(BigInteger.ONE);
    }
   
}
