/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lesson6;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author jnuno
 */
public class SHA {
    public SHA(){
        
    }
    
    /*
    Right binary rotation of binary word w by n positions
    */
    public int RotateRight(int w, int n){
        return Integer.rotateRight(w, n);
    }
    
    /*
    Right binary shift of binary word w by n positions
    */
    public int ShiftRight(int w, int n){
        return w >>> n;
    }
    
    /*
    Function which converts element a into byte array of size b
    */
    public byte[] bytearray(long a, int b){
        return ByteBuffer.allocate(b).putLong(a).array();
    }
    
    /*
    Function which is parsing byte array a into unsigned 32 bits integer
    */
    public int parseUint32(byte[] a){
        return ByteBuffer.wrap(a).getInt();       
    }
    
    public long parseUint64(byte[] a){
        return ByteBuffer.wrap(a).getLong();       
    }
    
    public int S(int x, int a, int b, int c){
        return Integer.rotateRight(x, a) ^ Integer.rotateRight(x, b) ^ Integer.rotateRight(x, c);
    }
    
    public int s(int x, int a, int b, int c){
        return Integer.rotateRight(x, a) ^ Integer.rotateRight(x, b) ^ (x >>> c);
    }
    
    public long S(long x, int a, int b, int c){
        return Long.rotateRight(x, a) ^ Long.rotateRight(x, b) ^ Long.rotateRight(x, c);
    }
    
    public long s(long x, int a, int b, int c){
        return Long.rotateRight(x, a) ^ Long.rotateRight(x, b) ^ (x >>> c);
    }
    
    public int Maj(int a, int b, int c){
        return (a & b) ^ (a & c) ^ (b & c);
    }
    
    public int Ch(int a, int b, int c){
        return (a & b) ^ (~a & c);
    }
    
    public long Maj(long a, long b, long c){
        return (a & b) ^ (a & c) ^ (b & c);
    }
    
    public long Ch(long a, long b, long c){
        return (a & b) ^ (~a & c);
    }
    
    /*
    m - byte array representing a message
    n - block size (in bytes)
    */
    public byte[] pad(byte[] m, int n){
        long tmp = m.length * 8;
        
        byte[] pad = new byte[(int)Math.ceil((m.length + 9.0)/n)*n];
        System.arraycopy(m, 0, pad, 0, m.length);
        pad[m.length] = (byte)0x80;
        
        int i = m.length + 1;
        for(; i % n != n - 8; i++){    
        }
        
        System.arraycopy(bytearray(tmp, 8), 0, pad, i, 8);
        return pad;
    }
    
    public byte[] sha256(byte[] M){
        int[] H = {0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a, 
            0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19};
        int[] K = {0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1,
            0x923f82a4, 0xab1c5ed5, 0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 
            0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174, 0xe49b69c1, 0xefbe4786, 
            0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da, 
            0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 
            0x06ca6351, 0x14292967, 0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 
            0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85, 0xa2bfe8a1, 0xa81a664b, 
            0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070, 
            0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 
            0x5b9cca4f, 0x682e6ff3, 0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 
            0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2};
        
        
        M = pad(M, 64);
        int[] tmp = new int[8];
        int i = 0;
        while(i < M.length) {
            int[] W = new int[64];
            int j;
            for(j = 0; j < 16; j++){
                byte[] tmpM = new byte[4];
                System.arraycopy(M, (int)Math.floor(i / 64.0) * 64 + j * 4, tmpM, 0, 4);
                W[j] = parseUint32(tmpM);
            }   
            for(j = 16; j < 64; j++){
                int tmpW = W[j - 16] + W[j - 7] + s(W[j - 15], 7, 18, 3) + s(W[j - 2], 17, 19, 10);
                W[j] = tmpW;
                
            }
            
            for(j = 0; j < 8; j++){
                tmp[j] = H[j];
            }
            
            for(j = 0; j < 64; j++){
                int t1 = K[j] + W[j] + S(tmp[4], 6, 11, 25) + Ch(tmp[4], tmp[5], tmp[6]) + tmp[7];
                int t2 = Maj(tmp[0], tmp[1], tmp[2]) + S(tmp[0], 2, 13, 22);
                
                for(int k = 7; k > 0; k--){
                    tmp[k] = tmp[k - 1];
                }
                
                tmp[0] = t1 + t2;
                tmp[4] = tmp[4] + t1;
            }
            
            for(j = 0; j < 8; j++){
                H[j] += tmp[j];
            }
            
            i += 64;
        }
        
        // Converting int[] to byte[]
        ByteBuffer byteBuffer = ByteBuffer.allocate(H.length * Integer.BYTES);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(H);
        return byteBuffer.array();
    }
    
