package com.PiMan.RecieverMod.Packets;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.util.handlers.FlashHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageFlashClient extends MessageBase<MessageFlashClient> implements IMessage {
		
	private boolean update;
	
	private int duration;
	
	private int dimension;
	
	private int x;
	
	private int y;
	 
	private int z;

	public MessageFlashClient(){}
	
	public MessageFlashClient(boolean update, int dimension) {
		this(update, BlockPos.ORIGIN, dimension, 0);
	}
	
	public MessageFlashClient(boolean update, BlockPos pos, int dimension, int duration) {
		this.update = update;
		this.duration = duration;
		this.dimension = dimension;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {

		update = buf.readBoolean();
		duration = buf.readInt();
		dimension = buf.readInt();
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		
		buf.writeBoolean(update);
		buf.writeInt(duration);
		buf.writeInt(dimension);
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		
	}

	@Override
	public void handleClientSide(MessageFlashClient message, EntityPlayer player) {
				
		if (message.dimension == player.dimension) {
		
			if (message.update) {
				FlashHandler.Update(message.dimension);
				//System.out.println("Updating Flashes");
			}
			else {
				FlashHandler.AddFlash(new BlockPos(message.x, message.y, message.z), message.dimension, message.duration);
				//System.out.println("Creating Flash");
			}
		}
	}

	@Override
	public void handleServerSide(MessageFlashClient message, EntityPlayer player) {

	}

}
