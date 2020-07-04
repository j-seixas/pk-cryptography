/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lesson5;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author jnuno
 */
public class RSA {
    public BigInteger p, q, n, fi, e, d;
    
    public RSA() {
       
        do {
            
            p = BigInteger.probablePrime(1024 - 64, new Random());
            q = BigInteger.probablePrime(1024 + 64, new Random());
           
        } while (p.equals(q));
        
        n = p.multiply(q);
        fi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
                
        do {
            e = new BigInteger(fi.bitLength() / 4, new Random());
        } while ( !e.gcd(fi).equals(BigInteger.ONE));
        
        d = e.modInverse(fi);
    }
    
     public byte[] RSAEncode(byte[] msg){
        BigInteger m = new BigInteger(msg);
        return m.modPow(e, n).toByteArray();
    }
    
    public byte[] RSADecode(byte[] encodedMsg){
        BigInteger c = new BigInteger(encodedMsg);
        return c.modPow(d,n).toByteArray();
    }
    
    public byte[] RSA_OAEP_Encode(byte[] msg) throws NoSuchAlgorithmException{
        
        byte[] BEM = OAEPEncoding(msg);
        BigInteger c = new BigInteger(1, BEM);

        return c.modPow(e, n).toByteArray();
    }
    
    public byte[] RSA_OAEP_Decode(byte[] encodedMsg) throws NoSuchAlgorithmException{
        BigInteger c = new BigInteger(1, encodedMsg);
        encodedMsg = c.modPow(d, n).toByteArray();
        
        byte[] msg = new byte[255];
        System.arraycopy(encodedMsg, 0, msg, msg.length - encodedMsg.length, encodedMsg.length);

        return OAEPDecoding(msg);      
    }
    
    public byte[] mgf1(byte[] m, int len){
        
        //byte[] output = new byte[(int)Math.ceil((double)len / 32)];
        byte[] output = new byte[256];
        
        
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] temp = new byte[m.length + 4];
            System.arraycopy(m, 0, temp, 0, m.length);

            
            for(int i = 0; i < Math.ceil(len/32.0); i++){
                temp = new byte[m.length + 4];
                System.arraycopy(m, 0, temp, 0, m.length);
                
                ByteBuffer bf = ByteBuffer.allocate(4);
                bf.putInt(i);
                bf.rewind();
                bf.get(temp, temp.length-4, 4);

                byte[] tempSha = sha.digest(temp);
                System.arraycopy(tempSha, 0, output, i * tempSha.length, tempSha.length);
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        byte[] returnOutput = new byte[len];
        System.arraycopy(output, 0, returnOutput, 0, len);
        
        return returnOutput;
        
    }
    
    public byte[] OAEPEncoding(byte[] message) throws NoSuchAlgorithmException {
        //Using SHA-256 and hLen of 32
        int hLen = 32;
        int k = 255;
        
        if(message.length > k - 2 * hLen - 2){
            System.out.println("Error");
        }
        
        byte[] lHash = MessageDigest.getInstance("SHA-256").digest("".getBytes());
        byte[] PS = new byte[k - message.length - 2 * hLen - 2];
        
        byte[] DB = new byte[k - hLen - 1];
        System.arraycopy(lHash, 0, DB, 0, hLen);
        System.arraycopy(PS, 0, DB, hLen, PS.length);
        DB[hLen + PS.length] = 0x01;
        System.arraycopy(message, 0, DB, hLen + PS.length + 1, message.length);
        
        byte[] seed = new byte[hLen];
        new Random().nextBytes(seed);
        
        byte[] dbMask = mgf1(seed, k - hLen - 1);
        
        byte[] maskedDB = new byte[k - hLen - 1];
        for(int i = 0; i < DB.length; i++) 
            maskedDB[i] = (byte) (DB[i] ^ dbMask[i]);
        
        byte[] seedMask = mgf1(maskedDB, hLen);
        byte[] maskedSeed = new byte[hLen];
        for(int i = 0; i < hLen; i++) 
            maskedSeed[i] = (byte) (seed[i] ^ seedMask[i]);
        
        
        byte[] EM = new byte[k];
        EM[0] = 0x00;
        System.arraycopy(maskedSeed, 0, EM, 1, hLen);
        System.arraycopy(maskedDB, 0, EM, hLen + 1, k - hLen - 1);
        
        return EM;
    }
    
