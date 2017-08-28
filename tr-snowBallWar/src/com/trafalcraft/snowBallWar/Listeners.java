package com.trafalcraft.snowBallWar;

import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.trafalcraft.snowBallWar.Main;
import com.trafalcraft.snowBallWar.controller.GameEnd;
import com.trafalcraft.snowBallWar.utils.Msg;

public class Listeners implements Listener{
	
	@EventHandler (priority = EventPriority.HIGH)
	  public void onPlayerLeave(PlayerQuitEvent e){
		Player p = e.getPlayer();
		Main.getPC().removePlayer(p.getUniqueId());
	}

	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			try{
			if(!Main.getAC().getArena(Main.getPC().getPlayer(e.getEntity().getUniqueId()).getArena()).getstatus().equals("en-jeux")){
				e.setCancelled(true);
			}
			}catch(Exception ee){
				
			}
		}
	}
	
	@EventHandler
	public void onPlayerDamageByPlayer(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Snowball){
			if(e.getDamager() instanceof Snowball){
				if(((Snowball)e.getDamager()).getShooter() instanceof Player){
					Player p;
					p = (Player) e.getEntity();
					if(Main.getPC().getPlayer(p.getUniqueId()) != null){
						if(!Main.getPC().getPlayer(p.getUniqueId()).getStatus().equalsIgnoreCase("combat")){
							e.setCancelled(true);
						}else{
								e.setDamage(4);
								if(e.getFinalDamage()>=p.getHealth()){
									//a reecrire proprement
									p.getInventory().clear();
									e.setDamage(0);
									p.setHealth(20);
									p.setFoodLevel(20);
									p.teleport(Main.getAC().getArena(Main.getPC().getPlayer(p.getUniqueId()).getArena()).getRandomSpawn());
									
									int Score = Main.getPC().getPlayer(((Player) ((Snowball)e.getDamager()).getShooter()).getUniqueId()).getScore();
									Main.getPC().getPlayer(((Player) ((Snowball)e.getDamager()).getShooter()).getUniqueId()).setScore(Score+1);
									Player p2 = (Player) ((Snowball)e.getDamager()).getShooter();
									p2.setLevel(Score+1);
									Main.getAC().getArena(Main.getPC().getPlayer(p.getUniqueId()).getArena()).updateScoreBoard(p2);
									
									GameEnd.Launch((Player) ((Snowball)e.getDamager()).getShooter(), Main.getPC().getPlayer(((Player) ((Snowball)e.getDamager()).getShooter()).getUniqueId()).getArena());                           
									
									for(Player allp:Main.getAC().getArena(Main.getPC().getPlayer(p.getUniqueId()).getArena()).getPlayerList()){
										if(allp != p){
											allp.sendMessage(Msg.Prefix+Msg.Kill.toString().replace("$Mort", p.getName()).replace("Tueur", ((Player) ((Snowball)e.getDamager()).getShooter()).getName()));	
										}else{
											allp.sendMessage(Msg.Prefix+Msg.Mort.toString().replace("$Tueur", ((Player) ((Snowball)e.getDamager()).getShooter()).getName()));
										}
									}
									
								}
								return;
						}
						
					}
					
				}
			}
		}
		e.setCancelled(true);
	}
	
	
	
}
