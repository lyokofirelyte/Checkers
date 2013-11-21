package com.github.lyokofirelyte.Checkers;

import com.github.lyokofirelyte.Checkers.Game.CGameEvent;
import com.github.lyokofirelyte.Checkers.Internal.CFileManager;
import com.github.lyokofirelyte.Checkers.Internal.CManager;
import com.github.lyokofirelyte.Checkers.Internal.CUtil;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CMain extends JavaPlugin implements Listener {
	
	public CManager manager;
	public CFileManager fileManager;
	public CUtil utils;
	public PluginManager pm;
	
	public void onEnable(){
		
		pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
		pm.registerEvents(new CGameEvent(this), this);
		getCommand("checkers").setExecutor(new CCommand(this));
		
		manager = new CManager(this);
		fileManager = new CFileManager(this);
		utils = new CUtil(this);
		
	 	try {
			fileManager.initFiles();
		} catch (IOException e){
			e.printStackTrace();
		} catch (InvalidConfigurationException e){
			e.printStackTrace();
		}
		
		fileManager.loadAll();
		getLogger().log(Level.INFO, "Thanks for downloading my plugin! It means a lot to me :D");
	}
	
	public void onDisable(){
		
		try {
			fileManager.saveAll();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
 }
 