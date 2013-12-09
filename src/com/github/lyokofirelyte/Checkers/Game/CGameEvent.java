 package com.github.lyokofirelyte.Checkers.Game;
 
import com.github.lyokofirelyte.Checkers.CMain;
import com.github.lyokofirelyte.Checkers.Internal.CPlayer;
import com.github.lyokofirelyte.Checkers.Internal.CSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class CGameEvent implements Listener {
	

	CMain pl;
	String blockList = ":0: :9: :18: :27: :36: :45: :54: :63:"; // DANGER ZONE!@#$^!
	List<String> lore = new ArrayList<>();
 
	public CGameEvent(CMain instance) {
		pl = instance;
	}
 
	@EventHandler(priority=EventPriority.LOWEST)
	public void onClose(InventoryCloseEvent e){
		
	   if (e.getInventory().getName().contains("GAME")){
		   pl.manager.cInvs.put(e.getPlayer().getName(), e.getInventory());
	   }
	}
 
	@EventHandler(priority=EventPriority.LOWEST)
	public void onClick(InventoryClickEvent e){
	   
		if ((e.getInventory().getName().contains("Menu")) && ((e.getWhoClicked() instanceof Player)) && (e.getCurrentItem() != null)){
			
			e.setCancelled(true);
			Player p = (Player)e.getWhoClicked();
			
			switch (e.getCurrentItem().getItemMeta().getDisplayName().substring(2).toLowerCase()) { 

	       		case "start game": start(p); break;
	       		
	       		case "close": p.closeInventory(); break;
	       		
	       		case "your stats": stats(p); break;
	       		
	       		default: break;
			}
		}
       		

       	if ((e.getInventory().getName().contains("GAME")) && ((e.getWhoClicked() instanceof Player))){
       		
       	
       		e.setCancelled(true);
       		Player p = (Player)e.getWhoClicked();
       		CPlayer player = pl.manager.getCPlayer(p.getName());
       		
       		if (!player.getTurn()) {
       			return;
       		}
       		
       		if ((e.getCurrentItem() != null) && (e.getCurrentItem().getType() != Material.AIR) && (e.getCurrentItem().getItemMeta().getDisplayName().contains("AI"))) {
       			return;
       		}
       		
       		if (blockList.contains(":" + e.getSlot() + ":")){
       			
       			if (e.getSlot() == 0) {
       				p.closeInventory();
       			}
       			
       			return;
       		}
       		
       		if ((e.getCurrentItem() != null) && (e.getCurrentItem().getType() != Material.AIR) && (e.getCurrentItem().getItemMeta().getDisplayName().contains("Your"))){
       			
		        player.setSelPiece(e.getSlot());
		        player.setSelPieceItem(e.getCurrentItem());
		        pl.manager.updateCPlayer(p.getName(), player);
		        pl.utils.cMsg(p, "Selected slot " + e.getSlot());
		        return;
       		}
       		
       		if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType() == Material.AIR)){ // Are you moving to a free space?
       			
       			if ((e.getSlot() == player.getSelPiece() + 16) || (e.getSlot() == player.getSelPiece() - 16) || // ALL VALID MOVES, SPEEDS UP CHECKING
       				(e.getSlot() == player.getSelPiece() + 20) || (e.getSlot() == player.getSelPiece() - 20) || // IF YOU DIDN'T CHOOSE A VALID ONE
       				(e.getSlot() == player.getSelPiece() + 10) || (e.getSlot() == player.getSelPiece() - 10) ||
       				(e.getSlot() == player.getSelPiece() + 8)  || (e.getSlot() == player.getSelPiece() - 8)){
       				// Redundant but necessary checks
	       			if ((e.getSlot() == player.getSelPiece() + 16) || (e.getSlot() == player.getSelPiece() + 20)){ // Did you try a king jump?
	       				
	       				if (!player.getSelPieceItem().getItemMeta().hasLore()) { // Are you a king?
	       					return;
	       				}
	       			}
	       			
	       			if (e.getSlot() == player.getSelPiece() + 16){ // Did you try a king jump?
       					
       					if (e.getInventory().getItem(player.getSelPiece() + 8) != null && // Is the middle space empty?
       						e.getInventory().getItem(player.getSelPiece() + 8).getItemMeta().getDisplayName().contains("AI")){ // Is it an AI piece?
       						e.getInventory().getItem(player.getSelPiece() + 8).setType(Material.AIR); // Remove from board
       					} else {
       						return; // abort!
       					}
	       			}
       					
    	       		if (e.getSlot() == player.getSelPiece() + 20){ // Did you try a king jump?
           				
    	       			if (e.getInventory().getItem(player.getSelPiece() + 10) != null && // Is the middle space empty?
           					e.getInventory().getItem(player.getSelPiece() + 10).getItemMeta().getDisplayName().contains("AI")){ // Is it an AI piece?
           					e.getInventory().getItem(player.getSelPiece() + 10).setType(Material.AIR); // Remove from board
           				} else {
           					return;
           				}
    	       		}
    	       		
	       			if (e.getSlot() == player.getSelPiece() - 16){ // Did you try a normal jump..?
       					
       					if (e.getInventory().getItem(player.getSelPiece() - 8) != null && // Is the middle space empty?
       						e.getInventory().getItem(player.getSelPiece() - 8).getItemMeta().getDisplayName().contains("AI")){ // Is it an AI piece?
       						e.getInventory().getItem(player.getSelPiece() - 8).setType(Material.AIR); // Remove from board
       					} else {
       						return; // abort!
       					}
	       			}
       					
    	       		if (e.getSlot() == player.getSelPiece() - 20){ // Did you try a normal jump?
           				
    	       			if (e.getInventory().getItem(player.getSelPiece() - 10) != null && // Is the middle space empty?
           					e.getInventory().getItem(player.getSelPiece() - 10).getItemMeta().getDisplayName().contains("AI")){ // Is it an AI piece?
           					e.getInventory().getItem(player.getSelPiece() - 10).setType(Material.AIR); // Remove from board
           				} else {
           					return;
           				}
    	       		}
    	       		
	       			move(e, player, p); // Move your piece.
       			}
       		}
       	}
	}
 
   public void move(InventoryClickEvent e, CPlayer player, Player p){
	   
	   e.setCurrentItem(player.getSelPieceItem());
	   e.getInventory().setItem(player.getSelPiece(), new ItemStack(Material.AIR, 1));
	   player.setSelPiece(0);
	   player.setTurn(false);
     
	   if ((e.getSlot() > 0) && (e.getSlot() <= 8)) { // If it's a king we will make it glow. #whynot
		   e.getCurrentItem().getItemMeta().setLore(lore);
		   e.getCurrentItem().addEnchantment(Enchantment.DURABILITY, 10);
	   }
     
	   pl.manager.updateCPlayer(p.getName(), player); // save changes to hashmap
	   
	   Bukkit.broadcastMessage("Ai start");
	   aiMove(e, player); // THE AI BEGINS! MUWHAH! >.>
   }
   
   private void aiMove(InventoryClickEvent e, CPlayer player){ // no other class should make the AI move...
	   
	   Random rand = new Random();
	   int selection = rand.nextInt((e.getInventory().getSize()) - 1);
	   int x = -1;

	   for (ItemStack i : e.getInventory()){
		   
		   x++;
	   
	  		if (i != null && i.hasItemMeta() && i.getItemMeta().getDisplayName().contains("AI")){ // Are you moving to a free space?
	  			
	  			if (e.getInventory().getItem(selection).getType() != Material.AIR){
	  				aiMove(e, player);
	  				return;
	  			}
	   			
	   			if ((selection == x + 16) || (selection == x - 16) || // ALL VALID MOVES, SPEEDS UP CHECKING
	   				(selection == x + 20) || (selection == x - 20) || // IF YOU DIDN'T CHOOSE A VALID ONE
	   				(selection == x + 10) || (selection == x - 10) ||
	   				(selection == x + 8)  || (selection == x - 8)){
	   				// Redundant but necessary checks
	   				Bukkit.broadcastMessage("valid ai move");
	       			if ((selection == x - 16) || (selection == x - 20)){ // Did you try a king jump?
	       				
	       				if (!e.getInventory().getItem(x).getItemMeta().hasLore()) { // Are you a king?
	       					aiMove(e, player);
	       					return;
	       				}
	       			}
	       			
	       			if (blockList.contains(":" + selection + ":")){
	       				aiMove(e, player);
	       				Bukkit.broadcastMessage("block list");
	       				return;
	       			}
	       			
	       			if (selection == x - 16){ // Did you try a king jump?
	   					
	   					if (e.getInventory().getItem(x - 8) != null && // Is the middle space empty?
	   						e.getInventory().getItem(x - 8).getItemMeta().getDisplayName().contains("Your")){ 
	   						e.getInventory().getItem(x - 8).setType(Material.AIR); // Remove from board
	   						Bukkit.broadcastMessage("s");
	   					} else {
	   						aiMove(e, player);
	   						return; 
	   					}
	       			}
	   					
		       		if (selection == x - 20){ // Did you try a king jump?
	       				
		       			if (e.getInventory().getItem(x - 10) != null && // Is the middle space empty?
	       					e.getInventory().getItem(x - 10).getItemMeta().getDisplayName().contains("Your")){ 
	       					e.getInventory().getItem(x - 10).setType(Material.AIR); // Remove from board
	       					Bukkit.broadcastMessage("s");
	       				} else {
	       					aiMove(e, player);
	       					return;
	       				}
		       		}
		       		
	       			if (selection == x + 16){ // Did you try a normal jump..?
	   					
	   					if (e.getInventory().getItem(x + 8) != null && // Is the middle space empty?
	   						e.getInventory().getItem(x + 8).getItemMeta().getDisplayName().contains("Your")){
	   						e.getInventory().getItem(x + 8).setType(Material.AIR); // Remove from board
	   						Bukkit.broadcastMessage("s");
	   					} else {
	   						aiMove(e, player);
	   						return; 
	   					}
	       			}
	   					
		       		if (selection == x + 20){ // Did you try a normal jump?
	       				
		       			if (e.getInventory().getItem(x + 10) != null && // Is the middle space empty?
	       					e.getInventory().getItem(x + 10).getItemMeta().getDisplayName().contains("Your")){ 
	       					e.getInventory().getItem(x + 10).setType(Material.AIR); // Remove from board
	       					Bukkit.broadcastMessage("s");
	       				} else {
	       					aiMove(e, player);
	       					return;
	       				}
		       		}
		       		
		       		e.getInventory().setItem(selection, i);
		       		e.getInventory().setItem(x, new ItemStack(Material.AIR, 1));
		       		player.setTurn(true);
		       		pl.manager.updateCPlayer(e.getWhoClicked().getName(), player);
		       		Bukkit.broadcastMessage("X was " + x + ", selection was " + selection);
		       		return;
	   			}
	   			
	   			aiMove(e, player);
	   			return;
	   		}
	   }
   }
 
   public void start(Player p){
	   
	   if (!lore.contains("King")) {
		   lore.add("King");
	   }
     
	   if (pl.manager.getCPlayer(p.getName()) == null) {
		   pl.fileManager.newCPlayer(p.getName());
	   }
	   
	   
       CPlayer player = pl.manager.getCPlayer(p.getName());
       player.setTurn(true);
       pl.manager.updateCPlayer(p.getName(), player);
	
	   if (pl.manager.getCSystem(p.getName()) == null) {
	       pl.manager.cSystems.put(p.getName(), new CSystem(p.getName()));
	       p.openInventory(pl.manager.cInvs.get("default")); // make new game
	       return;
	   }
 
       p.openInventory(pl.manager.cInvs.get(p.getName())); // resume saved game
   }
 
   public void stats(Player p){
	   
	   if (pl.manager.getCPlayer(p.getName()) == null) {
		   pl.fileManager.newCPlayer(p.getName());
	   }
	   
       CPlayer player = pl.manager.getCPlayer(p.getName());
       
       int wins = player.getWins();
       int losses = player.getLosses();
       int score = player.getScore();
       
       if ((wins == 0) && (losses == 0) && (score == 0)) {
    	   pl.utils.cMsg(p, "You have no records!");
    	   return;
       }
       
	   pl.utils.cMsg(p, "Wins: " + wins);
	   pl.utils.cMsg(p, "Losses: " + losses);
	   pl.utils.cMsg(p, "Score: " + score);
   }
 }