package com.trafalcraft.snowBallWar.utils;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.trafalcraft.snowBallWar.Main;

public enum Msg {
	
	Prefix("§bSnowBallWar §9§l> §r§b "),
	ERREUR("§4SnowBallWar §l> §r§c "),
	NO_PERMISSIONS("§4Erreur §9§l> §r§bVous n'avez pas la permission de faire sa."),
	Command_Use("§4SnowBallWar §l> §r§cutilisation de la commande: §6/sbw $commande"),
	
	//Debugueur
	PluginLoadSuccess("SnowBallWar chargé"),
	LoadArenaSuccess("Arène $aname chargé avec succes"),
	Version("Le serveur tourne en $version"),
	
	//Info
	NoArenaFound("Désolé mais il n'y a pas encore d'arène, utilise /sbw createarena"),
	locSPawn("Le spawn $nbSpawn est citué au coordonnées $cooSpawn"),
	
	//ERREUR
	LoadArenaError("Une erreur est survenu pendant le chargement de l'arene: $aname"),
	ArenaAlreadyExist("Une arene du même nom éxiste déjà"),
	ArenaDontExist("l'arène $aname n'existe pas"),
    saveArenaError("Une Erreur est survenue pendant la sauvegarde ,regardé la console"),
    SpawnListEmpty("Il n'y a aucun spawn ou le spawn séléctionné n'existe pas"),
    NumberFormatError("La valeur doit etre numérique et comprise entre 1 et 32 767"),
	Arena_Full("l'arène est déja pleine"),
	leaveButNotInArena("§atu n'est pas dans une arène"),
	PlayerNotConnectedToServer("Le joueur $player n'est pas connecté"),
	removePlayerNotInArena("l'arène $aname ne contient pas le joueur $player"),
	//String_to_int("La valeur du spawn doit etre numérique et comprise entre 0 et $MaxPlayers"),
    //classes_no_exist("La classe $classe n'existe pas"),
	//Erreur_JOIN("Vous êtes déja en jeux, utilisé $cmd pour quitter"),
	
	//setup
    Reload_Ok("Reload Ok!"),
    addArena("L'arène $aname a bien été crée utilisé §9/sbw savearena §bpour la sauvegardé après l'avoir configurer"),
    min_players("Le nombre minimum de joueur dans l'arene est maintenant de $min-players"),
    max_players("Le nombre maximum de joueur dans l'arene est maintenant de $max-players"),
    lobbyCooldown("Le temp d'attente au lobby est maintenant de $lobbyCooldown"),
    gameCooldown("Le temp maximum d'une partie est maintenant de $gameCooldown"),
    spawnSet("Le Spawn a été ajouté avec succès"),
    spawnSpecSet("Le Spawn spectateur a été ajouté avec succès"),
    setP1("Le premier point de l'arène a bien été configuré"),
    setP2("Le deuxième point de l'arène a bien été configuré"),
    saveArena("L'arène a été sauvegardé avec succès"),
    removeLastSpawn("Le dernier spawn configurer a bien été supprimé"),
    removeSpawn("Le spawn a bien été supprimé"),
    newSpawnList("Faite /sbw spawnList pour voir la list des spawn ordonnées"),
    spawnModification("Le Spawn a été modifié avec succès"),
    //suppr_spawn_success("Le spawn $spawn a bien été supprimé, il reste: $rspawn spawn"),
    //set_spawn_success("Le spawn du déssinateur a bien été configuré"),
    //spawn_spec("Le spawn des spectateurs a bien été configuré"),
    //setclasses("La classe $classe §ra bien été sauvegardé"),
    
    
    //jeux
    ARENA_START("La partie commence !"),
    Timer("La partie commence dans $temps secondes!"),
    Kill("$Mort a été tué par $Tueur !"),
	Mort("Vous avez été tué par $Tueur !"),
	gameInProgress("Une partie est déja lancé"),
	PlayerJoinToPlayer("§aBienvenue dans cette bataille de boule de neige"),
	PlayerJoinToOther("$Player à rejoins la bataille ($currentPlayer/$max-players)"),
	PlayerLeaveToPlayer("Tu a bien quitté l'arène"),
	PlayerLeaveToOther("$Player à quitté la bataille ($currentPlayer/$max-players)"),
	WinMessageToOtherPlayer("§l§9$winner §r§ba gagné.");
	
	
    //Bienvenue("Bienvenue dans le snowBallWar"),
    //Arret_Timer("Un joueur a quitté compte à rebours annulé!"),
    //Join("$joueur a rejoint ($njoueursIG/$njoueurmax)");
	
	
	static JavaPlugin plugin = Main.getinstance();
	  public static void getHelp(Player sender){
	        sender.sendMessage("");
	        sender.sendMessage("§3§l-------------------SnowBallWar-------------------");
	        sender.sendMessage("§3/sbw setup <nom de l'arene> §b- crée l'arène.");
	        sender.sendMessage("§3/sbw spawn<numero> §b- Configurer le lieu de spawn des joueurs.");
	        sender.sendMessage("                       §3Version: §6" + plugin.getDescription().getVersion());
	        sender.sendMessage("§3------------------------------------------------");
	        sender.sendMessage("");
	        Bukkit.getLogger().info("\u001B[31m" + sender.getName() + "\u001B[36m" + " a regarde la page d'aide." + "\u001B[0m");
		  }
	  
	  
	    private String value;

