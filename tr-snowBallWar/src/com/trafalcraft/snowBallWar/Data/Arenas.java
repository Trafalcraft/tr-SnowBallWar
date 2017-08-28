package com.trafalcraft.snowBallWar.Data;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.trafalcraft.snowBallWar.Main;
import com.trafalcraft.snowBallWar.controller.GameInit;
import com.trafalcraft.snowBallWar.utils.Msg;
import com.trafalcraft.snowBallWar.utils.Nbt;
import com.trafalcraft.snowBallWar.utils.trScoreBoard;

public class Arenas {
	private String aname = null;
//	private boolean enable = false;	
	private String astatus = "lobby";//lobby/en-jeux/reset
	private int maxPlayers = 8;
	private int minPlayers = 2;
	private int lobbyCooldown = 30;
	private int gameCooldown = 60;
	private Location spawnSpec = null;
	private Location p1 = null;
	private Location p2 = null;
	private boolean GameTimerRun = false;
	private boolean lobbyTimerRun = false;
	private trScoreBoard sc = null;
	private ArrayList <Player> playerList = new ArrayList<Player>();
	private ArrayList<Location> spawnList = new ArrayList<Location>();
	
	private int taskLobby;
	private int taskGame;
	private int temps;
	
	
	public Arenas(String aname){
		this.aname = aname;
	}
	
	public void resetArena(){
		this.GameTimerRun = false;
		this.lobbyTimerRun = false;
		removeScoreBoard();
		if(getPlayerList().size()>=1){
			for(int i = 0;i<getPlayerList().size();i++){
				Main.getPC().removePlayer(getPlayerList().get(i).getUniqueId());
			}
		}
		getPlayerList().clear();
		this.stopGameTimer();
		this.stopLobbyTimer();
		this.astatus = "lobby";
	}
	
	public void setName(String aname){
		this.aname = aname;
	}
	
	public String getName(){
		return this.aname;
	}
	
	public void setstatus(String astatus){
		this.astatus = astatus;
	}
	
	public String getstatus(){
		return this.astatus;
	}
	
	public void setMaxPlayers(int maxPlayers){
		this.maxPlayers = maxPlayers;
	}
	
	public int getMaxPlayers(){
		return this.maxPlayers;
	}
	
	public void setMinPlayers(int minPlayers){
		this.minPlayers = minPlayers;
	}
	
	public int getMinPlayers(){
		return this.minPlayers;
	}
	
	public void setLobbyCooldown(int secondes){
		this.lobbyCooldown = secondes;
	}
	
	public int getLobbyCooldown(){
		return this.lobbyCooldown;
	}
	
	public void setGameCooldown(int minutes){
		this.gameCooldown = minutes;
	}
	
	public int getGameCooldown(){
		return this.gameCooldown;
	}
	
	
	public void addSpawn(Location spawnLoc){
		this.spawnList.add(spawnLoc);
	}
	
	public void removeLastSpawn() throws ArrayIndexOutOfBoundsException{
		this.spawnList.remove(spawnList.size()-1);
	}
	
	public void removeSpawn(int spawn) throws ArrayIndexOutOfBoundsException{
		this.spawnList.remove(spawn-1);
	}
	
	public void setSpawn(Location spawnLoc, int spawnNumber){
		this.spawnList.set(spawnNumber-1,spawnLoc);
	}
	
	public Location getRandomSpawn() throws ArrayIndexOutOfBoundsException{
		Random rand = new Random();
		int rd = rand.nextInt(spawnList.size());
		return this.spawnList.get(rd);
	}
	
	public Location getSpawn(int spawn) throws ArrayIndexOutOfBoundsException{
		return this.spawnList.get(spawn-1);
	}
	
	public ArrayList<Location> getSpawnList(){
		return this.spawnList;
	}
	
	
	
	public void setSpecSpawn(Location spawnSpec){
		this.spawnSpec = spawnSpec;
	}
	
	public Location getSpecSpawn(){
		return this.spawnSpec;
	}
	
	public void setP1(Location p1){
		this.p1 = p1;
	}
	
	public Location getP1(){
		return this.p1;
	}
	
	public void setP2(Location p2){
		this.p2 = p2;
	}
	
	public Location getP2(){
		return this.p2;
	}
	
	
	public void createScoreBoard(){
		this.sc = new trScoreBoard(aname);
	}
	
	public void updateScoreBoard(Player p){
		this.sc.updateScore(aname, p);
	}
	
	public void removeScoreBoard(){
		if(sc != null){
			this.sc.clearScoreBoard();
			this.sc = null;
		}
	}
	
