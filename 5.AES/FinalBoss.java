/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalboss;

/**
 *
 * @author jnuno
 */
public class FinalBoss {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Testing transpose
        AES aes = new AES();
        int[] state = {0x6353e08c, 0x0960e104, 0xcd70b751, 0xbacad0e7};
        int[] transpose = aes.transpose(state);
        aes.printArray(transpose);
        
        //Testing subword
        int[] word = {0x01020304, 0x04030201};
        aes.printInt(aes.subWord(word[0]));
        aes.printInt(aes.subWord(word[1]));
        
        aes.printArray(aes.subWords(word));
        
        //Testing ShiftRows
        int[] words = {0x63cab704, 0x0953d051, 0xcd60e0e7, 0xba70e18c};
        aes.printArray(aes.shiftRows(words));
        
        //Testing ExpandKey
        int[] K = new int[(10 + 1) * 4];
        K[0] = 0x2b7e1516;
        K[1] = 0x28aed2a6;
        K[2] = 0xabf71588;
        K[3] = 0x09cf4f3c;
        
        aes.printArray(aes.expandKey(K));
        
        //Testing AddRoundKey
        int[] key = {0x00010203, 0x04050607, 0x08090a0b, 0x0c0d0e0f};
        int[] state_addRoundKey = {0x00112233, 0x44556677, 0x8899aabb, 0xccddeeff};
        aes.printArray(aes.addRoundKey(state_addRoundKey, key));
        
        //Testing MixColumns
        int[] st = {0x6353e08c, 0x0960e104, 0xcd70b751, 0xbacad0e7};
        System.out.println("--> MixColumns");
        aes.printArray(aes.mixColumns(st));
        System.out.println("--> Inv Mix Columns");
        int[] invSt = {0xe9f74eec, 0x023020f6, 0x1bf2ccf2, 0x353c21c7};
        aes.printArray(aes.invMixColumns(invSt));
        
        
        //AES Cyphering
        int[] m = {0x00112233, 0x44556677, 0x8899aabb, 0xccddeeff};
        int[] k = new int[(10 + 1) * 4];
        k[0] = 0x00010203;
        k[1] = 0x04050607;
        k[2] = 0x08090a0b;
        k[3] = 0x0c0d0e0f;

        System.out.println("--> Cyphering");
        int[] cypher = aes.aesCyphering(k, m);
        aes.printArray(cypher);
        
        //AES Decyphering
        System.out.println("--> Decyphering");
        aes.printArray(aes.aesDecyphering(aes.K, cypher));
        
    }
    
}
