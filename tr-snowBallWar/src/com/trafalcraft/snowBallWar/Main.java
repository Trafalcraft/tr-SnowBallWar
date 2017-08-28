package com.trafalcraft.snowBallWar;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.trafalcraft.snowBallWar.Main;
import com.trafalcraft.snowBallWar.utils.Msg;
import com.trafalcraft.snowBallWar.Data.Arenas;
import com.trafalcraft.snowBallWar.Data.Joueurs;
import com.trafalcraft.snowBallWar.controller.ArenasControll;
import com.trafalcraft.snowBallWar.controller.PlayerControll;
import com.trafalcraft.snowBallWar.utils.Location_String;

public class Main extends JavaPlugin{
	
	private static Main instance;
	private PlayerControll pc;
	private ArenasControll ac;
	private Joueurs j;
	private YamlConfiguration yc;
	private File f;
	
	public void onEnable(){
		instance = this;
        pc = new PlayerControll();
        ac = new ArenasControll();
        
		Bukkit.getServer().getPluginManager().registerEvents(new Listeners(), this);
		instance.getConfig().options().copyDefaults(true);
		instance.saveDefaultConfig();
		instance.reloadConfig();
		
		f = new File(instance.getDataFolder(), "Message.yml");
		if(f.exists()){
			Msg.load(f);
		}else{
			try {
				f.createNewFile();
				yc = YamlConfiguration.loadConfiguration(f);
				Msg.DefaultMsg();
				yc.save(f);
			} catch (IOException e) {
				this.getLogger().warning("\u001B[31m"+"Une erreur est survenue pendant la création du fichier message.yml."+"\u001B[0m");
				this.getLogger().warning("\u001B[31m"+"La configuration de base sera utilisé."+"\u001B[0m");
				e.printStackTrace();
			}
		}


		if(instance.getConfig().getConfigurationSection("arenes") != null){
			for(String aname : instance.getConfig().getConfigurationSection("arenes").getKeys(false)){
				if(!ac.contains(aname)){
					try{
					ac.addMap(aname);
					ac.getArena(aname).setMaxPlayers(instance.getConfig().getInt("arenes."+aname+".max-joueur"));
					ac.getArena(aname).setMinPlayers(instance.getConfig().getInt("arenes."+aname+".min-joueur"));
					ac.getArena(aname).setLobbyCooldown(instance.getConfig().getInt("arenes."+aname+".lobbyCooldown"));
					ac.getArena(aname).setGameCooldown(instance.getConfig().getInt("arenes."+aname+".gameCooldown"));
					for(String spawn : instance.getConfig().getConfigurationSection("arenes."+aname+".spawns").getKeys(false)){
						ac.getArena(aname).addSpawn(Location_String.StringToLoc(instance.getConfig().getString("arenes."+aname+".spawns."+spawn)));
					}
					ac.getArena(aname).setSpecSpawn(Location_String.StringToLoc(instance.getConfig().getString("arenes."+aname+".spawns.spec")));	
					ac.getArena(aname).setP1(Location_String.StringToLoc(instance.getConfig().getString("arenes."+aname+".arene.P1")));
					ac.getArena(aname).setP2(Location_String.StringToLoc(instance.getConfig().getString("arenes."+aname+".arene.P2")));	
					this.getLogger().info(Msg.LoadArenaSuccess.toString().replace("$aname", aname));
					}catch(Exception e){
						this.getLogger().warning("\u001B[31m"+Msg.LoadArenaError.toString().replace("$aname", aname)+"\u001B[0m");
					}
				}
			}
		}
        this.getLogger().info(Msg.PluginLoadSuccess.toString());
        this.getLogger().info(Msg.Version.toString().replace("$version", Bukkit.getServer().getVersion()));
	}
	
