 package com.github.lyokofirelyte.Checkers.Internal;
 
 
 public class CSystem {
	 
	private int piecesStolen = 0;
	private Boolean turn = false;
	private int score = 0;
	private int wins = 0;
	private int losses = 0;
	String name;
 
	public CSystem(String name){
	  this.name = name;
	}
 
	public int getPiecesStolen(){
	  return piecesStolen;
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
 
	public void setPiecesStolen(int a){
	  piecesStolen = a;
	}
 
	public void setScore(int a){
	  score = a;
	}
 
	public void setWins(int a){
	  score = a;
	}
 
	public void setLosses(int a){
	  losses = a;
	}
 
	public void setTurn(Boolean a){
	  turn = a;
	}
 }