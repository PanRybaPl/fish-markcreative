/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.panryba.mc.markcreative;

import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author PanRyba.pl
 */
public class Plugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new MarkCreativeListener(), this);
    }
    
}
