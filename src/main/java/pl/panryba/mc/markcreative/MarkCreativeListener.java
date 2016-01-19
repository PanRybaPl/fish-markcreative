/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.panryba.mc.markcreative;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author PanRyba.pl
 */
public class MarkCreativeListener implements Listener {

    private final static String CREATIVE_MARKER = ("(przedmiot z trybu CREATIVE)");
    private Set<InventoryAction> markedActions;

    public MarkCreativeListener() {
        this.markedActions = new HashSet<>();
        this.markedActions.add(InventoryAction.PLACE_ALL);
        this.markedActions.add(InventoryAction.PLACE_ONE);
        this.markedActions.add(InventoryAction.PLACE_SOME);
        this.markedActions.add(InventoryAction.CLONE_STACK);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        if(player.isOp()) {
            return;
        }

        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }
        
        ItemStack stack = event.getCurrentItem();
        if (stack == null) {
            return;
        }

        ItemMeta meta = stack.getItemMeta();
        if (meta == null) {
            return;
        }

        if (!meta.hasLore()) {
            return;
        }
        List<String> lore = meta.getLore();
        if (lore == null) {
            return;
        }
        
        if(!lore.contains(CREATIVE_MARKER)) {
            return;
        }
        
        event.setCancelled(true);
        player.sendMessage(ChatColor.RED + "Nie mozesz uzywac tego przedmiotu poniewaz pochodzi on z trybu CREATIVE");
        
        Bukkit.getLogger().info(player.getName() + " tried to use CREATIVE item: " + stack);
    }
    
    @EventHandler
    public void onInventoryCreative(InventoryCreativeEvent event) {
        if (!this.markedActions.contains(event.getAction())) {
            return;
        }

        ItemStack item = event.getCursor();
        if (item == null) {
            return;
        }

        if(event.getWhoClicked().hasPermission("markcreative.nomark")) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        List<String> lore;

        if (meta.hasLore()) {
            lore = meta.getLore();
            if (lore.contains(CREATIVE_MARKER)) {
                return;
            }
        } else {
            lore = new ArrayList<>();
        }

        lore.add(CREATIVE_MARKER);

        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            lore.add("- " + player.getName());
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}
