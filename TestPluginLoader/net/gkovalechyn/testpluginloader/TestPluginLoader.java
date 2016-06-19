/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.testpluginloader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author gkovalechyn
 */
public class TestPluginLoader extends JavaPlugin{
    private Object plugin;
    
    @Override
    public void onEnable(){
        this.getLogger().info("Trying to load the test plugin.");
        try{
            PluginLoader loader = new PluginLoader(new File(this.getDataFolder(), "TestPlugin.jar"));
            
            if (!loader.initialize()){
                this.getLogger().severe("Failed to initialize.");
                return;
            }
            
            Class<?> clazz = loader.loadClass("net.gkovalechyn.testplugin.TestPlugin");
            Object o = clazz.getDeclaredConstructor(JavaPlugin.class).newInstance(this);
            o.getClass().getDeclaredMethod("onEnable", (Class<?>[]) null).invoke(o, (Object[]) null);
            
            this.plugin = o;
        }catch(IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(TestPluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(TestPluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(TestPluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TestPluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TestPluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(TestPluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void onDisable(){
        try {
            this.plugin.getClass().getDeclaredMethod("onDisable", (Class<?>[]) null).invoke(plugin, (Object[]) null);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(TestPluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(TestPluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TestPluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TestPluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(TestPluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
