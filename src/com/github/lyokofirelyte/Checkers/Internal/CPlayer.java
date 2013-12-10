 package com.github.lyokofirelyte.Checkers.Internal;
 
 import org.bukkit.inventory.ItemStack;
 
 public class CPlayer {
	 
	private int piecesStolen = 0;
	private Boolean turn = false;
	private int selSlot = 0;
	private int selPiece = 0;
	private ItemStack selPieceItem;
	private int score = 0;
	private int wins = 0;
	private int losses = 0;
	String name;
	Boolean rawr = false;
 
	public CPlayer(String name){
	  this.name = name;
	}
	
	public Boolean getRawr(){
		return rawr;
	}
 
	public ItemStack getSelPieceItem(){
	  return selPieceItem;
	}
 
	public int getPiecesStolen(){
	  return piecesStolen;
	}
 
	public int getSelSlot(){
	  return selSlot;
	}
 
	public int getSelPiece(){
	  return selPiece;
	}
 
	public int getScore(){
	  return score;
	}
 
	public int getWins(){
	  return wins;
	}
 
	public int getLosses(){
	  return losses;
	}
 
	public Boolean getTurn(){
	  return turn;
	}
 
	public void setRawr(Boolean a){
		rawr = a;
	}
	
	public void setSelPieceItem(ItemStack a){
	  selPieceItem = a;
	}
 
	public void setPiecesStolen(int a){
	  piecesStolen = a;
	}
 
	public void setScore(int a){
	  score = a;
	}
 
	public void setWins(int a){
	  wins = a;
	}
 
	public void setLosses(int a){
	  losses = a;
	}
 
	public void setSelSlot(int a){
	  selSlot = a;
	}
 
	public void setSelPiece(int a){
	  selPiece = a;
	}
 
	public void setTurn(Boolean a){
	  turn = a;
	}
 }