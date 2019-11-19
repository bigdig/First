package com.tfzq.yjgl;

import java.security.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.crypto.*;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 16-8-23
 * Time: 上午10:48
 * To change this template use File | Settings | File Templates.
 */
public class DESPlus {
    private static String strDefaultKey = "national";
    private Cipher encryptCipher = null;
    private Cipher decryptCipher = null;

    public static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;   // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];    // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;    }    // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");    }
            sb.append(Integer.toString(intTmp, 16));   }
        return sb.toString();  }


    public static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);   }
        return arrOut;  }


    /**   * 默认构造方法，使用默认密钥   *   * @throws Exception   */
    public DESPlus() throws Exception {
        this(strDefaultKey);
    }


    /**   * 指定密钥构造方法   *   * @param strKey   *    指定的密钥   * @throws Exception   */
    public DESPlus(String strKey) throws Exception {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        Key key = getKey(strKey.getBytes());

        encryptCipher = Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        decryptCipher = Cipher.getInstance("DES");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }


    public byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);  }

    public String encrypt(String strIn) throws Exception {
        return byteArr2HexStr(encrypt(strIn.getBytes()));  }

    public byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);  }
    public String decrypt(String strIn) throws Exception {
        return new String(decrypt(hexStr2ByteArr(strIn)));  }



    private Key getKey(byte[] arrBTmp) throws Exception {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];
        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];   }
        // 生成密钥
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
        return key;  }


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            String loginname="admin";
            String test = "//DESencrypt"+loginname;
            //DESPlus des = new DESPlus();//默认密钥
            DESPlus des = new DESPlus("%#[ekpSSOhsicpweb]@!?"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));//自定义密钥
            System.out.println("加密前的字符："+test);
            System.out.println("加密后的字符："+des.encrypt(test));
            System.out.println("解密后的字符："+des.decrypt(des.encrypt(test)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
