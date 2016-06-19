/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.pluginencryptor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.security.Key;
import java.util.Base64;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * @author gkovalechyn
 */
public class Security {

    public static void encrypt(File src, File dest, Key key, IvParameterSpec iv) throws Exception {
        JarFile jarFile = new JarFile(src);
        JarOutputStream out = new JarOutputStream(new FileOutputStream(dest));

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] buffer = new byte[1024];
        int bytesRead;

        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        
        for (Enumeration<JarEntry> enumeration = jarFile.entries(); enumeration.hasMoreElements();) {
            JarEntry entry = enumeration.nextElement();
            BufferedInputStream in = new BufferedInputStream(jarFile.getInputStream(entry));
            ZipEntry destinationZip = new ZipEntry(entry.getName());
            byte[] cipherReturn;

            out.putNextEntry(destinationZip);

            if (!entry.getName().endsWith(".class")) {

                while ((bytesRead = in.read(buffer)) > 0) {//Dont care about the folders
                    out.write(buffer, 0, bytesRead);
                }
            } else {
                while ((bytesRead = in.read(buffer)) > 0) {
                    if ((cipherReturn = cipher.update(buffer, 0, bytesRead)) != null) {
                        out.write(cipherReturn);
                    }
                }

                cipherReturn = cipher.doFinal();
                out.write(cipherReturn);
            }
        }

        out.close();
    }

    public static void decrypt(File src, File dest, Key key, IvParameterSpec iv) throws Exception {
        JarFile jarFile = new JarFile(src);
        JarOutputStream out = new JarOutputStream(new FileOutputStream(dest));

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] buffer = new byte[1024];
        int bytesRead;

        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        for (Enumeration<JarEntry> enumeration = jarFile.entries(); enumeration.hasMoreElements();) {
            JarEntry entry = enumeration.nextElement();
            BufferedInputStream in = new BufferedInputStream(jarFile.getInputStream(entry));

            ZipEntry destinationZip = new ZipEntry(entry.getName());
            byte[] cipherReturn;

            out.putNextEntry(destinationZip);

            if (!entry.getName().endsWith(".class")) {

                while ((bytesRead = in.read(buffer)) > 0) {//Do not give a fuck about the folders
                    out.write(buffer, 0, bytesRead);
                }
            } else {
                while ((bytesRead = in.read(buffer)) > 0) {
                    if ((cipherReturn = cipher.update(buffer, 0, bytesRead)) != null) {
                        out.write(cipherReturn);
                    }
                }

                cipherReturn = cipher.doFinal();
                out.write(cipherReturn);
            }
        }

        out.close();
    }
}
