package com.simple.sharding.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hash算法工具类
 * @author: wuyu
 * @since: 12-8-7 上午10:54
 * @version: 1.0.0
 */
public enum HashAlgorithm {

    
	/**
	 * MD5-based hash algorithm used by ketama.
	 */
	KETAMA_HASH;


    /**
     * 一致性算法
     * @param str
     * @return
     */
	public static long hash(String str) {
        return hash(computeMd5(str),0);
    }

    /**
     * 一致性算法
     * @param digest
     * @param nTime
     * @return
     */
	public static long hash(byte[] digest, int nTime) {
		long rv = ((long) (digest[3+nTime*4] & 0xFF) << 24)
				| ((long) (digest[2+nTime*4] & 0xFF) << 16)
				| ((long) (digest[1+nTime*4] & 0xFF) << 8)
				| (digest[0+nTime*4] & 0xFF);

		return rv & 0xffffffffL; /* Truncate to 32-bits */
	}

	/**
	 * 生成MD5的key值.
	 */
	public static byte[] computeMd5(String k) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 not supported", e);
		}
		md5.reset();
		byte[] keyBytes = null;
		try {
			keyBytes = k.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unknown string :" + k, e);
		}

		md5.update(keyBytes);
		return md5.digest();
	}

    public static void main(String[] args){
        String str = "1016222022012091113382610004000845";
        for(int i=0;i<str.getBytes().length;i++){
            System.out.print(str.getBytes()[i] + ",");
        }
        System.out.println(str.getBytes().length);
         System.out.println(str.getBytes().length);
        System.out.println(hash(str));

          String str1 = "1016222022012091111352710003001314";
        for(int i=0;i<str1.getBytes().length;i++){
            System.out.print(str1.getBytes()[i] + ",");
        }
        System.out.println(str1.getBytes().length);
        System.out.println(hash(str1));

         String str2 = "1016222022012091111352710003001315";
        for(int i=0;i<str2.getBytes().length;i++){
            System.out.print(str2.getBytes()[i] + ",");
        }
        System.out.println(str2.getBytes().length);
        System.out.println(hash(str2));
    }


}

