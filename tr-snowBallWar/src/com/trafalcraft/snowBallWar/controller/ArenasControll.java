package com.trafalcraft.snowBallWar.controller;

import java.util.Collection;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Maps;
import com.trafalcraft.snowBallWar.Main;
import com.trafalcraft.snowBallWar.Data.Arenas;
import com.trafalcraft.snowBallWar.utils.Location_String;
import com.trafalcraft.snowBallWar.utils.Msg;

public class ArenasControll {
	private JavaPlugin plugin = Main.getinstance();
	private final Map<String, Arenas> activeMap = Maps.newHashMap();


	public void addMap(String name){
		if(!this.activeMap.containsKey(name)){
			Arenas arene = new Arenas(name);
			activeMap.put(name, arene);
		}
	}
	
	
	public boolean contains(String aname){
		if(this.activeMap.containsKey(aname)){
			return true;
		}
		return false;
	}
	
	public void removeMap(String name){
		if(this.activeMap.containsKey(name)){
			activeMap.remove(name);
		}
	}
	
	public Arenas getArena(String name){
		return activeMap.get(name);
	}
	
	public void addPlayer(String aname, Player player){
		if(player.isOnline()){
			activeMap.get(aname).addPlayer(player);
			int playerSize = activeMap.get(aname).playerSize();
			if(playerSize > activeMap.get(aname).getMinPlayers()){
				
			}
		}else{
			plugin.getLogger().warning(Msg.PlayerNotConnectedToServer.toString().replace("$player", player.getName()));
		}
	}
	
	public boolean containsPlayer(String aname, Player player){
		return activeMap.get(aname).containsPlayer(player);
	}
	
	public int size(String aname){
		return activeMap.size();
	}
	
	public void removePlayer(String aname, Player player){
		if(player.isOnline()){
			if(activeMap.get(aname).containsPlayer(player)){
				activeMap.get(aname).removePlayer(player);
			}else{
				plugin.getLogger().warning(Msg.removePlayerNotInArena.toString().replace("$aname", aname)
						.replace("$player", player.getName()));
			}
		}else{
			plugin.getLogger().warning(Msg.PlayerNotConnectedToServer.toString().replace("$player", player.getName()));
		}
	}

    public Collection<Arenas> getAll() {
        return activeMap.values();
    }
    
	public String saveArena(String name) {
		Arenas arene = activeMap.get(name);
		if(arene != null){
			String aname = activeMap.get(name).getName();
			try{
				plugin.getConfig().set("arenes."+aname+".max-joueur", arene.getMaxPlayers());
				plugin.getConfig().set("arenes."+aname+".min-joueur", arene.getMinPlayers());
				plugin.getConfig().set("arenes."+aname+".lobbyCooldown", arene.getLobbyCooldown());
				plugin.getConfig().set("arenes."+aname+".gameCooldown", arene.getGameCooldown());
				Location_String.LocationToString(arene.getSpawnList().get(arene.getSpawnList().size()-1));
				for(int i = 0;i<arene.getSpawnList().size();i++){
					plugin.getConfig().set("arenes."+aname+".spawns."+i+1, Location_String.LocationToString(arene.getSpawn(i+1)));
				}
				plugin.getConfig().set("arenes."+aname+".spawns.spec", Location_String.LocationToString(arene.getSpecSpawn()));
				plugin.getConfig().set("arenes."+aname+".arene.P1", Location_String.LocationToString(arene.getP1()));
				plugin.getConfig().set("arenes."+aname+".arene.P2", Location_String.LocationToString(arene.getP2()));
				plugin.saveConfig();
				return Msg.Prefix+Msg.saveArena.toString();
			}catch(NullPointerException | IndexOutOfBoundsException e){
				Main.getinstance().getLogger().warning("\u001B[31m"+e.toString()+"\u001B[0m");
						Main.getinstance().getLogger().warning("\u001B[31m"+"Il manque des information pour bien sauvegardé l'arène"+"\u001B[0m");
						Main.getinstance().getLogger().warning("\u001B[31m"+"maxPlayer: "+"\u001B[0m"+arene.getMaxPlayers());
						Main.getinstance().getLogger().warning("\u001B[31m"+"minPlayer: "+"\u001B[0m"+arene.getMinPlayers());
						Main.getinstance().getLogger().warning("\u001B[31m"+"LobbyCooldown: "+"\u001B[0m"+arene.getLobbyCooldown());
						Main.getinstance().getLogger().warning("\u001B[31m"+"gameCooldown: "+"\u001B[0m"+arene.getGameCooldown());
						Main.getinstance().getLogger().warning("\u001B[31m"+"specspawn: "+"\u001B[0m"+arene.getSpecSpawn());
						Main.getinstance().getLogger().warning("\u001B[31m"+"p1: "+"\u001B[0m"+arene.getP1());
						Main.getinstance().getLogger().warning("\u001B[31m"+"p2: "+"\u001B[0m"+arene.getP2());
						String message=null;
						if(arene.getSpawnList().size()>0){
							message = Location_String.LocationToString(arene.getSpawnList().get(0));
						}
						if(arene.getSpawnList().size()>1){
							for(int i = 1;i<arene.getSpawnList().size();i++){
								message+=","+Location_String.LocationToString(arene.getSpawnList().get(i));
							}
						}
						Main.getinstance().getLogger().warning("\u001B[31m"+"spawnList: "+"\u001B[0m"+message);					
				
				return Msg.ERREUR.toString()+Msg.saveArenaError;
			}
		}
		return Msg.ERREUR.toString()+Msg.NoArenaFound;
	}
}
