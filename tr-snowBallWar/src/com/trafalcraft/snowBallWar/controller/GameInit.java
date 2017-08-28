package com.trafalcraft.snowBallWar.controller;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.trafalcraft.snowBallWar.Main;

public class GameInit {
	
	
	public static void Launch(String aname){
		if(Main.getAC().getArena(aname).getstatus().equals("lobby")){
			ArrayList <Location> spawnList = new ArrayList<Location>();
			for(int i = 0;i < Main.getAC().getArena(aname).getSpawnList().size();i++){
				spawnList.add(Main.getAC().getArena(aname).getSpawnList().get(i));
			}
			Main.getAC().getArena(aname).setstatus("in-Game");
			for(Player p : Main.getAC().getArena(aname).getPlayerList()){
				Random rand = new Random();
				int rd = rand.nextInt(spawnList.size());
				p.teleport(spawnList.get(rd));
				spawnList.remove(rd);
				Main.getPC().getPlayer(p.getUniqueId()).setStatus("freeze");
				p.setSaturation(10);
			}
			Main.getAC().getArena(aname).startGameTimer();
			
			
		}
	}
}
