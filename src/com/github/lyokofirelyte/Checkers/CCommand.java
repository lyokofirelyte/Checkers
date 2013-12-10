package com.github.lyokofirelyte.Checkers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CCommand implements CommandExecutor {
	
	CMain pl;
	Boolean setup = Boolean.valueOf(false);
	Inventory inv;
	String black = ":2: :4: :6: :8: :10: :12: :14: :16: :20: :22: :24: :26:";
	String red = ":70: :68: :66: :64: :62: :60: :58: :56: :52: :50: :48: :46:";

	public CCommand(CMain instance) {
		pl = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
	  
		if (cmd.getName().equalsIgnoreCase("checkers")){
			
			Player p = (Player)sender;

			if (!setup) {
				setup();
			}
			
			p.openInventory(inv);
		}
		return true;
	}

	private void setup(){
		
		inv = Bukkit.createInventory(null, 9, "§aCheckers Main Menu");
		inv.addItem(pl.utils.makeItem("§bStart Game", "§3New Game", true, Enchantment.DURABILITY, 10, 0, Material.DIAMOND, 1));
		inv.addItem(pl.utils.makeItem("§eYour Stats", "§6View Stats", false, Enchantment.DURABILITY, 10, 0, Material.EMERALD, 1));
		inv.addItem(pl.utils.makeItem("§eReset Game", "§3RESET", false, Enchantment.DURABILITY, 10, 0, Material.TNT, 1));
		inv.addItem(pl.utils.makeItem("§4Close", "§cClose Window", false, Enchantment.DURABILITY, 10, 0, Material.REDSTONE, 1));
		pl.manager.cInvs.put("menu", inv);
		defaultInv();
		setup = true;
	}

	public void defaultInv(){
	  
	     Inventory inv2 = Bukkit.createInventory(null, 72, "§aIN-GAME");
	     
	     for (int x = 0; x < 72; x++){
	    	 
		     if (black.contains(":" + x + ":")){
		         inv2.setItem(x, pl.utils.quickItem("§3AI Piece", 15));
		     } else if (red.contains(":" + x + ":")) {
		         inv2.setItem(x, pl.utils.quickItem("§3Your Piece", 14));
		     }
	     }
		       
		 inv2.setItem(0, pl.utils.quickItem("§4Exit", 0));
		 pl.manager.cInvs.put("default", inv2);
	}
}