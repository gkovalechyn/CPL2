/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.pluginencryptor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author gkovalechyn
 */
public class PluginEncrypter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 2){
            System.out.println("Arguments: <OriginalPlugin> <DestinationName>");
            return;
        }
        
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        SecureRandom random = new SecureRandom();
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(args[1] + ".key")));
        SecretKey key;
        IvParameterSpec params;
        byte[] temp = new byte[16];
        
        generator.init(random);
        
        key = generator.generateKey();
        random.nextBytes(temp);
        params = new IvParameterSpec(temp);
        
        for(byte b : key.getEncoded()){
            out.write(("(byte) 0x" + String.format("%02X", b) + ", ").getBytes());
        }
        
        out.write('\n');
        for(byte b : params.getIV()){
            out.write(("(byte) 0x" + String.format("%02X", b) + ", ").getBytes());
        }
        
        out.close();
        Security.encrypt(new File(args[0]), new File(args[1]), key, params);
    }
    
}
