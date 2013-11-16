package com.github.lyokofirelyte.Checkers.Game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.github.lyokofirelyte.Checkers.CMain;
import com.github.lyokofirelyte.Checkers.Internal.CPlayer;
import com.github.lyokofirelyte.Checkers.Internal.CSystem;

public class CGameEvent implements Listener {
	
	CMain pl;
	
	String blockList = ":0: :9: :18: :27: :36: :45: :54: :63:";
	List<String> lore = new ArrayList<>();
	
	public CGameEvent(CMain instance){
		pl = instance;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onClose(InventoryCloseEvent e){
		
		if (e.getInventory().getName().contains("GAME")){
			pl.manager.cInvs.put(e.getPlayer().getName(), e.getInventory());
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onClick(InventoryClickEvent e){
		
		if (e.getInventory().getName().contains("Menu") && e.getWhoClicked() instanceof Player && e.getCurrentItem() != null){
			
			e.setCancelled(true);
			Player p = ((Player)e.getWhoClicked());
			
			switch (e.getCurrentItem().getItemMeta().getDisplayName().substring(2).toLowerCase()){
			
				case "start game": start(p); break;
				
				case "your stats": stats(p); break;
				
				case "close": p.closeInventory(); break;
				
				default: break;
			}
		}
		
		if (e.getInventory().getName().contains("GAME") && e.getWhoClicked() instanceof Player){
			
			e.setCancelled(true);
			Player p = ((Player)e.getWhoClicked());
			
			CPlayer player = pl.manager.getCPlayer(p.getName());
			
			// Is it their turn?
			if (!player.getTurn()){
				return;
			}
			
			// Did they click on the wrong piece?
			if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR && e.getCurrentItem().getItemMeta().getDisplayName().contains("AI")){
				return;
			}
			
			// Did they click the side-bar menu?
			if (blockList.contains(":" + e.getSlot() + ":")){
				
				if (e.getSlot() == 0){
					p.closeInventory();
				}	
				
				return;
			}
			
			// Select the piece to move
			if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR && e.getCurrentItem().getItemMeta().getDisplayName().contains("Your")){
				
				player.setSelPiece(e.getSlot());
				player.setSelPieceItem(e.getCurrentItem());
				pl.manager.updateCPlayer(p.getName(), player);
				pl.utils.cMsg(p,  "Selected slot " + e.getSlot());
				return;
			}
			
			// Here's the hard stuff! First we check to see if the slot they clicked is open.
			if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR){
				
				// Now we see if the slot is in a diagional from it.
				if (getDiag(e.getSlot(), player)){
					
					// This is a check to see if we are moving backwards - so we need a king to do that.
					if (getDirection(e.getSlot(), player)){
						
						// Is it not a king?
						if (!player.getSelPieceItem().getItemMeta().hasLore()){
							return;
						} else {
							move(e, player, p);
						}
						
					} else {
						move(e, player, p);
					}
				}
			}	
		}
	}

	public void move(InventoryClickEvent e, CPlayer player, Player p){
		
		if (getJump(e.getSlot(), player)){
			
			if (getDirection(e.getSlot(), player)){
				
			}
		}
		
		e.setCurrentItem(player.getSelPieceItem());
		e.getInventory().setItem(player.getSelPiece(), new ItemStack(Material.AIR, 1));
		player.setSelPiece(0);
		player.setTurn(false);
		
		if (e.getSlot() > 0 && e.getSlot() <= 8){
			e.getCurrentItem().getItemMeta().setLore(lore);
			e.getCurrentItem().addEnchantment(Enchantment.DURABILITY, 10);
		}
		
		pl.manager.updateCPlayer(p.getName(), player);
	}
	
	public void start(Player p){
		
		if (!lore.contains("King")){
			lore.add("King");
		}
		
		if (pl.manager.getCPlayer(p.getName()) == null){
			pl.fileManager.newCPlayer(p.getName());
		}
	
		if (pl.manager.getCSystem(p.getName()) == null){
			pl.manager.cSystems.put(p.getName(), new CSystem(p.getName()));
			p.openInventory(pl.manager.cInvs.get("default"));
			
			CPlayer player = pl.manager.getCPlayer(p.getName());
			player.setTurn(true);
			pl.manager.updateCPlayer(p.getName(), player);
			return;
		}
		
		p.openInventory(pl.manager.cInvs.get(p.getName()));
	}
	
	public void stats(Player p){

		if (pl.manager.getCPlayer(p.getName()) == null){
			pl.fileManager.newCPlayer(p.getName());
		}
		
		CPlayer player = pl.manager.getCPlayer(p.getName());
		
		int wins = player.getWins();
		int losses = player.getLosses();
		int score = player.getScore();
		
		if (wins == 0 && losses == 0 && score == 0){
			pl.utils.cMsg(p, "You have no records!");
			return;
		}
		
		pl.utils.cMsg(p, "Wins: " + wins);
		pl.utils.cMsg(p, "Losses: " + losses);
		pl.utils.cMsg(p, "Score: " + score);
	}
	
	public Boolean getDiag(int slot, CPlayer player){
		
		if (slot == player.getSelPiece() + 10 || slot == player.getSelPiece() - 10
			|| slot == player.getSelPiece() + 8 || slot == player.getSelPiece() - 8
			|| slot == player.getSelPiece() + 20 || slot == player.getSelPiece() - 20
			|| slot == player.getSelPiece() + 16 || slot == player.getSelPiece() - 16){
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getDirection(int slot, CPlayer player) {

		if (slot == player.getSelPiece() + 10 || slot == player.getSelPiece() + 8
			|| slot == player.getSelPiece() + 16 || slot == player.getSelPiece() + 20){
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getJump(int slot, CPlayer player){
		
		if (slot == player.getSelPiece() + 20 || slot == player.getSelPiece() - 20
			|| slot == player.getSelPiece() + 16 || slot == player.getSelPiece() - 16){
			return true;
		} else {
			return false;
		}
	}
}