		private Msg(String value) {
			this.value = value;
	        //set(value);
	    }
	    public String toString(){
	    	return value;
	    }
	    public void replaceby(String value){
			this.value = value;
	    }
	    
	    public static void load(File f){
	    	YamlConfiguration yc = Main.getYC();
			yc = YamlConfiguration.loadConfiguration(f);
	    	Prefix.replaceby(yc.getString("default.prefix"));
	    	ERREUR.replaceby(yc.getString("default.error"));
	    	NO_PERMISSIONS.replaceby(yc.getString("default.no_permission"));
	    	Command_Use.replaceby(yc.getString("default.command_use"));
	    	
	    	//Debugueur
	    	PluginLoadSuccess.replaceby(yc.getString("debug.PluginLoadSuccess"));
	    	 LoadArenaSuccess.replaceby(yc.getString("debug.LoadArenaSuccess"));
	    	 Version.replaceby(yc.getString("debug.Version"));
	    	
	    	
	    	//Info
	    	NoArenaFound.replaceby(yc.getString("info.NoArenaFound"));
	    	locSPawn.replaceby(yc.getString("info.locSPawn"));

	    	//ERREUR
	    	 LoadArenaError.replaceby(yc.getString("erreur.LoadArenaError"));
	    	 ArenaAlreadyExist.replaceby(yc.getString("erreur.ArenaAlreadyExist"));
	    	 ArenaDontExist.replaceby(yc.getString("erreur.ArenaDontExist"));
	    	 saveArenaError.replaceby(yc.getString("erreur.saveArenaError"));
	    	 SpawnListEmpty.replaceby(yc.getString("erreur.SpawnListEmpty"));
	    	 NumberFormatError.replaceby(yc.getString("erreur.NumberFormatError"));
	    	 Arena_Full.replaceby(yc.getString("erreur.Arena_Full"));
	    	 leaveButNotInArena.replaceby(yc.getString("erreur.leaveButNotInArena"));
	    	 PlayerNotConnectedToServer.replaceby(yc.getString("erreur.PlayerNotConnectedToServer"));
	    	 removePlayerNotInArena.replaceby(yc.getString("erreur.removePlayerNotInArena"));

	    	//setup
	    	 Reload_Ok.replaceby(yc.getString("setup.Reload_Ok"));
	    	 addArena.replaceby(yc.getString("setup.addArena"));
	    	 min_players.replaceby(yc.getString("setup.min_players"));
	    	 max_players.replaceby(yc.getString("setup.max_players"));
	    	 lobbyCooldown.replaceby(yc.getString("setup.lobbyCooldown"));
	    	 gameCooldown.replaceby(yc.getString("setup.gameCooldown"));
	    	 spawnSet.replaceby(yc.getString("setup.spawnSet"));
	    	 spawnSpecSet.replaceby(yc.getString("setup.spawnSpecSet"));
	    	 setP1.replaceby(yc.getString("setup.setP1"));
	    	 setP2.replaceby(yc.getString("setup.setP2"));
	    	 saveArena.replaceby(yc.getString("setup.saveArena"));
	    	 removeLastSpawn.replaceby(yc.getString("setup.removeLastSpawn"));
	    	 newSpawnList.replaceby(yc.getString("setup.newSpawnList"));
	    	 spawnModification.replaceby(yc.getString("setup.spawnModification"));
	    	
	        //jeux
	    	 ARENA_START.replaceby(yc.getString("setup.ARENA_START"));
	    	 Timer.replaceby(yc.getString("setup.Timer"));
	    	 Kill.replaceby(yc.getString("setup.Kill"));
	    	 Mort.replaceby(yc.getString("setup.Mort"));
	    	 gameInProgress.replaceby(yc.getString("setup.gameInProgress"));
	    	 PlayerJoinToPlayer.replaceby(yc.getString("setup.PlayerJoinToPlayer"));
	    	 PlayerJoinToOther.replaceby(yc.getString("setup.PlayerJoinToOther"));
	    	 PlayerLeaveToOther.replaceby(yc.getString("setup.PlayerLeaveToOther"));
	    	 WinMessageToOtherPlayer.replaceby(yc.getString("setup.WinMessageToOtherPlayer"));
	    }
	    
