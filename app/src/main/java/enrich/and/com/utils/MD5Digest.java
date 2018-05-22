package enrich.and.com.utils;

import java.security.MessageDigest;

public class MD5Digest {

    public static String generateMD5(String original) throws Exception {

        String md5 = "";

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(original.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }

        md5 = sb.toString();
        System.out.println("digested(hex):" + md5);

        return md5;
    }

}