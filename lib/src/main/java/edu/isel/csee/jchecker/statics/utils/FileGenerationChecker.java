package edu.isel.csee.jchecker.statics.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class FileGenerationChecker {
    public static byte[] createChecksum(String filename) throws Exception {
        InputStream fis = new FileInputStream(filename);
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");

        int numRead;
        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while(numRead != -1);

        fis.close();
        return complete.digest();
    }

    public static String getMD5Checksum(String filename) throws Exception {
        byte[] b = createChecksum(filename);
        String result = "";

        for(int i = 0; i < b.length; i ++) {
            result += Integer.toString((b[i] & 255) + 256, 16).substring(1);
        }

        return result;
    }

    public static boolean compareChecksum(String hash, String filePath, String workPath) throws Exception {
        if (hash.isEmpty()) {
            return true;
        } else if (filePath == null) {
            return false;
        } else {
            List<String> generationHash = new ArrayList<>();
            String[] hashList = hash.split(",");

            boolean flag = true;

            File f = new File(workPath + "/" + filePath);
            if (f.isFile()) {
                System.out.println("Computed hashcode : " +getMD5Checksum(workPath + "/" + filePath));
                generationHash.add(getMD5Checksum(workPath + "/" + filePath));
            } else {
                System.out.println("Does not exist file : " + workPath + "/" + filePath);
            }

            for(int i = 0; i < hashList.length; i ++) {
                String eachHash = hashList[i];
                if (!generationHash.contains(eachHash.trim())) {
                    flag = false;
                } else {
                    System.out.println("Existing hashcode : " + eachHash);
                }
            }

            return flag;
        }
    }
}