    public byte[] sha512(byte[] M){
        long[] H = {0x6a09e667f3bcc908L, 0xbb67ae8584caa73bL, 0x3c6ef372fe94f82bL,
                0xa54ff53a5f1d36f1L, 0x510e527fade682d1L, 0x9b05688c2b3e6c1fL,
                0x1f83d9abfb41bd6bL, 0x5be0cd19137e2179L};
        
        long[] K = {0x428a2f98d728ae22L, 0x7137449123ef65cdL, 0xb5c0fbcfec4d3b2fL,
                0xe9b5dba58189dbbcL, 0x3956c25bf348b538L, 0x59f111f1b605d019L, 
                0x923f82a4af194f9bL, 0xab1c5ed5da6d8118L, 0xd807aa98a3030242L, 
                0x12835b0145706fbeL, 0x243185be4ee4b28cL, 0x550c7dc3d5ffb4e2L, 
                0x72be5d74f27b896fL, 0x80deb1fe3b1696b1L, 0x9bdc06a725c71235L,
                0xc19bf174cf692694L, 0xe49b69c19ef14ad2L, 0xefbe4786384f25e3L, 
                0x0fc19dc68b8cd5b5L, 0x240ca1cc77ac9c65L, 0x2de92c6f592b0275L, 
                0x4a7484aa6ea6e483L, 0x5cb0a9dcbd41fbd4L, 0x76f988da831153b5L, 
                0x983e5152ee66dfabL, 0xa831c66d2db43210L, 0xb00327c898fb213fL,
                0xbf597fc7beef0ee4L, 0xc6e00bf33da88fc2L, 0xd5a79147930aa725L, 
                0x06ca6351e003826fL, 0x142929670a0e6e70L, 0x27b70a8546d22ffcL, 
                0x2e1b21385c26c926L, 0x4d2c6dfc5ac42aedL, 0x53380d139d95b3dfL, 
                0x650a73548baf63deL, 0x766a0abb3c77b2a8L, 0x81c2c92e47edaee6L,
                0x92722c851482353bL, 0xa2bfe8a14cf10364L, 0xa81a664bbc423001L, 
                0xc24b8b70d0f89791L, 0xc76c51a30654be30L, 0xd192e819d6ef5218L, 
                0xd69906245565a910L, 0xf40e35855771202aL, 0x106aa07032bbd1b8L, 
                0x19a4c116b8d2d0c8L, 0x1e376c085141ab53L, 0x2748774cdf8eeb99L,
                0x34b0bcb5e19b48a8L, 0x391c0cb3c5c95a63L, 0x4ed8aa4ae3418acbL, 
                0x5b9cca4f7763e373L, 0x682e6ff3d6b2b8a3L, 0x748f82ee5defb2fcL, 
                0x78a5636f43172f60L, 0x84c87814a1f0ab72L, 0x8cc702081a6439ecL, 
                0x90befffa23631e28L, 0xa4506cebde82bde9L, 0xbef9a3f7b2c67915L,
                0xc67178f2e372532bL, 0xca273eceea26619cL, 0xd186b8c721c0c207L, 
                0xeada7dd6cde0eb1eL, 0xf57d4f7fee6ed178L, 0x06f067aa72176fbaL, 
                0x0a637dc5a2c898a6L, 0x113f9804bef90daeL, 0x1b710b35131c471bL, 
                0x28db77f523047d84L, 0x32caab7b40c72493L, 0x3c9ebe0a15c9bebcL,
                0x431d67c49c100d4cL, 0x4cc5d4becb3e42b6L, 0x597f299cfc657e2aL, 
                0x5fcb6fab3ad6faecL, 0x6c44198c4a475817L};
        
        
        M = pad(M, 128);
        
        long[] tmp = new long[8];
        int i = 0;
        while(i < M.length) {
            long[] W = new long[80];
            int j;
            for(j = 0; j < 16; j++){
                byte[] tmpM = new byte[8];
                System.arraycopy(M, (int)Math.floor(i / 128.0) * 128 + j * 8, tmpM, 0, 8);
                W[j] = parseUint64(tmpM);
            }   
            for(j = 16; j < 80; j++){
                long tmpW = W[j - 16] + W[j - 7] + s(W[j - 15], 1, 8, 7) + s(W[j - 2], 19, 61, 6);
                W[j] = tmpW;
            }
            
            
            
            for(j = 0; j < 8; j++){
                tmp[j] = H[j];
            }
            
            for(j = 0; j < 80; j++){
                long t1 = K[j] + W[j] + S(tmp[4], 14, 18, 41) + Ch(tmp[4], tmp[5], tmp[6]) + tmp[7];
                long t2 = Maj(tmp[0], tmp[1], tmp[2]) + S(tmp[0], 28, 34, 39);
                
                for(int k = 7; k > 0; k--){
                    tmp[k] = tmp[k - 1];
                }
                
                tmp[0] = t1 + t2;
                tmp[4] = tmp[4] + t1;
            }
            
            for(j = 0; j < 8; j++){
                H[j] += tmp[j];
            }
            
            i += 128;
        }
        
        // Converting long[] to byte[]
        ByteBuffer byteBuffer = ByteBuffer.allocate(H.length * 8);
        LongBuffer longBuffer = byteBuffer.asLongBuffer();
        longBuffer.put(H);
        return byteBuffer.array();
    }
}
