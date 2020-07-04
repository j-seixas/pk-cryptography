package finalboss;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
//import javax.xml.bind.DatatypeConverter;

public class AES{
   
    public AES(){}
    
    int Nb = 4;  
    int Nk = 4; //for AES 128
    int Nr = 10; //for AES 128

    int[] K = new int[(Nr + 1) * Nb];;
    int[] state = new int[4];

    int[][] sbox = {{0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76}, 
        {0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0}, 
        {0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15}, 
        {0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75}, 
        {0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84}, 
        {0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf}, 
        {0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8}, 
        {0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2}, 
        {0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73}, 
        {0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb}, 
        {0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79}, 
        {0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08}, 
        {0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a}, 
        {0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e}, 
        {0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf}, 
        {0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}};
		
    int[][] invsbox = {{0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb}, 
        {0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb}, 
        {0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e}, 
        {0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25}, 
        {0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92}, 
        {0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84}, 
        {0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06}, 
        {0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b}, 
        {0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73}, 
        {0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e}, 
        {0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b}, 
        {0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4}, 
        {0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f}, 
        {0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef}, 
        {0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61}, 
        {0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d}};
		
    int[] rcon = {0x01000000, 0x02000000, 0x04000000, 0x08000000, 0x10000000, 0x20000000, 0x40000000, 0x80000000, 0x1b000000, 0x36000000};

    int[] mixVars = {0x02030101, 0x01020301, 0x01010203, 0x03010102};

    int[] invMixVars = {0x0e0b0d09, 0x090e0b0d, 0x0d090e0b, 0x0b0d090e};


    public byte fastMul(int a, int e){
        int tmp = 2;
        int base = a;
        while(tmp <= e){
            if ( ((a >> 7) & 0xFF) == 0x01)
                a = ((a << 1) & 0xFF) ^ 0x1b;
            else
                a = ((a << 1) & 0xFF);
            tmp *= 2;
        }
        tmp /= 2;
        while(tmp < e){
            a = a ^ base;
            tmp++;
        }
        return (byte)(a & 0xFF);
    }
    
    public byte fastMul2(int a, int e){
        while (e != 0x01){
            int sign = (a >> 7) & 0x01;
            a = (a << 1) & 0xFF;
            if (sign == 1)
                a = a ^ 0x1b;
            e = e/2;
        }
        return (byte)a;
    }
    
    public byte xTimes(int a, int e){
        int temp = 0x00;
        for (int i = 7; i >0; i--){
            if (((e >> i) & 0x01) == 1){
                temp = temp ^ fastMul2(a, (int)Math.pow(2, i));
            }
        }
        if (e % 2 == 1)
            temp = temp ^ a;
        return (byte)temp;
    }
    
