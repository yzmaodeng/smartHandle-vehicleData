package com.xinghe.xbx.common.util.encrypt;

import java.io.UnsupportedEncodingException;

/**
 * 
 * @author jing
 * @date 2015年1月21日
 */
public class TEA {
	//keys
	private static int delta = 0x9e3779b9;
	private static int delta_16 = 0xE3779B90;

	private static int delta_a = 0x50E7F8D;
	private static int delta_b = 0x10984F7E;
	private static int delta_c = 0x76EF3720;
	
	public static String Encrypt(String in) {
		if (in.isEmpty())
			return "";
		
		byte [] pIn = in.getBytes();
	    int inLen = pIn.length;
	    
	    //first 4 bytes for key
		int outLen = inLen + 4;
		byte [] out = new byte[outLen];
		byte [] pOut = out;
		int inIndex = 0;
		int outIndex = 0;
	    
		int key = generateKey();
		//key = 565295188;

		intToByte(key, pOut, outIndex);
		outIndex += 4;

		int j = 0;
		int icount = (inLen / 8) * 2;
		while(j < icount)
		{
			int y = byteToInt(pIn, inIndex);
			inIndex += 4;
			int z = byteToInt(pIn, inIndex);
			inIndex += 4;
			int sum = 0;								
			int a, b, c, d;
			a = key;
			b = key + delta_a;
			c = key + delta_b;
			d = key + delta_c;
			for (int i = 0; i < 16; i++) 
			{						/* basic cycle start */
				sum += delta;
				y += ((z << 4) + a) ^ (z + sum) ^ ((z >>> 5) + b);
				z += ((y << 4) + c) ^ (y + sum) ^ ((y >>> 5) + d);/* end cycle */
			}
			intToByte(y, pOut, outIndex);
			outIndex += 4;
			intToByte(z, pOut, outIndex);
			outIndex += 4;
			j += 2;
		}

	    //for last bytes
		j = 4 * j;
		for (; j < inLen; ++j)
		{
			pOut[outIndex++] = (byte)~pIn[inIndex++];
		}
	    
	    //base64
	    String out_msg = Base64.Encode(out);
	    
		return out_msg;
	}
	
	public static String Decrypt(String in) {
		if (in.isEmpty())
			return "";
	    
	    //base64
	    byte[] pIn;
		try {
			pIn = Base64.Decode(in);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	    
	    int inLen = pIn.length;
		int outLen = inLen - 4;
		byte [] out = new byte[outLen];
		byte [] pOut = out;
		
		int inIndex = 0;
		int outIndex = 0;
		
		int key;
		key = byteToInt(pIn, inIndex);
		inIndex += 4;
		
		int j = 0;
		int icount = ((inLen - 4) / 8) * 2;
		while(j < icount)
		{
			int y = byteToInt(pIn, inIndex);
			inIndex += 4;
			int z = byteToInt(pIn, inIndex);
			inIndex += 4; 
			
			int sum = 0;

	        sum = delta_16; /* delta << 4*/

			int a, b, c, d;
			a = key;
			b = key + delta_a;
			c = key + delta_b;
			d = key + delta_c;

			for(int i=0; i<16; i++) 
			{													/* basic cycle start */
				z -= ((y<<4) + c) ^ (y + sum) ^ ((y>>>5) + d);
				y -= ((z<<4) + a) ^ (z + sum) ^ ((z>>>5) + b);
				sum -= delta;									/* end cycle */
			}
			
			intToByte(y, pOut, outIndex);
			outIndex += 4;
			intToByte(z, pOut, outIndex);
			outIndex += 4;
			j += 2;
		}
		
		j = 4 * j;
		for (; j < outLen; ++j)
		{
			pOut[outIndex++] = (byte)~pIn[inIndex++];
		}
	    
	    String out_msg = new String(out);

		return out_msg;
	}
	
	private static void intToByte(long number, byte[] inByte, int index) {  
	    inByte[index++] = (byte) (0xff & number);  
	    inByte[index++] = (byte) ((0xff00 & number) >>> 8);
	    inByte[index++] = (byte) ((0xff0000 & number) >>> 16);
	    inByte[index++] = (byte) ((0xff000000 & number) >>> 24);
	} 
	
	private static int byteToInt(byte[] inByte, int index) {
		int number = 0;
		String strNum = new String();
		strNum += String.format("%02x", inByte[index + 3]);
		strNum += String.format("%02x", inByte[index + 2]);
		strNum += String.format("%02x", inByte[index + 1]);
		strNum += String.format("%02x", inByte[index + 0]);
		number = (int)Long.parseLong(strNum, 16);

		return number;
	} 
	
	private static int generateKey()
	{
		return swap32((int)(System.currentTimeMillis() / 1000));
	}
	
	private static int swap32(int n)
	{
		byte [] b = new byte[4];
		b[0] = (byte)(n & 0xff);
		b[1] = (byte)(n >>> 8 & 0xff);
		b[2] = (byte)(n >>> 16 & 0xff);
		b[3] = (byte)(n >>> 24 & 0xff);
		return ((int)b[3] + (b[2] << 8) + (b[1] << 16) + (b[0] << 24));
	}
	
	public static void main(String args[]) {
		
		String msg = "";
		String stra = Encrypt(msg);
		String strb = Decrypt(stra);
		System.out.println(stra + " \n" + strb);
	
	}
}
