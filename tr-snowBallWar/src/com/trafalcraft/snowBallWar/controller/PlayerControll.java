package com.trafalcraft.snowBallWar.controller;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;
import com.trafalcraft.snowBallWar.Main;
import com.trafalcraft.snowBallWar.Data.Joueurs;
import com.trafalcraft.snowBallWar.utils.Msg;

public class PlayerControll {
	private final Map<UUID, Joueurs> inGamePlayers = Maps.newHashMap();
	
	public void addPlayer(UUID uuid, String aname){
		if(!this.inGamePlayers.containsKey(uuid)){
			Player p = Bukkit.getServer().getPlayer(uuid);
			if(!Main.getAC().getArena(aname).getstatus().equalsIgnoreCase("lobby")){
				p.sendMessage(Msg.ERREUR.toString()+Msg.gameInProgress);
				return;
			}
			if(Main.getAC().getArena(aname).playerSize() == Main.getAC().getArena(aname).getMaxPlayers()){
				p.sendMessage(Msg.Prefix.toString()+Msg.Arena_Full);
				return;
			}
			Joueurs joueurs = new Joueurs(uuid);
			inGamePlayers.put(uuid, joueurs);
			Main.getPC().getPlayer(uuid).setArena(aname);
			Main.getPC().getPlayer(uuid).setStatus("lobby");
			Main.getAC().addPlayer(aname, Bukkit.getServer().getPlayer(uuid));
			Location specspawn = Main.getAC().getArena(aname).getSpecSpawn();
			p.teleport(specspawn);
			p.getInventory().clear();
			p.setHealth(20);
			p.setFoodLevel(20);
			p.setSaturation(200);
			p.setGameMode(GameMode.ADVENTURE);
			p.sendMessage(Msg.Prefix.toString()+Msg.PlayerJoinToPlayer);
			for(Player allp:Bukkit.getOnlinePlayers()){
				if(Main.getPC().getPlayer(allp.getUniqueId()) == null){
					
				}else{
					if(Main.getPC().getPlayer(allp.getUniqueId()).getArena().equalsIgnoreCase(aname)){
					allp.sendMessage(Msg.Prefix+Msg.PlayerJoinToOther.toString().replace("$Player", p.getName())
					.replace("$currentPlayer", Main.getAC().getArena(aname).playerSize()+"")
					.replace("$max-players", Main.getAC().getArena(aname).getMaxPlayers()+""));
				}
			}
			}
		}
		
		if((Main.getAC().getArena(aname).getMinPlayers() <= Main.getAC().getArena(aname).playerSize()) && 
				!Main.getAC().getArena(aname).lobbyTimerRun()){
			Main.getAC().getArena(aname).startLobbyTimer();
		}
	}
	
	
	
	public void removePlayer(UUID uuid){
		if(this.inGamePlayers.containsKey(uuid)){
			Player p = Bukkit.getServer().getPlayer(uuid);
			String aname = Main.getPC().getPlayer(uuid).getArena();
			Main.getAC().removePlayer(aname, Bukkit.getServer().getPlayer(uuid));
			Main.getPC().getPlayer(uuid).setArena(null);
	        this.inGamePlayers.remove(uuid);
			if(Main.getAC().getArena(aname).getMinPlayers() > Main.getAC().getArena(aname).playerSize()){
				Main.getAC().getArena(aname).stopLobbyTimer();
			}
			

			
	        if(Main.getAC().getArena(aname).getstatus().equalsIgnoreCase("lobby")){
				for(Player allp:Bukkit.getOnlinePlayers()){
					if(Main.getPC().getPlayer(allp.getUniqueId()) == null){
						
					}else{
					if(Main.getPC().getPlayer(allp.getUniqueId()).getArena().equalsIgnoreCase(aname)){
						allp.sendMessage(Msg.Prefix+Msg.PlayerLeaveToOther.toString().replace("$Player", p.getName())
								.replace("$currentPlayer", Main.getAC().getArena(aname).playerSize()+"")
								.replace("$max-players", Main.getAC().getArena(aname).getMaxPlayers()+""));
					}
				}
				}
	        }else if(Main.getAC().getArena(aname).getstatus().equalsIgnoreCase("en-jeux")){
				for(Player allp:Bukkit.getOnlinePlayers()){
					if(Main.getPC().getPlayer(allp.getUniqueId()) == null){
						
					}else{
						if(Main.getPC().getPlayer(allp.getUniqueId()).getArena().equalsIgnoreCase(aname)){
							allp.sendMessage(Msg.Prefix+Msg.PlayerLeaveToOther.toString().replace("$Player", p.getName())
									.replace("$currentPlayer", Main.getAC().getArena(aname).playerSize()+"")
									.replace("$max-players", Main.getAC().getArena(aname).getMaxPlayers()+""));

						}
					}
				}
				GameEnd.Launch(p,aname);
	        }
		}
		
	}
	
	public Joueurs getPlayer(UUID uuid){
		return inGamePlayers.get(uuid);
	}
	
	public Joueurs getPlayerByName(String name){
    	for (Joueurs gPlayer: inGamePlayers.values()) {
    		if (gPlayer.getName().equals(name)) {
    			return gPlayer;
    		}
    	} 
    	return null;   
	}
	
    public Collection<Joueurs> getAll() {
        return inGamePlayers.values();
    }
}