	public void onDisable(){
		/*for(Arenas a : Main.getAC().getAll()){
			a.resetArena();
		}*/
	}
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[]args){
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("snowBallWar")){
				if(args.length == 0){
				Msg.getHelp(p);
				return true;
				}
				if(args[0].equalsIgnoreCase("help")){
					Msg.getHelp(p);
					return true;
				}
				if(/*p.isOp() && */pc.getPlayer(p.getUniqueId()) == null){
					if(args.length == 1){
						if(args[0].equalsIgnoreCase("reload")&&p.isOp()){
							instance.reloadConfig();
							p.sendMessage(Msg.Prefix.toString()+Msg.Reload_Ok);
							return true;
						}
						if(args[0].equalsIgnoreCase("reloadMsg")&&p.isOp()){
							Msg.load(f);
							p.sendMessage(Msg.Prefix.toString()+Msg.Reload_Ok);
							return true;
						}	
					}
					if(args[0].equalsIgnoreCase("listarena")){
						if(ac.getAll().size()>1){
							String message = "";
							for(Arenas arene : ac.getAll()){
								message+=arene.getName()+",";
							}
							message = message.substring(0,message.length()-1);
							p.sendMessage(Msg.Prefix.toString()+message);
						}else if(ac.getAll().size()==1){
							for(Arenas arene : ac.getAll()){
								p.sendMessage(Msg.Prefix.toString()+arene.getName());
							}
						}else{
							p.sendMessage(Msg.ERREUR+Msg.NoArenaFound.toString());
						}
						return true;
					}

						if(args[0].equalsIgnoreCase("createarena")&&p.isOp()){
							if(args.length == 1){
								p.sendMessage(Msg.Command_Use.toString().replace("$commande", "create <nom de l'arène>"));
								return false;
							}else{
								if(ac.getArena(args[1]) != null){
									p.sendMessage(Msg.ERREUR+Msg.ArenaAlreadyExist.toString());
									return false;
								}
								ac.addMap(args[1]);
								p.sendMessage(Msg.Prefix.toString()+Msg.addArena.toString().replace("$aname", args[1]));
								return true;
							}
						}
											
						//la
						if(args[0].equalsIgnoreCase("addSpawn")&&p.isOp()){
							if(args.length == 1){
								p.sendMessage(Msg.Command_Use.toString().replace("$commande", "addSpawn <nom de l'arène>"));
								return false;
							}else{
								if(ac.getArena(args[1]) == null){
									p.sendMessage(Msg.ERREUR+Msg.ArenaDontExist.toString().replace("$aname", args[1]));
									return false;
								}
								ac.getArena(args[1]).addSpawn(p.getLocation());
								p.sendMessage(Msg.Prefix.toString()+Msg.spawnSet);
								return true;
							}
						}
						
						if(args[0].equalsIgnoreCase("setSpecSpawn")&&p.isOp()){
							if(args.length == 1){
								p.sendMessage(Msg.Command_Use.toString().replace("$commande", "setSpecSpawn <nom de l'arène>"));
								return false;
							}else{
								if(ac.getArena(args[1]) == null){
									p.sendMessage(Msg.ERREUR+Msg.ArenaDontExist.toString().replace("$aname", args[1]));
									return false;
								}
								ac.getArena(args[1]).setSpecSpawn(p.getLocation());
								p.sendMessage(Msg.Prefix.toString()+Msg.spawnSpecSet);
								return true;
							}
						}
						if(args[0].equalsIgnoreCase("setP1")&&p.isOp()){
							if(args.length == 1){
								p.sendMessage(Msg.Command_Use.toString().replace("$commande", "setP1 <nom de l'arène>"));
								return false;
							}else{
								if(ac.getArena(args[1]) == null){
									p.sendMessage(Msg.ERREUR+Msg.ArenaDontExist.toString().replace("$aname", args[1]));
									return false;
								}
								ac.getArena(args[1]).setP1(p.getTargetBlock((Set<Material>) null, 100).getLocation());
								p.sendMessage(Msg.Prefix.toString()+Msg.setP1);
								return true;
							}
						}
						if(args[0].equalsIgnoreCase("setP2")&&p.isOp()){
							if(args.length == 1){
								p.sendMessage(Msg.Command_Use.toString().replace("$commande", "setP2 <nom de l'arène>"));
								return false;
							}else{
								if(ac.getArena(args[1]) == null){
									p.sendMessage(Msg.ERREUR+Msg.ArenaDontExist.toString().replace("$aname", args[1]));
									return false;
								}
								ac.getArena(args[1]).setP2(p.getTargetBlock((Set<Material>) null, 100).getLocation());
								p.sendMessage(Msg.Prefix.toString()+Msg.setP2);
								return true;
							}
						}
						if(args[0].equalsIgnoreCase("savearena")&&p.isOp()){
							if(args.length == 1){
								p.sendMessage(Msg.Command_Use.toString().replace("$commande", "savearena <nom de l'arène>"));
								return false;
							}else{
								instance.reloadConfig();
								p.sendMessage(ac.saveArena(args[1]));
								return true;
							}
						}
						if(args[0].equalsIgnoreCase("join")){
							if(args.length == 1){
								p.sendMessage(Msg.Command_Use.toString().replace("$commande", "join <nom de l'arène>"));
								return false;
							}else{
								if(ac.getArena(args[1]) == null){
									p.sendMessage(Msg.ERREUR+Msg.ArenaDontExist.toString().replace("$aname", args[1]));
									return false;
								}
								pc.addPlayer(p.getUniqueId(), args[1]);
								
								
								return true;
							}
						}
						if(args[0].equalsIgnoreCase("removeLastSpawn")&&p.isOp()){
							if(args.length == 1){
								p.sendMessage(Msg.Command_Use.toString().replace("$commande", "removeLastSpawn <nom de l'arène>"));
								return false;
							}else{
								if(ac.getArena(args[1]) == null){
									p.sendMessage(Msg.ERREUR+Msg.ArenaDontExist.toString().replace("$aname", args[1]));
									return false;
								}else{
									if(ac.getArena(args[1]).getSpawnList().size() > 0){
										ac.getArena(args[1]).removeLastSpawn();
										p.sendMessage(Msg.Prefix.toString()+Msg.removeLastSpawn);
									}else{
										p.sendMessage(Msg.ERREUR.toString()+Msg.SpawnListEmpty);
									}
								}
							}
						}
						
						if(args[0].equalsIgnoreCase("removeSpawn")&&p.isOp()){
							if(args.length < 3){
								p.sendMessage(Msg.Command_Use.toString().replace("$commande", "removeSpawn <nom de l'arène> <numero du spawn>"));
								return false;
							}else{
								if(ac.getArena(args[1]) == null){
									p.sendMessage(Msg.ERREUR+Msg.ArenaDontExist.toString().replace("$aname", args[1]));
									return false;
								}else{
									try {
										int spawn = Integer.parseInt(args[2]);
										if(ac.getArena(args[1]).getSpawnList().size() < spawn){
											ac.getArena(args[1]).removeSpawn(spawn);
											p.sendMessage(Msg.Prefix.toString()+Msg.removeSpawn);
											p.sendMessage(Msg.Prefix.toString()+Msg.newSpawnList);
										}else{
											p.sendMessage(Msg.ERREUR.toString()+Msg.SpawnListEmpty);
										}
										return true;
									}
									catch (NumberFormatException nfe) {
										Bukkit.getLogger().warning(nfe.toString());
										p.sendMessage(Msg.ERREUR.toString() + Msg.NumberFormatError);						
									return false;
									}
								}
							}
						}
						if(args[0].equalsIgnoreCase("setSpawn")&&p.isOp()){
							if(args.length < 3){
								p.sendMessage(Msg.Command_Use.toString().replace("$commande", "setSpawn <nom de l'arène> <numero du spawn>"));
								return false;
							}else{
								if(ac.getArena(args[1]) == null){
									p.sendMessage(Msg.ERREUR+Msg.ArenaDontExist.toString().replace("$aname", args[1]));
									return false;
								}else{
									try {
										int spawn = Integer.parseInt(args[2]);
										if(ac.getArena(args[1]).getSpawnList().size() < spawn){
											ac.getArena(args[1]).setSpawn(p.getLocation(), spawn);
											p.sendMessage(Msg.Prefix.toString()+Msg.spawnModification);
										}else{
											p.sendMessage(Msg.ERREUR.toString()+Msg.SpawnListEmpty);
										}
										return true;
									}
									catch (NumberFormatException nfe) { 
										Bukkit.getLogger().warning(nfe.toString());
										p.sendMessage(Msg.ERREUR.toString()+Msg.NumberFormatError);						
									return false;
									}
								}
							}
						}
						if(args[0].equalsIgnoreCase("LocSpawn")&&p.isOp()){
							if(args.length < 3){
								p.sendMessage(Msg.Command_Use.toString().replace("$commande", "savearena <nom de l'arène> <numero du spawn>"));
								return false;
							}else{
								if(ac.getArena(args[1]) == null){
									p.sendMessage(Msg.ERREUR+Msg.ArenaDontExist.toString().replace("$aname", args[1]));
									return false;
								}else{
									try {
										int spawn = Integer.parseInt(args[2]);
										if(ac.getArena(args[1]).getSpawnList().size() < spawn){
											p.sendMessage(Msg.Prefix+Msg.locSPawn.toString().replace("$nbSpawn", spawn+"")
													.replace("$cooSpawn", Location_String.LocationToString(ac.getArena(args[1]).getSpawn(spawn))));
										}else{
											p.sendMessage(Msg.ERREUR.toString()+Msg.SpawnListEmpty);
										}
										return true;
									}
									catch (NumberFormatException nfe) { 
										Bukkit.getLogger().warning(nfe.toString());
										p.sendMessage(Msg.ERREUR.toString()+Msg.NumberFormatError);						
									return false;
									}
								}
							}
						}
						if(args[0].equalsIgnoreCase("SpawnList")&&p.isOp()){
							if(args.length < 3){
							p.sendMessage(Msg.Command_Use.toString().replace("$commande", "savearena <nom de l'arène>"));
							return false;
						}else{
							if(ac.getArena(args[1]) == null){
								p.sendMessage(Msg.ERREUR+Msg.ArenaDontExist.toString().replace("$aname", args[1]));
								return false;
							}else{
								try {
									int spawn = Integer.parseInt(args[2]);
									if(ac.getArena(args[1]).getSpawnList().size() < spawn){
										p.sendMessage(Msg.Prefix+Location_String.LocationToString(ac.getArena(args[1]).getSpawn(spawn)));
									}else{
										p.sendMessage(Msg.ERREUR.toString()+Msg.SpawnListEmpty);
									}
									return true;
								}
								catch (NumberFormatException nfe) { 
									Bukkit.getLogger().warning(nfe.toString());
									p.sendMessage(Msg.ERREUR.toString() +Msg.NumberFormatError);						
								return false;
								}
							}
						}
						}
						
						
						if(args[0].equalsIgnoreCase("setMaxPlayers")&&p.isOp()){
							if(args.length < 3){
								p.sendMessage(Msg.Command_Use.toString().replace("$commande", "savearena <nom de l'arène>"));
								return false;
							}else{
								int maxp;
								if(ac.getArena(args[1]) == null){
									p.sendMessage(Msg.ERREUR+Msg.ArenaDontExist.toString().replace("$aname", args[1]));
									return false;
								}
								try {
									maxp = Integer.parseInt(args[2]);
									ac.getArena(args[1]).setMaxPlayers(maxp);
									p.sendMessage(Msg.Prefix.toString()+Msg.max_players.toString().replace("$max-players", args[2]));
									return true;
								}
								catch (NumberFormatException nfe) { 
									Bukkit.getLogger().warning(nfe.toString());
									p.sendMessage(Msg.ERREUR.toString() +Msg.NumberFormatError);							
								return false;
								}
							}
						}
						if(args[0].equalsIgnoreCase("setMinPlayers")&&p.isOp()){
							if(args.length < 3){
							p.sendMessage(Msg.Command_Use.toString().replace("$commande", "savearena <nom de l'arène>"));
							return false;
						}else{
							if(ac.getArena(args[1]) == null){
								p.sendMessage(Msg.ERREUR+Msg.ArenaDontExist.toString().replace("$aname", args[1]));
								return false;
							}
							int minp;
							try {
								minp = Integer.parseInt(args[2]);
								ac.getArena(args[1]).setMinPlayers(minp);
								p.sendMessage(Msg.Prefix.toString()+Msg.min_players.toString().replace("$min-players", args[2]));
								return true;
							}
							catch (NumberFormatException nfe) { 
								Bukkit.getLogger().warning(nfe.toString());
								p.sendMessage(Msg.ERREUR.toString() +Msg.NumberFormatError);		
								return false;
							}
						}
						}
						if(args[0].equalsIgnoreCase("setLobbyCooldown")&&p.isOp()){
							if(args.length < 3){
							p.sendMessage(Msg.Command_Use.toString().replace("$commande", "savearena <nom de l'arène>"));
							return false;
						}else{
							if(ac.getArena(args[1]) == null){
								p.sendMessage(Msg.ERREUR+Msg.ArenaDontExist.toString().replace("$aname", args[1]));
								return false;
							}
							int LobbyCooldown;
							try {
								LobbyCooldown = Integer.parseInt(args[2]);
								ac.getArena(args[1]).setLobbyCooldown(LobbyCooldown);
								p.sendMessage(Msg.Prefix.toString()+Msg.lobbyCooldown.toString().replace("$lobbyCooldown", args[2]));
								return true;
							}
							catch (NumberFormatException nfe) { 
								Bukkit.getLogger().warning(nfe.toString());
								p.sendMessage(Msg.ERREUR.toString() +Msg.NumberFormatError);	
								return false;
							}
						}
						}
						if(args[0].equalsIgnoreCase("gameCooldown")&&p.isOp()){
							if(args.length < 3){
							p.sendMessage(Msg.Command_Use.toString().replace("$commande", "savearena <nom de l'arène>"));
							return false;
						}else{
							if(ac.getArena(args[1]) == null){
								p.sendMessage(Msg.ERREUR+Msg.ArenaDontExist.toString().replace("$aname", args[1]));
								return false;
							}
							int gameCooldown;
							try {
								gameCooldown = Integer.parseInt(args[2]);
								ac.getArena(args[1]).setGameCooldown(gameCooldown);
								p.sendMessage(Msg.Prefix.toString()+Msg.gameCooldown.toString().replace("$gameCooldown", args[2]));
								return true;
							}
							catch (NumberFormatException nfe) { 
								Bukkit.getLogger().warning(nfe.toString());
								p.sendMessage(Msg.ERREUR.toString() +Msg.NumberFormatError);	
								return false;
							}
						}
		}
					
				}
				if(args[0].equalsIgnoreCase("leave")){
					if(!(pc.getPlayer(p.getUniqueId()) != null)){
						p.sendMessage(Msg.ERREUR.toString()+Msg.leaveButNotInArena);
						return false;
					}
					pc.removePlayer(p.getUniqueId());
					p.sendMessage(Msg.Prefix.toString()+Msg.PlayerLeaveToPlayer);
					return true;
				}
				
				
				if(args[0].equalsIgnoreCase("info")&&p.isOp()){
/*					p.sendMessage(pc.getAll()+";");
					p.sendMessage(pc.getPlayer(p.getUniqueId())+";");
					p.sendMessage(pc.getPlayerByName(p.getName())+";");
					p.sendMessage("--");*/
					if(pc.getPlayer(p.getUniqueId()) != null){

						p.sendMessage("§4getName §r"+pc.getPlayer(p.getUniqueId()).getName()+"");
						p.sendMessage("§4getStatus §r"+pc.getPlayer(p.getUniqueId()).getStatus()+"");
						p.sendMessage("§4getPlayer §r"+pc.getPlayer(p.getUniqueId()).getPlayer()+"");
						p.sendMessage("§4getScore §r"+pc.getPlayer(p.getUniqueId()).getScore()+"");
						p.sendMessage("§4getUUID §r"+pc.getPlayer(p.getUniqueId()).getuuid()+"");
						p.sendMessage("§4getArena §r"+pc.getPlayer(p.getUniqueId()).getArena()+"");
						p.sendMessage("§aVoici vos infos du snowBallWar §6/dmt info");
						Main.getAC().getArena(Main.getPC().getPlayer(p.getUniqueId()).getArena()).stopLobbyTimer();
						//getRunLobbyCooldown().stopLobbyCooldown();
						
					}else{
						p.sendMessage("§atu n'a pas rejoins le snowBallWar §6/dmt info");
					}
					return true;
				}
				if(args[0].equalsIgnoreCase("areneinfo")&&p.isOp()){
					
/*					p.sendMessage(pc.getAll()+";");
					p.sendMessage(pc.getPlayer(p.getUniqueId())+";");
					p.sendMessage(pc.getPlayerByName(p.getName())+";");
					p.sendMessage("--");*/
					if(ac.getArena("test") != null){
					/*	p.sendMessage("§4getName §r"+ac.getArena("test").getName()+"");
						p.sendMessage("§4getSize §r"+ac.getArena("test").playerSize()+"");					
						p.sendMessage("§4getMinPlayers §r"+ac.getArena("test").getMinPlayers()+"");
						p.sendMessage("§4getMaxPlayers §r"+ac.getArena("test").getMaxPlayers()+"");
						p.sendMessage("§4getStatus §r"+ac.getArena("test").getstatus()+"");
						p.sendMessage("§4getDrawer §r"+ac.getArena("test").getDrawer()+"");
						p.sendMessage("§aVoici vos infos du DrawMyThing §6/dmt info");
						*/
						Main.getAC().getArena(Main.getPC().getPlayer(p.getUniqueId()).getArena()).startLobbyTimer();
						//getRunLobbyCooldown().runLobbyCooldown("test");
					}else{
						p.sendMessage("§apas d'arene test");
					}
					return true;
				}
			}
			Msg.getHelp(p);
		}
		return false;
	}
	
	public static Main getinstance(){
		return instance;
	}
	
	public static PlayerControll getPC(){
		return instance.pc;
	}
	public static ArenasControll getAC(){
		return instance.ac;
	}
	public static YamlConfiguration getYC(){
		return instance.yc;
	}
	public Joueurs getJoueurs(){
		return instance.j;
	}
}