    public byte[] OAEPDecoding(byte[] cipher) throws NoSuchAlgorithmException {
        //Using SHA-256 and hLen of 32
        int hLen = 32;
        int k = 255;
        
        byte Y;
        byte[] maskedSeed = new byte[hLen];
        byte[] maskedDB = new byte[k - hLen - 1];
        
        //System.arraycopy(cipher, 0, Y, 0, 1);
        Y = cipher[0];
        if(Y != 0x00){
            System.out.println("Error on 10 ->" + Y);
            return new byte[0];
        }
        System.arraycopy(cipher, 1, maskedSeed, 0, hLen);
        System.arraycopy(cipher, 1 + hLen, maskedDB, 0, k - hLen - 1);
        
        byte[] lHash = MessageDigest.getInstance("SHA-256").digest("".getBytes());
        
        byte[] seedMask = mgf1(maskedDB, hLen);
        byte[] seed = new byte[hLen];
        for(int i = 0; i < hLen; i++) 
            seed[i] = (byte) (maskedSeed[i] ^ seedMask[i]);
        
        byte[] dbMask = mgf1(seed, k - hLen - 1);    
        byte[] DB = new byte[k - hLen - 1];
        for(int i = 0; i < DB.length; i++) 
            DB[i] = (byte) (maskedDB[i] ^ dbMask[i]);
        
        byte[] lHash1 = new byte[hLen];
        System.arraycopy(DB, 0, lHash1, 0, hLen);
        int j = hLen;
        for(; j < DB.length; j++){
            if(DB[j] == 0x01)
                break;
            else if(DB[j] != 0x00) {
                System.out.println(DB[j]);
                System.out.println("Error on 8" + j);
                return new byte[0];
            }
        }
        
        if(j == DB.length){
            System.out.println("Error on 8");
            return new byte[0];
        }

        
        byte[] M = new byte[DB.length - j - 1];
        System.arraycopy(DB, j + 1, M, 0, DB.length - j - 1);
        
        for(int i = 0; i < lHash.length; i++)
            if(lHash[i] != lHash1[i]){
                System.out.println("Error on 9");
                return new byte[0];
            }     
               
        return M; 
    }
    
    public byte[] generateRSAKEM() throws NoSuchAlgorithmException{
        BigInteger RAND = new BigInteger(188 * 8, new Random());
        byte[] BRAND = RAND.toByteArray();

        byte[] BKEY = MessageDigest.getInstance("SHA-256").digest(BRAND);
        //System.out.println(DatatypeConverter.printHexBinary(BKEY));
        byte[] BC = RSA_OAEP_Encode(BRAND);
        return BC;
    }
    
    public byte[] receiveRSAKEM(byte[] cryptogram) throws NoSuchAlgorithmException{
        byte[] KEY = RSA_OAEP_Decode(cryptogram);
        byte[] BK = MessageDigest.getInstance("SHA-256").digest(KEY);
        return BK;
    }
    
