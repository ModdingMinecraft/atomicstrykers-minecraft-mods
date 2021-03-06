package atomicstryker.magicyarn.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import atomicstryker.magicyarn.client.MagicYarnClient;
import atomicstryker.magicyarn.common.network.NetworkHelper.IPacket;
import cpw.mods.fml.common.FMLCommonHandler;

public class HandshakePacket implements IPacket
{

    @Override
    public void writeBytes(ChannelHandlerContext ctx, ByteBuf bytes)
    {
    }

    @Override
    public void readBytes(ChannelHandlerContext ctx, ByteBuf bytes)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            MagicYarnClient.instance.onServerAnsweredChallenge();
        }
        else
        {
            ctx.writeAndFlush(this);
        }
    }
}
