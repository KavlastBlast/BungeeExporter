package com.lss233.bungeeexporter.utils;

import io.netty.channel.Channel;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;

public class PlayerUtils {
    public static String getHost(ProxiedPlayer player) throws Exception {
        PendingConnection pc = player.getPendingConnection();
        Object handshake = pc.getClass().getMethod("getHandshake").invoke(pc);
        return (String) handshake.getClass().getMethod("getHost").invoke(handshake);
    }
    public static String getLocalAddress(ProxiedPlayer player) throws Exception {
        PendingConnection pc = player.getPendingConnection();
        Object channelWrapper = getField(pc, "ch");
        Channel channel = (Channel) getField(channelWrapper, "ch");
        InetSocketAddress address = (InetSocketAddress) channel.localAddress();
        return address.getHostString();
    }
    public static Object getField(Object instance, String name) throws Exception {
        Field field = instance.getClass().getDeclaredField("ch");
        field.setAccessible(true);
        return field.get(instance);
    }
}