    public byte[] createEMSAPSS(byte[] message) throws NoSuchAlgorithmException {
        
        int hLen = 32;
        int sLen  = 32;
        int emLen = 255;
        
        if(emLen < hLen + sLen + 2){
            System.out.println("Error in emLen < hLen + sLen + 2");
            return new byte[0];
        }
        
        byte[] mHash = MessageDigest.getInstance("SHA-256").digest(message);
        byte[] salt = new byte[sLen];
        new Random().nextBytes(salt);
        
        byte[] M_ = new byte[8 + hLen + sLen];
        for (int i = 0; i < 8; i++) {
            M_[i] = 0x00;
        }
        
        System.arraycopy(mHash, 0, M_, 8, mHash.length);
        System.arraycopy(salt, 0, M_, 8 + mHash.length, salt.length);

        byte[] H = MessageDigest.getInstance("SHA-256").digest(M_);
    
        byte[] PS = new byte[emLen - sLen - hLen - 2];
        byte[] DB = new byte[emLen - hLen - 1];
        
        System.arraycopy(PS, 0, DB, 0, PS.length);
        DB[PS.length] = 0x01;
        System.arraycopy(salt, 0, DB, PS.length + 1, salt.length);
        
        byte[] dbMask = mgf1(H, emLen - hLen - 1);
        
        byte[] maskedDB = new byte[DB.length];
        for (int i = 0; i < DB.length; i++) {
            maskedDB[i] = (byte)(DB[i] ^ dbMask[i]); 
        }
        
        byte[] EM = new byte[emLen];
        System.arraycopy(maskedDB, 0, EM, 0, maskedDB.length);
        System.arraycopy(H, 0, EM, maskedDB.length, EM.length - maskedDB.length - 1);
        EM[EM.length - 1] = (byte)0xbc;
        
        return EM;
    }
    
    public boolean verifyEMSAPSS(byte[] EM, byte[] message) throws NoSuchAlgorithmException {
        int hLen = 32;
        int sLen  = 32;
        int emLen = EM.length;
        
        if(emLen < hLen + 2){
            System.out.println("Signature invalid 1");
            return false;
        }
        
        byte[] mHash = MessageDigest.getInstance("SHA-256").digest(message);
        
        if(EM[emLen - 1] != (byte)0xbc){
            System.out.println("Signature invalid 2");
            return false;            
        }
        
        byte[] maskedDB = new byte[emLen - hLen - 1];
        System.arraycopy(EM, 0, maskedDB, 0, emLen - hLen - 1);

        byte[] H = new byte[hLen];
        System.arraycopy(EM, emLen - hLen - 1, H, 0, hLen);
        
        byte[] dbMask = mgf1(H, emLen - hLen - 1);
        byte[] DB = new byte[maskedDB.length];
        for (int i = 0; i < maskedDB.length; i++) {
            DB[i] = (byte)(maskedDB[i] ^ dbMask[i]); 
        }
        
        for(int i = 0; i < emLen - hLen - sLen - 2; i++){
            if(DB[i] != (byte)0x00){
                System.out.println("Signature invalid 3");
                return false;
            }
                
        }
        
        if(DB[emLen - hLen - sLen - 2] != (byte)0x01){
            System.out.println("Signature invalid 4");
            return false;
        }
        
        byte[] salt = new byte[sLen];
        System.arraycopy(DB, DB.length - sLen, salt, 0, sLen);
        
        byte[] M_ = new byte[8 + mHash.length + salt.length];
        System.arraycopy(mHash, 0, M_, 8, mHash.length);
        System.arraycopy(salt, 0, M_, 8 + mHash.length, salt.length);
        byte[] H_ = MessageDigest.getInstance("SHA-256").digest(M_);
        
        boolean equal = true;
        for(int i = 0; i < hLen; i++){
            if(H[i] != H_[i]){
                return false;
            }
        }
        
        System.out.println("Signature valid");
        return equal;
    }
    
    byte[] createRSAPSS(byte[] msg) throws NoSuchAlgorithmException {
        byte[] BEM = createEMSAPSS(msg);
        BigInteger EM = new BigInteger(1, BEM);
        EM = EM.modPow(d, n);
        byte[] SIG = EM.toByteArray();
        return SIG;
    }
    
    
    boolean verifyRSAPSS(byte[] msg, byte[] signature) throws NoSuchAlgorithmException{
        BigInteger EM = new BigInteger(1, signature);
        EM = EM.modPow(e, n);
        byte[] BEM = EM.toByteArray();

        return verifyEMSAPSS(trimArray(BEM), msg);
    }
    
    public byte[] trimArray(byte[] array) {
        
        if(array[0] == 0){
            byte[] temp = new byte[array.length-1];
            System.arraycopy(array, 1, temp, 0, temp.length);
            return temp;
        }       
            
        return array;
    }

}