    public int[] transpose(int[] in) {
        byte[][] matrix = new byte[4][4];
        matrix[0] = ByteBuffer.allocate(4).putInt(in[0]).array();
        matrix[1] = ByteBuffer.allocate(4).putInt(in[1]).array();
        matrix[2] = ByteBuffer.allocate(4).putInt(in[2]).array();
        matrix[3] = ByteBuffer.allocate(4).putInt(in[3]).array();
        
        byte[][] out = new byte[4][4];
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                out[i][j] = matrix[j][i];
            }
        }
        
        int[] output = new int[4];
        output[0] = ByteBuffer.wrap(out[0]).getInt();
        output[1] = ByteBuffer.wrap(out[1]).getInt();
        output[2] = ByteBuffer.wrap(out[2]).getInt();
        output[3] = ByteBuffer.wrap(out[3]).getInt();
        return output;
    }
    
    public int subWord(int a){
        int out = 0;
        
        for(int i = 0; i < 4; i++)
            out = (out << 8) | sbox[(a >> 32 - 8 * (i + 1) & 0xF0) >> 4][a >> 32 - 8 * (i + 1) & 0x0F];
        return out;
    }
    
    public int[] subWords(int[] arr){
        for(int i = 0; i < arr.length; i++)
            arr[i] = subWord(arr[i]);
        
        return arr;
    }
    
      public int invSubWord(int a){
        int out = 0;
        
        for(int i = 0; i < 4; i++)
            out = (out << 8) | invsbox[(a >> 32 - 8 * (i + 1) & 0xF0) >> 4][a >> 32 - 8 * (i + 1) & 0x0F];
        return out;
    }
    
    public int[] invSubWords(int[] arr){
        for(int i = 0; i < arr.length; i++)
            arr[i] = invSubWord(arr[i]);
        
        return arr;
    }
    
    
    public int[] shiftRows(int[] arr){
        arr = transpose(arr);
        for(int i = 0; i < arr.length; i++){
            arr[i] = Integer.rotateLeft(arr[i], i * 8);
        }
        return transpose(arr);
    }
    
    public int[] invShiftRows(int[] arr){
        arr = transpose(arr);
        for(int i = 0; i < arr.length; i++){
            arr[i] = Integer.rotateRight(arr[i], i * 8);
        }
        return transpose(arr);
    }
    
    public int[] expandKey(int[] key){
           
        for(int i = Nk; i < (Nr + 1) * Nb; i++){
            int tmp = key[i-1];
            if(i % Nk == 0)
                tmp = Integer.rotateLeft(subWord(tmp), 8) ^ rcon[Math.floorDiv(i, Nk) - 1];
            else if(Nk > 6 && i % Nk == 4)
                tmp = subWord(tmp);

            key[i] = tmp ^ key[i - Nk];
        }
        
        return key;
    }
    
    public int[] addRoundKey(int[] state, int[] roundKey){
        for(int i = 0; i < state.length || i < roundKey.length; i++)
            state[i] = state[i] ^ roundKey[i];
        
        return state;
    }
    
    public int[] mixColumns(int[] state){
        int[] tstate = transpose(state);
        byte[][] matrix = new byte[4][4];
        int[][] mState = new int[4][4];
        int[][] mMix = new int[4][4];
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++) {
                mState[i][j] = tstate[i] >> ((3 - j) * 8) & 0xFF;
                mMix[i][j] = mixVars[i] >> ((3 - j) * 8) & 0xFF;
            }
        }
        
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                for(int k = 0; k < 4; k++)
                    matrix[i][j] ^= xTimes(mState[k][j], mMix[i][k]);
            }
        }
        
        int[] out = new int[4];
        for(int i = 0; i < 4; i++)
            out[i] = ByteBuffer.wrap(matrix[i]).getInt();
        
        return transpose(out);
    }
    
    public int[] invMixColumns(int[] state){
        int[] tstate = transpose(state);
        byte[][] matrix = new byte[4][4];
        int[][] mState = new int[4][4];
        int[][] mInvMix = new int[4][4];
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++) {
                mState[i][j] = tstate[i] >> ((3 - j) * 8) & 0xFF;
                mInvMix[i][j] = invMixVars[i] >> ((3 - j) * 8) & 0xFF;
            }
        }
        
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                for(int k = 0; k < 4; k++)
                    matrix[i][j] ^= xTimes(mState[k][j], mInvMix[i][k]);
            }
        }
        
        int[] out = new int[4];
        for(int i = 0; i < 4; i++)
            out[i] = ByteBuffer.wrap(matrix[i]).getInt();
        
        return transpose(out);
    }
    
    
    public int[] aesCyphering(int[] key, int[] M){
        K = expandKey(key);
        state = M;

        int[] tmp = new int[Nb];
        System.arraycopy(K, 0, tmp, 0, Nb);
        state = addRoundKey(state, tmp);
        
        for(int i = 1; i < Nr; i++){
            
            state = subWords(state);
            state = shiftRows(state);
            state = mixColumns(state);
            
            int[] temp = new int[(i + 1) * Nb - i * Nb];
            System.arraycopy(K, i * Nb, temp, 0, (i + 1) * Nb - i * Nb);
            state = addRoundKey(state, temp);  
        }
       
        state = subWords(state);
        state = shiftRows(state);
        
        int[] ktmp = new int[(Nr + 1) * Nb - Nr * Nb];
        System.arraycopy(K, Nr * Nb, ktmp, 0, (Nr + 1) * Nb - Nr * Nb);
        state = addRoundKey(state, ktmp);

        return state;
    }
    
    public int[] aesDecyphering(int[] key, int[] cypher) {
        K = expandKey(key);
        int[] tmp = new int[(Nr + 1) * Nb - Nr * Nb];
        System.arraycopy(K, Nr * Nb, tmp, 0, (Nr + 1) * Nb - Nr * Nb);
        state = addRoundKey(cypher, tmp);
       
        for(int i = Nr - 1; i > 0; i--) {
            state = invSubWords(state);
            state = invShiftRows(state);
   
            int[] temp = new int[(i + 1) * Nb - i * Nb];
            System.arraycopy(K, i * Nb, temp, 0, (i + 1) * Nb - i * Nb);
            state = addRoundKey(state, temp); 
            
            state = invMixColumns(state); 
        }
       
        state = invSubWords(state);
        state = invShiftRows(state);
        
        int[] ktmp = new int[Nb];
        System.arraycopy(K, 0, ktmp, 0, Nb);
        state = addRoundKey(state, ktmp);
        
        return state;
    }
    
    
    public void printInt(int a){
        System.out.println(Integer.toHexString(a));
    }
    
    public void printArray(int[] arr){
        System.out.print("Printing int array: ");
        for(int i = 0; i < arr.length; i++)
            System.out.print(Integer.toHexString(arr[i]) + " ");
        System.out.println("");
    }
    
    public void printArray(byte[] arr){
        System.out.print("Printing byte array: ");
        //System.out.println(DatatypeConverter.printHexBinary(arr));
    }
    
    public void printMatrix(byte[][] matrix){
        System.out.print("Printing byte matrix: ");
        for(int i = 0; i < matrix.length; i++)
          //  System.out.print(DatatypeConverter.printHexBinary(matrix[i]) + " ");
        System.out.println("");
    }
    
    public void printMatrix(int[][] matrix){
        System.out.print("---> Printing int matrix: ");
        for(int i = 0; i < matrix.length; i++)
            printArray(matrix[i]);
        System.out.println("---> Finished");
    }
}

