 package com.github.lyokofirelyte.Checkers.Game;
 
import com.github.lyokofirelyte.Checkers.CMain;
import com.github.lyokofirelyte.Checkers.Internal.CPlayer;
import com.github.lyokofirelyte.Checkers.Internal.CSystem;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CGameEvent implements Listener {
	

	CMain pl;
	String blockList = ":0: :9: :18: :27: :36: :45: :54: :63:"; // DANGER ZONE!@#$^!
	Inventory inv;
	String black = ":2: :4: :6: :8: :10: :12: :14: :16: :20: :22: :24: :26:";
	String red = ":70: :68: :66: :64: :62: :60: :58: :56: :52: :50: :48: :46:";
	List<String> lore = new ArrayList<>();
	List<String> blockListTwo = new ArrayList<>();
 
	public CGameEvent(CMain instance) {
		pl = instance;
	}
 
	@EventHandler(priority=EventPriority.LOWEST)
	public void onClose(InventoryCloseEvent e){
		
	   if (e.getInventory().getName().contains("GAME")){
		   pl.manager.cInvs.put(e.getPlayer().getName(), e.getInventory());
	   }
	}
	
	private void reset(Player p){ 
		
		Inventory inv2 = Bukkit.createInventory(null, 72, "§aIN-GAME");
		     
		for (int x = 0; x < 72; x++){
		    	 
			 if (black.contains(":" + x + ":")){
			     inv2.setItem(x, pl.utils.quickItem("§3AI Piece", 15));
			 } else if (red.contains(":" + x + ":")) {
			     inv2.setItem(x, pl.utils.quickItem("§3Your Piece", 14));
			 }
		 }
			       
		inv2.setItem(0, pl.utils.quickItem("§4Exit", 0));
		pl.manager.cInvs.put(p.getName(), inv2);
		pl.utils.cMsg(p, "RESET!");
		pl.manager.getCPlayer(p.getName()).setRawr(false); // re-init reset allowance
		pl.manager.getCPlayer(p.getName()).setPiecesStolen(0);
		pl.manager.getCSystem(p.getName()).setPiecesStolen(0);
		pl.manager.updateCPlayer(p.getName(), pl.manager.getCPlayer(p.getName()));
		pl.manager.updateCSystem(p.getName(), pl.manager.getCSystem(p.getName()));
	}
 
	@EventHandler(priority=EventPriority.LOWEST)
	public void onClick(InventoryClickEvent e){ // fired when you click something
	   
		if ((e.getInventory().getName().contains("Menu")) && ((e.getWhoClicked() instanceof Player)) && (e.getCurrentItem() != null) && (e.getCurrentItem().hasItemMeta()) && e.getCurrentItem().getItemMeta().hasDisplayName()){
			
			e.setCancelled(true);
			Player p = (Player)e.getWhoClicked();
			
			switch (e.getCurrentItem().getItemMeta().getDisplayName().substring(2).toLowerCase()) { 

	       		case "start game": start(p); break;
	       		
	       		case "close": p.closeInventory(); break;
	       		
	       		case "your stats": stats(p); break;
	       		
	       		case "reset game": reset(p); break;
	       		
	       		default: break;
			}
		}
       		
       	if ((e.getInventory().getName().contains("GAME")) && ((e.getWhoClicked() instanceof Player))){
       		
           Player p = (Player)e.getWhoClicked();
       	   CPlayer player = pl.manager.getCPlayer(p.getName());
       	   CSystem system = pl.manager.getCSystem(e.getWhoClicked().getName());
       	   
       	   if (system.getPiecesStolen() >= 12){ // system lost
       		   pl.utils.cMsg(p, p.getName() + " has won!");
       		   p.closeInventory();
       		   player.setWins(player.getWins() + 1);
       		   player.setScore(player.getScore() + 10);
       	       pl.manager.updateCPlayer(p.getName(), player);
       	       reset(p);
       		   return;
       	   }
       	   
       	   if (player.getPiecesStolen() >= 12){ // player lost
       		   pl.utils.cMsg(p, "System has won!");
       		   p.closeInventory();
       		   player.setLosses(player.getLosses() + 1);
       		   player.setScore(player.getScore() - 5);
       		   pl.manager.updateCPlayer(p.getName(), player);
       		   reset(p);
       		   return;
       	   }
       	   
       	   
     	   if (!player.getRawr()){ // reset check

         	   int y = -1;
    	   
	    	   for (ItemStack i : e.getInventory()){
	    		   y++;
	    		   if (i == null){
	    			   e.getInventory().setItem(y, new ItemStack(Material.GLASS, 1));
	    		   }
	    	   }
	    	   
	    	   for (int xX = 0; xX > -21; xX--){
	    		   blockListTwo.add(":" + xX + ":");
	    	   }
	    	   
	    	   for (int xX = 72; xX < 100; xX++){
	    		   blockListTwo.add(":" + xX + ":");
	    	   }
	    	   
	    	   String[] meh = blockList.split(" ");
	    	   
	    	   for (int x = 0; x < 8; x++){
	    		   blockListTwo.add(meh[x]);
	    		   if (x != 0){
		    		   e.getInventory().getItem(Integer.parseInt(meh[x].replaceAll(":", ""))).setType(Material.GRASS);
	    		   }
	    	   }
	    	   
	    	   player.setRawr(true);
	    	   pl.manager.updateCPlayer(p.getName(), player);
     	   	}
     	   
     	     
    	   for (int x = 1; x < 9; x++) {
    		   
    		   if (!e.getInventory().getItem(x).getType().equals(Material.GLASS)){
    			   if (e.getInventory().getItem(x).getItemMeta().getDisplayName().contains("Your")){
    				   e.getInventory().getItem(x).setType(Material.APPLE); 
    			   }
    		   }
    	   }
    	   
    	   for (int x = 64; x < 71; x++) {
    		   
    		   if (!e.getInventory().getItem(x).getType().equals(Material.GLASS)){
    			   if (e.getInventory().getItem(x).getItemMeta().getDisplayName().contains("AI")){
    				   e.getInventory().getItem(x).setType(Material.BAKED_POTATO);
    			   }
    		   }
    	   }
       		
       		e.setCancelled(true);
       		
       		if ((e.getCurrentItem() != null) && (e.getCurrentItem().getType() != Material.GLASS) && (e.getCurrentItem().getItemMeta().getDisplayName().contains("AI"))) {
       			return;
       		}
       		
       		if (blockList.contains(":" + e.getSlot() + ":")){
       			
       			if (e.getSlot() == 0) {
       				p.closeInventory();
       			}
       			
       			return;
       		}
       		
       		if ((e.getCurrentItem() != null) && (e.getCurrentItem().getType() != Material.GLASS) && (e.getCurrentItem().getItemMeta().getDisplayName().contains("Your"))){
       			
		        player.setSelPiece(e.getSlot());
		        player.setSelPieceItem(e.getCurrentItem());
		        pl.manager.updateCPlayer(p.getName(), player);
		        pl.utils.cMsg(p, "Selected slot " + e.getSlot());
		        return;
       		}
       		
       		if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType() == Material.GLASS)){ // Are you moving to a free space?
       			
       			if ((e.getSlot() == player.getSelPiece() + 16) || (e.getSlot() == player.getSelPiece() - 16) || // ALL VALID MOVES, SPEEDS UP CHECKING
       				(e.getSlot() == player.getSelPiece() + 20) || (e.getSlot() == player.getSelPiece() - 20) || // IF YOU DIDN'T CHOOSE A VALID ONE
       				(e.getSlot() == player.getSelPiece() + 10) || (e.getSlot() == player.getSelPiece() - 10) ||
       				(e.getSlot() == player.getSelPiece() + 8)  || (e.getSlot() == player.getSelPiece() - 8)){
       				// Redundant but necessary checks
	       			if ((e.getSlot() == player.getSelPiece() + 16) || (e.getSlot() == player.getSelPiece() + 20) || (e.getSlot() == player.getSelPiece() + 8) || (e.getSlot() == player.getSelPiece() + 10)){ // Did you try a king jump?
	       				
	       				if (!player.getSelPieceItem().getType().equals(Material.APPLE)) { // Are you a king?
	       					return;
	       				}
	       			}
	       			
	       			if (e.getSlot() == player.getSelPiece() + 16){ // Did you try a king jump?
       					
       					if (e.getInventory().getItem(player.getSelPiece() + 8).getType() != Material.GLASS && // Is the middle space empty?
       						e.getInventory().getItem(player.getSelPiece() + 8).getItemMeta().getDisplayName().contains("AI")){ // Is it an AI piece?
       						e.getInventory().setItem(player.getSelPiece() + 8, new ItemStack(Material.GLASS)); // Remove from board
       						system.setPiecesStolen(system.getPiecesStolen() + 1);
       						pl.manager.updateCSystem(e.getWhoClicked().getName(), system);
       					} else {
       						return; // abort!
       					}
	       			}
       					
    	       		if (e.getSlot() == player.getSelPiece() + 20){ // Did you try a king jump?
           				
    	       			if (e.getInventory().getItem(player.getSelPiece() + 10).getType() != Material.GLASS && // Is the middle space empty?
           					e.getInventory().getItem(player.getSelPiece() + 10).getItemMeta().getDisplayName().contains("AI")){ // Is it an AI piece?
    	       				e.getInventory().setItem(player.getSelPiece() + 10, new ItemStack(Material.GLASS)); // Remove from board
       						system.setPiecesStolen(system.getPiecesStolen() + 1);
       						pl.manager.updateCSystem(e.getWhoClicked().getName(), system);
           				} else {
           					return;
           				}
    	       		}
    	       		
	       			if (e.getSlot() == player.getSelPiece() - 16){ // Did you try a normal jump..?
       					
       					if (e.getInventory().getItem(player.getSelPiece() - 8).getType() != Material.GLASS && // Is the middle space empty?
       						e.getInventory().getItem(player.getSelPiece() - 8).getItemMeta().getDisplayName().contains("AI")){ // Is it an AI piece?
       						e.getInventory().setItem(player.getSelPiece() - 8, new ItemStack(Material.GLASS)); // Remove from board
       						system.setPiecesStolen(system.getPiecesStolen() + 1);
       						pl.manager.updateCSystem(e.getWhoClicked().getName(), system);
       					} else {
       						return; // abort!
       					}
	       			}
       					
    	       		if (e.getSlot() == player.getSelPiece() - 20){ // Did you try a normal jump?
           				
    	       			if (e.getInventory().getItem(player.getSelPiece() - 10).getType() != Material.GLASS && // Is the middle space empty?
           					e.getInventory().getItem(player.getSelPiece() - 10).getItemMeta().getDisplayName().contains("AI")){ // Is it an AI piece?
           					e.getInventory().setItem(player.getSelPiece() - 10, new ItemStack(Material.GLASS)); // Remove from board
       						system.setPiecesStolen(system.getPiecesStolen() + 1);
       						pl.manager.updateCSystem(e.getWhoClicked().getName(), system);
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
	   e.getInventory().setItem(player.getSelPiece(), new ItemStack(Material.GLASS, 1));
	   player.setSelPiece(0);
	   player.setTurn(false);
     
	   pl.manager.updateCPlayer(p.getName(), player); // save changes to hashmap

	   aiMove(e, player); // THE AI BEGINS! MUWHAH! >.>

   }

   private void aiMove(InventoryClickEvent e, CPlayer player){

	   int x = -1;
	   
	   for (ItemStack i : e.getInventory().getContents()){

		   x++;
		   
	  		if (i != null && i.hasItemMeta() && i.getItemMeta().getDisplayName().contains("AI") && !blockList.contains(":" + x + ":")){

	       			if (!blockListTwo.contains(":" + (x - 16) + ":") && e.getInventory().getItem(x - 16) != null && e.getInventory().getItem(x - 16).getType().equals(Material.GLASS) && e.getInventory().getItem(x).getType().equals(Material.BAKED_POTATO)){ // Did you try a king jump?
	
	   					if (!blockListTwo.contains(":" + (x - 8) + ":") && !e.getInventory().getItem(x - 8).getType().equals(Material.GLASS) && e.getInventory().getItem(x - 8).getItemMeta().getDisplayName().contains("Your")) { 
	   						e.getInventory().setItem(x - 8, new ItemStack(Material.GLASS)); // Remove from board
       						player.setPiecesStolen(player.getPiecesStolen() + 1);
       						pl.manager.updateCPlayer(e.getWhoClicked().getName(), player);
	   	   					finishTurn(e.getWhoClicked().getName(), player, e.getInventory(), i, -16, x);
	   	   					return;
	   					} 
	       			}
	       			
	       			if (!blockListTwo.contains(":" + (x - 20) + ":") && e.getInventory().getItem(x - 20) != null && e.getInventory().getItem(x - 20).getType().equals(Material.GLASS) && e.getInventory().getItem(x).getType().equals(Material.BAKED_POTATO)){ // Did you try a king jump?
	       				
	   					if (!blockListTwo.contains(":" + (x - 10) + ":") && !e.getInventory().getItem(x - 10).getType().equals(Material.GLASS) && e.getInventory().getItem(x - 10).getItemMeta().getDisplayName().contains("Your")) { 
	   						e.getInventory().setItem(x - 10, new ItemStack(Material.GLASS)); // Remove from board
       						player.setPiecesStolen(player.getPiecesStolen() + 1);
       						pl.manager.updateCPlayer(e.getWhoClicked().getName(), player);
	   	   					finishTurn(e.getWhoClicked().getName(), player, e.getInventory(), i, -20, x);
	   	   					return;
	   					} 
	       			}

	       			if (!blockListTwo.contains(":" + (x + 16) + ":") && e.getInventory().getItem(x + 16) != null && e.getInventory().getItem(x + 16).getType().equals(Material.GLASS)){ // Did you try a normal jump..?
	   					
	   					if (!blockListTwo.contains(":" + (x + 8) + ":") && !e.getInventory().getItem(x + 8).getType().equals(Material.GLASS) && // Is the middle space empty?
	   						e.getInventory().getItem(x + 8).getItemMeta().getDisplayName().contains("Your")){
	   						e.getInventory().setItem(x + 8, new ItemStack(Material.GLASS)); // Remove from board
       						player.setPiecesStolen(player.getPiecesStolen() + 1);
       						pl.manager.updateCPlayer(e.getWhoClicked().getName(), player);
	   						finishTurn(e.getWhoClicked().getName(), player, e.getInventory(), i, 16, x);
	   	   					return;
	   					} 
	       			}
	   					
	       			if (!blockListTwo.contains(":" + (x + 20) + ":") && e.getInventory().getItem(x + 20) != null && e.getInventory().getItem(x + 20).getType().equals(Material.GLASS)){ // Did you try a normal jump..?
	   					
	   					if (!blockListTwo.contains(":" + (x + 10) + ":") && !e.getInventory().getItem(x + 10).getType().equals(Material.GLASS) && // Is the middle space empty?
	   						e.getInventory().getItem(x + 10).getItemMeta().getDisplayName().contains("Your")){
	   						e.getInventory().setItem(x + 10, new ItemStack(Material.GLASS)); // Remove from board
       						player.setPiecesStolen(player.getPiecesStolen() + 1);
       						pl.manager.updateCPlayer(e.getWhoClicked().getName(), player);
	   						finishTurn(e.getWhoClicked().getName(), player, e.getInventory(), i, 20, x);
	   	   					return;
	   					} 
	       			}
	       			
	       			if (!blockListTwo.contains(":" + (x + 8) + ":") && e.getInventory().getItem(x + 8) != null && e.getInventory().getItem(x + 8).getType().equals(Material.GLASS)){
	       				finishTurn(e.getWhoClicked().getName(), player, e.getInventory(), i, 8, x);
	       				return;
	       			}
	       			
	       			if (!blockListTwo.contains(":" + (x + 10) + ":") && e.getInventory().getItem(x + 10) != null && e.getInventory().getItem(x + 10).getType().equals(Material.GLASS)){
	       				finishTurn(e.getWhoClicked().getName(), player, e.getInventory(), i, 10, x);
	       				return;
	       			}
	       			
	       			if (!blockListTwo.contains(":" + (x - 8) + ":") && e.getInventory().getItem(x - 8) != null && e.getInventory().getItem(x - 8).getType().equals(Material.GLASS) && e.getInventory().getItem(x).getType().equals(Material.BAKED_POTATO)){
	       				finishTurn(e.getWhoClicked().getName(), player, e.getInventory(), i, -8, x);
	       				return;
	       			}
	       			
	       			if (!blockListTwo.contains(":" + (x - 10) + ":") && e.getInventory().getItem(x - 10) != null && e.getInventory().getItem(x - 10).getType().equals(Material.GLASS) && e.getInventory().getItem(x).getType().equals(Material.BAKED_POTATO)){
	       				finishTurn(e.getWhoClicked().getName(), player, e.getInventory(), i, -10, x);
	       				return;
	       			}
	  		}
	   	}
   }
   
   public void finishTurn(String player, CPlayer p, Inventory e, ItemStack i, int num, int x){
	   e.setItem(x + num, i);
	   e.setItem(x, new ItemStack(Material.GLASS, 1));
	   p.setTurn(true);
	   pl.manager.updateCPlayer(player, p);
   }
 
   public void start(Player p){
	   
	   if (!lore.contains("King")) {
		   lore.add("King");
	   }
     
	   if (pl.manager.getCPlayer(p.getName()) == null) {
		   pl.fileManager.newCPlayer(p.getName());
	   }
	   
       CPlayer player = pl.manager.getCPlayer(p.getName());
       pl.manager.updateCPlayer(p.getName(), player);
	
	   if (pl.manager.getCSystem(p.getName()) == null) {
	       pl.manager.cSystems.put(p.getName(), new CSystem(p.getName()));
	       pl.manager.cInvs.put(p.getName(), pl.manager.cInvs.get("default")); // pull new game inv
	   }
 
       p.openInventory(pl.manager.cInvs.get(p.getName())); // resume game
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