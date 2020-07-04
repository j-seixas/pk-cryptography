/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lesson2;
import java.math.BigInteger;
import java.util.Vector;

/**
 *
 * @author jnuno
 */
public class Lesson2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println(fermatLittleTheorem(BigInteger.valueOf(3),BigInteger.valueOf(5)));
        System.out.println(gcdExtended(3,5));
        //System.out.println(binaryPoweringMethod(5,20));
        //System.out.println(binaryPoweringMethodExt(5,20,23));
        System.out.println(euclideanGCD(3,5));
    }
    
    public static int euclideanGCD(int a, int b){
        
        int resultMod = b % a;
        while(true) {
            if(resultMod == 0)
                break;
            b = a;
            a = resultMod;
            resultMod = b % a;
        }
        
        return a;
    } 
    
    public static BigInteger fermatLittleTheorem(BigInteger a, BigInteger b){
        
        if(a.gcd(b).equals(BigInteger.ONE) && b.isProbablePrime(1)) {
            return a.pow(b.intValue() - 2).mod(b);
        }
        
        return BigInteger.ZERO;
    }
    
    public static int gcdExtended(int a, int b){
        int temp_a = a, temp_b = b, temp;
        
        Vector<Integer> x = new Vector<Integer>();
        Vector<Integer> q = new Vector<Integer>();
        x.add(0);
        x.add(1);
        int i = 0;

        while(temp_a >= 0) {
            
            if(i > 1) {
                int temp_x = (x.get(i - 2) - q.get(i - 2) * x.get(i - 1)) % b ;
                x.add(temp_x >= 0 ? temp_x : temp_x + b);
                //System.out.println("x" + x.lastElement());
            }
            
            if(temp_a >= 0) {
                if(temp_a > 0){
                    q.add(temp_b / temp_a);
                    //System.out.println(q.lastElement());
                    temp = temp_a;
                    temp_a = temp_b % temp_a;
                    temp_b = temp;
                } else
                    break;
                   
            } 
                  
            i++;
        } 
        
        return x.lastElement();
    }
    
    public static int binaryPoweringMethod(int base, int exponent){
        
        int result = 1;
        int b = exponent;
        
        
        while (b > 0) {
            

            if(b % 2 == 1) 
                result *= base;
                
            base *= base;      
            b = b >> 1;
        }
        
        return result;
        
    }
    
    public static int binaryPoweringMethodExt(int base, int exponent, int mod_value){
        
        int result = 1;
        int b = exponent;
        
        
        while (b > 0) {
            

            if(b % 2 == 1) {
                result *= base;
                result %= mod_value;
            }
                
                
            base *= base;  
            base %= mod_value;
            b = b >> 1;
            
            
        }
        
        return result;
        
    }
    
}