	    public static void DefaultMsg(){
	    	YamlConfiguration yc = Main.getYC();
	    	//default
	    	yc.set("default.prefix", Prefix.toString());
	    	yc.set("default.error", ERREUR.toString());
	    	yc.set("default.no_permission", NO_PERMISSIONS.toString());
	    	yc.set("default.command_use", Command_Use.toString());
	    	
	    	//Debugueur
	    	yc.set("debug.PluginLoadSuccess", PluginLoadSuccess.toString());
	    	yc.set("debug.LoadArenaSuccess", LoadArenaSuccess.toString());
	    	yc.set("debug.Version", Version.toString());
	    	
	    	
	    	//Info
	    	yc.set("info.NoArenaFound",NoArenaFound.toString());
	    	yc.set("info.locSPawn",locSPawn.toString());

	    	//ERREUR
	    	yc.set("erreur.LoadArenaError", LoadArenaError.toString());
	    	yc.set("erreur.ArenaAlreadyExist", ArenaAlreadyExist.toString());
	    	yc.set("erreur.ArenaDontExist", ArenaDontExist.toString());
	    	yc.set("erreur.saveArenaError", saveArenaError.toString());
	    	yc.set("erreur.SpawnListEmpty", SpawnListEmpty.toString());
	    	yc.set("erreur.NumberFormatError", NumberFormatError.toString());
	    	yc.set("erreur.Arena_Full", Arena_Full.toString());
	    	yc.set("erreur.leaveButNotInArena", leaveButNotInArena.toString());
	    	yc.set("erreur.PlayerNotConnectedToServer", PlayerNotConnectedToServer.toString());
	    	yc.set("erreur.removePlayerNotInArena", removePlayerNotInArena.toString());

	    	//setup
	    	yc.set("setup.Reload_Ok", Reload_Ok.toString());
	    	yc.set("setup.addArena", addArena.toString());
	    	yc.set("setup.min_players", min_players.toString());
	    	yc.set("setup.max_players", max_players.toString());
	    	yc.set("setup.lobbyCooldown", lobbyCooldown.toString());
	    	yc.set("setup.gameCooldown", gameCooldown.toString());
	    	yc.set("setup.spawnSet", spawnSet.toString());
	    	yc.set("setup.spawnSpecSet", spawnSpecSet.toString());
	    	yc.set("setup.setP1", setP1.toString());
	    	yc.set("setup.setP2", setP2.toString());
	    	yc.set("setup.saveArena", saveArena.toString());
	    	yc.set("setup.removeLastSpawn", removeLastSpawn.toString());
	    	yc.set("setup.newSpawnList", newSpawnList.toString());
	    	yc.set("setup.spawnModification", spawnModification.toString());
	    	
	        //jeux
	    	yc.set("setup.ARENA_START", ARENA_START.toString());
	    	yc.set("setup.Timer", Timer.toString());
	    	yc.set("setup.Kill", Kill.toString());
	    	yc.set("setup.Mort", Mort.toString());
	    	yc.set("setup.gameInProgress", gameInProgress.toString());
	    	yc.set("setup.PlayerJoinToPlayer", PlayerJoinToPlayer.toString());
	    	yc.set("setup.PlayerJoinToOther", PlayerJoinToOther.toString());
	    	yc.set("setup.PlayerLeaveToOther", PlayerLeaveToOther.toString());
	    	yc.set("setup.WinMessageToOtherPlayer", WinMessageToOtherPlayer.toString());
	    	
	    }
		
/*	    void set(String value) {
	        //this.value = value;
	    }
*/
}
