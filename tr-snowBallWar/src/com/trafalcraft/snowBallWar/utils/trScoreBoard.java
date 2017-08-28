package com.trafalcraft.snowBallWar.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.trafalcraft.snowBallWar.Main;

public class trScoreBoard {
	
	private static ScoreboardManager manager = Bukkit.getScoreboardManager();
	private Scoreboard scoreinGame;// = manager.getNewScoreboard();
	private Objective o;// = scoreinGame.registerNewObjective("SnowBallWar", "kill");
	
	public trScoreBoard(String aname) {
		addScoreBoard(aname);
	}
	
	public void clearScoreBoard(){
		this.scoreinGame.clearSlot(DisplaySlot.SIDEBAR);;
		this.o.unregister();
	}
	private void sendScoreBoard(String aname){
		this.o.setDisplaySlot(DisplaySlot.SIDEBAR);
		this.o.getScore("§4Score§r: ").setScore(22);
		for(Player allp : Main.getAC().getArena(aname).getPlayerList()){
			this.o.getScore(allp.getName()).setScore(0);
		}
		this.o.getScore("").setScore(-1);
		this.o.getScore("§4Map§r:").setScore(-2);
		this.o.getScore("§a"+Main.getAC().getArena(aname).getName()).setScore(-3);
		this.o.getScore("").setScore(-4);
		
		for(Player allp : Main.getAC().getArena(aname).getPlayerList()){
			allp.setScoreboard(scoreinGame);
		}
	}
	
	public void addScoreBoard(String aname){
		this.scoreinGame = manager.getNewScoreboard();
		this.o = scoreinGame.registerNewObjective("SnowBallWar", "kill");
		this.sendScoreBoard(aname);
	}
	
	public void updateScore(String aname, Player p){
		this.o.getScore(p.getName()).setScore(Main.getPC().getPlayer(p.getUniqueId()).getScore());
	}
}