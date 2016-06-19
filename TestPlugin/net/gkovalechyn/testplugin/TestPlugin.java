/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.testplugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author gkovalechyn
 */
public class TestPlugin implements Listener{
    private final JavaPlugin parent;
    
    public TestPlugin(JavaPlugin parent){
        this.parent = parent;
    }
    
    public void onEnable(){
        parent.getLogger().info("Enabling the test plugin.");
        
        parent.getLogger().info("Registering join event.");
        parent.getServer().getPluginManager().registerEvents(this, parent);
        
        parent.getLogger().info("Registering new /test command.");
        if (this.registerCommand()){
            parent.getLogger().info("Registered the /test command :) .");
        }else{
            parent.getLogger().info("Failed to register the command :c.");
        }
        parent.getLogger().info("Test plugin enabled!");
    }
    
    public void onDisable(){
        parent.getLogger().info("Test plugin disabled!");
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent evt){
        evt.getPlayer().sendMessage("Hello from the test plugin!");
    }
    
    private boolean registerCommand(){
        CommandMap commandMap = null;
        Command cmd = new Command("test", "Test command", "", new ArrayList<>()) {
            
            @Override
            public boolean execute(CommandSender sender, String string, String[] strings) {
                sender.sendMessage("Test command.");
                return true;
            }
        };
        
        try{
            Field commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (CommandMap) commandMapField.get(this.parent.getServer().getPluginManager());
        }catch(NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException e){
            this.parent.getLogger().severe(e.getMessage());
            return false;
        }
        
        commandMap.register("test", cmd);
        return true;
    }
}
