 package com.github.lyokofirelyte.Checkers.Internal;
 
 import com.github.lyokofirelyte.Checkers.CMain;
 import java.util.ArrayList;
 import java.util.List;
 import org.bukkit.ChatColor;
 import org.bukkit.Material;
 import org.bukkit.enchantments.Enchantment;
 import org.bukkit.entity.Player;
 import org.bukkit.inventory.ItemStack;
 import org.bukkit.inventory.meta.ItemMeta;
 
 public class CUtil {
	 
	CMain pl;
	ItemStack i;
	ItemMeta iMeta;
	List<String> loreSplit;
 
	public CUtil(CMain instance){
    pl = instance;
   	}
 
	public ItemStack makeItem(String dispName, String lore, Boolean e, Enchantment enchant, int amplifier, int itemType, Material mat, int itemAmount){
		
		i = new ItemStack(mat, itemAmount, (short)itemType);
		iMeta = i.getItemMeta();
		List<String> loreList = new ArrayList<>();
		loreList.add(lore);
		
		if (e) {
		  iMeta.addEnchant(enchant, amplifier, true);
		}
		
		iMeta.setDisplayName(dispName);
		iMeta.setLore(loreList);
		i.setItemMeta(iMeta);
		return i;
   }
 
   public ItemStack quickItem(String dispName, int type){
		i = new ItemStack(Material.WOOL, 1, (short)type);
		iMeta = i.getItemMeta();
		iMeta.setDisplayName(dispName);
		i.setItemMeta(iMeta);
		return i;
   }
 
   public void cMsg(Player p, String s){
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCheckers &f//&a " + s));
   }
 }