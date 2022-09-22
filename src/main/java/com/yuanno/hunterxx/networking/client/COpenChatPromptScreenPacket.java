package com.yuanno.hunterxx.networking.client;

import com.yuanno.hunterxx.networking.PacketHandler;
import com.yuanno.hunterxx.networking.server.SOpenOverviewScreenPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class COpenChatPromptScreenPacket {

    public COpenChatPromptScreenPacket() {}

    public void encode(PacketBuffer buffer)
    {
    }

    public static COpenChatPromptScreenPacket decode(PacketBuffer buffer)
    {
        COpenChatPromptScreenPacket msg = new COpenChatPromptScreenPacket();

        return msg;
    }

    public static void handle(COpenChatPromptScreenPacket message, final Supplier<NetworkEvent.Context> ctx)
    {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
        {
            ctx.get().enqueueWork(() ->
            {
                PlayerEntity player = ctx.get().getSender();

                PacketHandler.sendTo(new SOpenOverviewScreenPacket(), player);
            });
        }
        ctx.get().setPacketHandled(true);
    }
}
