package com.trafalcraft.snowBallWar.controller;

import org.bukkit.entity.Player;

import com.trafalcraft.snowBallWar.Main;
import com.trafalcraft.snowBallWar.utils.Msg;

public class GameEnd {
	public static void Launch(Player p, String aName){
		if(Main.getPC().getPlayer(p.getUniqueId()) != null){
			if(!(Main.getPC().getPlayer(p.getUniqueId()).getScore() >= 5)){
				//La partie continue car le score est trop faible
				return;
			}
		}else if(!(Main.getAC().getArena(aName).getPlayerList().size() <= 1)){
			//La partie continue car assé de joueur
			return;
		}else if(Main.getAC().getArena(aName).getstatus().equalsIgnoreCase("reset")){
			//L'arène est déja fini mais la classe est rapelé (ceci est une sécurité contre les boucle infini)
			return;
		}
		if(Main.getAC().getArena(aName).getPlayerList().size() <= 1){
			//Le joueur p gagne car tous les autres sont partie
			p = Main.getAC().getArena(aName).getPlayer(0);
		}
		Main.getAC().getArena(aName).setstatus("reset");
		Main.getAC().getArena(aName).stopLobbyTimer();
		Main.getAC().getArena(aName).stopGameTimer();
		String trait = "§l§2"+"\u2014"+"§1"+"\u2014";
		for(int i = 0;i<29;i++){
			trait = trait+("§l§2"+"\u2014"+"§1"+"\u2014");
		}
		
		for(Player allp:Main.getAC().getArena(aName).getPlayerList()){
					if(Main.getPC().getPlayer(allp.getUniqueId()).getArena().equalsIgnoreCase(aName)){
						allp.sendMessage(trait);
						allp.sendMessage("");
						allp.sendMessage(Msg.WinMessageToOtherPlayer.toString());
						allp.sendMessage("");
						allp.sendMessage(trait);
					}
		}
		Main.getAC().getArena(aName).resetArena();
		Main.getAC().getArena(aName).setstatus("lobby");

		
		
		
	}
}
