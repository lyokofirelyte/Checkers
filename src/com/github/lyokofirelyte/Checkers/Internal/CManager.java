 package com.github.lyokofirelyte.Checkers.Internal;
 
 import com.github.lyokofirelyte.Checkers.CMain;
 import java.util.HashMap;
 import java.util.Map;
 import org.bukkit.inventory.Inventory;
 
 public class CManager {
	 
	CMain pl;
	public Map<String, CPlayer> cPlayers = new HashMap<>();
	public Map<String, CSystem> cSystems = new HashMap<>();
	public Map<String, Inventory> cInvs = new HashMap<>();
 
	public CManager(CMain instance){
	  pl = instance;
	}
 
	public CPlayer getCPlayer(String name){
		
	  if (cPlayers.containsKey(name)) {
		 return (CPlayer)cPlayers.get(name);
	  }
	  return null;
	}
 
	public CSystem getCSystem(String name){
		
	  if (cSystems.containsKey(name)) {
		 return (CSystem)cSystems.get(name);
	  }
	  return null;
	}
 
	public CPlayer updateCPlayer(String name, CPlayer cplayer){
		
	  cPlayers.put(name, cplayer);
	  return (CPlayer)cPlayers.get(name);
	}
 
	public CSystem updateCSystem(String name, CSystem csystem){
		
	  cSystems.put(name, csystem);
	  return (CSystem)cSystems.get(name);
	}
}