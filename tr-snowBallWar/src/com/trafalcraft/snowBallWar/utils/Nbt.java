package com.trafalcraft.snowBallWar.utils;

import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagShort;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftItem;
import org.bukkit.inventory.ItemStack;

public class Nbt {
	
	public static void createSnowBall(short age, Location zLoc){
        EntityItem ei;
        NBTTagCompound nbt;
        NBTTagShort nbts = new NBTTagShort(age);
    	ItemStack snowBall =  new ItemStack(Material.SNOW_BALL);
        ei = ((EntityItem)((CraftItem)zLoc.getWorld().dropItemNaturally(zLoc, snowBall)).getHandle());
        nbt = new NBTTagCompound();
        ei.b(nbt); // Copies everything from the Items NBT to my nbt
        nbt.set("Age", nbts);
        ei.a(nbt); // Sets the Items NBT to my nbt
    	//Bukkit.getWorld(zLoc.getWorld().getName()).dropItemNaturally(zLoc, snowBall);
	}

}
