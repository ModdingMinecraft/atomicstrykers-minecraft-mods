package atomicstryker.multimine.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import atomicstryker.multimine.client.MultiMineClient;
import atomicstryker.multimine.common.BlockPos;
import atomicstryker.multimine.common.MultiMineServer;
import atomicstryker.multimine.common.network.NetworkHelper.IPacket;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;

public class PartialBlockPacket implements IPacket
{

    private String user;
    private int x, y, z;
    float value;

    public PartialBlockPacket()
    {
    }

    public PartialBlockPacket(String username, int ix, int iy, int iz, float val)
    {
        user = username;
        x = ix;
        y = iy;
        z = iz;
        value = val;
    }

    @Override
    public void writeBytes(ChannelHandlerContext ctx, ByteBuf bytes)
    {
        ByteBufUtils.writeUTF8String(bytes, user);
        bytes.writeInt(x);
        bytes.writeInt(y);
        bytes.writeInt(z);
        bytes.writeFloat(value);
    }

    @Override
    public void readBytes(ChannelHandlerContext ctx, ByteBuf bytes)
    {
        user = ByteBufUtils.readUTF8String(bytes);
        x = bytes.readInt();
        y = bytes.readInt();
        z = bytes.readInt();
        value = bytes.readFloat();
        
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            MultiMineClient.instance().onServerSentPartialBlockData(x, y, z, value);
        }
        else
        {
            EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(user);
            if (player != null)
            {
                MultiMineServer.instance().onClientSentPartialBlockPacket(player, new BlockPos(x, y, z), value);
            }
        }
    }

}
