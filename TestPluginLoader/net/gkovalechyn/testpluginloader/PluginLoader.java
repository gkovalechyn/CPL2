
/*
 * File:   PluginLoader.java
 * Author: gkovalechyn
 *
 * Created on Jun 18, 2016, 7:03:14 PM
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.gkovalechyn.testpluginloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 *
 * @author gkovalechyn
 */
public class PluginLoader extends ClassLoader{
    
    private final JarFile jarFile;
    private final Map<String, Class> loadedClasses = new HashMap<>();

    public PluginLoader(File jarFile) throws IOException {
        this.jarFile = new JarFile(jarFile, false);
        System.loadLibrary("libCPL" + System.getProperty("sun.arch.data.model"));
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        Class result;
        
        result = this.loadedClasses.get(className);

        if (result == null) {
            result = this.loadClass(className, true);
        }

        if (result == null) {
            throw new ClassNotFoundException("Could not find class: " + className);
        }
        return result;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> clazz = this.loadedClasses.get(name);

        if (clazz == null) {
            clazz = this.loadClass(name, true);
        }

        return clazz;
    }

    @Override
    public Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        String fileInJar;
        ZipEntry entry;

        fileInJar = className.replaceAll("\\.", "/") + ".class";
        entry = this.jarFile.getEntry(fileInJar);
        
        if (entry != null) {
            try (InputStream in = this.jarFile.getInputStream(entry)) {
                byte[] buff = new byte[1024];
                byte[] classBytes = new byte[0];
                int read;
                Class<?> clazz;
                
                while ((read = in.read(buff)) > 0) {
                    byte[] temp = new byte[classBytes.length + read];
                    
                    System.arraycopy(classBytes, 0, temp, 0, classBytes.length);
                    System.arraycopy(buff, 0, temp, classBytes.length, read);
                    
                    classBytes = temp;
                }

                classBytes = this.translateClass(classBytes);

                clazz = this.defineClass(className, classBytes, 0, classBytes.length);
                this.loadedClasses.put(className, clazz);
                
                return clazz;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return super.findSystemClass(className);
        }
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        return this.getClass().getClassLoader().getResourceAsStream(name);
    }
    
    private native byte[] translateClass(byte[] clazz);
    public native boolean initialize();
}