	public void addPlayer(Player player){
		this.playerList.add(player);
	}

	
	public boolean containsPlayer(Player player){
		if(this.playerList.contains(player)){
			return true;
		}
		return false;
	}
	
	public Player getPlayer(int index){
		return 	this.playerList.get(index);
	}
	public ArrayList<Player> getPlayerList(){
		return 	this.playerList;
	}
	
	public int playerSize(){
		return 	this.playerList.size();
	}
	
	public void removePlayer(Player player){
		this.playerList.remove(player);
	}
	
	public void startLobbyTimer(){
		this.temps = 30;
		this.lobbyTimerRun = true;
		this.taskLobby = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getinstance(), new Runnable() {
            public void run() {

            	for(Player pl : getPlayerList()){
                   		if(temps == 30||temps == 20||temps == 10||(temps <= 5&&temps>0)){
                   			pl.sendMessage(Msg.Prefix+Msg.Timer.toString().replace("$temps", temps+""));
                            pl.playSound(pl.getLocation(), Sound.SUCCESSFUL_HIT, 1.0F, 1.0F);
                   		}else if(temps <= 0){
                   			pl.playSound(pl.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
                  			
                   			pl.sendMessage(Msg.Prefix+Msg.ARENA_START.toString());
                       		GameInit.Launch(aname);
                       		stopLobbyTimer();
                   		}
                }
            	temps = temps-1;
            }
         }, 0, 20);
	}
	
	public boolean lobbyTimerRun(){
		return this.lobbyTimerRun;
	}
	
	public void stopLobbyTimer(){
		Bukkit.getServer().getScheduler().cancelTask(this.taskLobby);
		this.lobbyTimerRun = false;
	}
	
	public void startGameTimer(){
		setstatus("en-jeux");
		createScoreBoard();
		this.temps = 605; //10 minutes et 5 secondes
		this.GameTimerRun = true;
		final ArrayList<Location> zone = new ArrayList<Location>();
		zone.clear();
		if(this.p1.getX()>this.p2.getX()){
			double echange = this.p1.getX();
			this.p1.setX(this.p2.getX());
			this.p2.setX(echange);
		}
		if(this.p1.getZ()>this.p2.getZ()){
			double echange = this.p1.getZ();
			this.p1.setZ(this.p2.getZ());
			this.p2.setZ(echange);
		}
		if(this.p1.getY()<this.p2.getY()){
			double echange = this.p1.getY();
			this.p1.setY(this.p2.getY());
			this.p2.setY(echange);
		}
		int secure = 1000;
		String world = p1.getWorld().getName();
		World w = Bukkit.getWorld(world);
		for(int i = (int) this.p1.getX(); i <= (int) this.p2.getX();i++){
			for(int f = (int) this.p1.getZ(); f <= (int) this.p2.getZ();f++){
				zone.add(new Location(w, i, this.p1.getY(),f));
				secure = secure-1;
				if(secure == 0){
					return;
				}
			}
		}

		this.taskGame = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getinstance(), new Runnable() {
			public void run() {
				short age = 5800;
                /*EntityItem ei;
                NBTTagCompound nbt;
                NBTTagShort nbts = new NBTTagShort(age);*/
            	for(Player pl : Bukkit.getOnlinePlayers()){
                	if(Main.getAC().getArena(aname).containsPlayer(pl)){
                		if(temps > 600){
                			pl.sendMessage((temps-600)+"");
                			
        					//Title.sendTitle(pl, "§6"+(temps-600), "", 20, 40, 20);
                		}else if(temps == 600){
                			pl.sendMessage("Feeeeeeu!");
        					//Title.sendTitle(pl, "§4"+"Feeeeeeeu", "", 20, 40, 20);
        					Main.getPC().getPlayer(pl.getUniqueId()).setStatus("combat");
                		}else{
                			
                		}
                	}
            	}
            	for(int i = 0; i < ((zone.size()/50)*Main.getAC().getArena(aname).playerSize());i++){
	            	Random rand = new Random();
	            	int rd = rand.nextInt(zone.size());
	            	Location zLoc = zone.get(rd);
	            	Nbt.createSnowBall(age, zLoc);
            	}
            	temps = temps-1;
            }
         
		}, 0, 20);
            
     
	}
	
	public boolean GameTimerRun(){
		return this.GameTimerRun;
	}
	
	public void stopGameTimer(){
		Bukkit.getServer().getScheduler().cancelTask(this.taskGame);
		this.GameTimerRun = false;
	}
	
	
	
}
