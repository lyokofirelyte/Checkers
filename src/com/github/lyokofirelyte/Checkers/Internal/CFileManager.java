 package com.github.lyokofirelyte.Checkers.Internal;
 
 import com.github.lyokofirelyte.Checkers.CMain;
 import java.io.File;
 import java.io.IOException;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.logging.Level;
 import org.bukkit.configuration.InvalidConfigurationException;
 import org.bukkit.configuration.file.YamlConfiguration;
 
 public class CFileManager {
	 
	CMain pl;
	CPlayer cPlayer;
	CSystem cSystem;
	File playersFile;
	YamlConfiguration playersYaml;
	List<String> userList = new ArrayList<>();
	String playersPath = "./plugins/Checkers/players.yml";
 
	public CFileManager(CMain instance){
		pl = instance;
	}
	 
	public void initFiles() throws IOException, InvalidConfigurationException {
		
		playersFile = new File(playersPath);
		 
		if (!playersFile.exists()) {
			playersFile.getParentFile().mkdirs();
			playersFile.createNewFile();
		}
	 
		playersYaml = YamlConfiguration.loadConfiguration(playersFile);
	}
	 
	public void saveAll() throws IOException {
		
		userList = playersYaml.getStringList("TotalUsers");
		 
		for (String user : userList) {
			cPlayer = pl.manager.getCPlayer(user);
			playersYaml.set("Users." + user + ".Score", cPlayer.getScore());
			playersYaml.set("Users." + user + ".Wins", cPlayer.getWins());
			playersYaml.set("Users." + user + ".Losses", cPlayer.getLosses());
		}
	 
		playersYaml.save(playersFile);
		pl.getLogger().log(Level.INFO, "Saved " + userList.size() + " users!");
	}
	 
	public void loadAll() {
		
	  userList = playersYaml.getStringList("TotalUsers");
	 
	  for (String user : userList) {
		  newCPlayer(user);
	  }
	 
	  pl.getLogger().log(Level.INFO, "Loaded " + userList.size() + " users!");
	}
	 
	public void newCPlayer(String name) {
		
	  userList = playersYaml.getStringList("TotalUsers");
	 
	  if (!userList.contains(name)) {
		  userList.add(name);
		  playersYaml.set("TotalUsers", userList);
	  }
	 
	  cPlayer = new CPlayer(name);
	  cPlayer.setScore(playersYaml.getInt("Users." + name + ".Score"));
	  cPlayer.setWins(playersYaml.getInt("Users." + name + ".Wins"));
	  cPlayer.setLosses(playersYaml.getInt("Users." + name + ".Losses"));
	 
	  pl.manager.updateCPlayer(name, cPlayer);
	}
 }
