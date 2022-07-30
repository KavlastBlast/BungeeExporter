package com.lss233.bungeeexporter.collectors;

import com.google.common.util.concurrent.AtomicLongMap;
import com.lss233.bungeeexporter.utils.PlayerUtils;
import io.prometheus.client.Collector;
import io.prometheus.client.Counter;
import io.prometheus.client.GaugeMetricFamily;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PlayerCollector extends Collector {
    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfs = new ArrayList<>();
        GaugeMetricFamily onlineGauge = new GaugeMetricFamily("online_players", "Get current online players.", Arrays.asList("server_name", "connected_ip"));
        for (ServerInfo serverInfo : ProxyServer.getInstance().getServers().values()) {
            AtomicLongMap<String> connectedIpMap = AtomicLongMap.create();
            for (ProxiedPlayer player : serverInfo.getPlayers()) {
                try {
                    connectedIpMap.incrementAndGet(PlayerUtils.getLocalAddress(player));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (Map.Entry<String, Long> entry : connectedIpMap.asMap().entrySet()) {
                onlineGauge.addMetric(Arrays.asList(serverInfo.getName(), entry.getKey()), entry.getValue());
            }
        }

        mfs.add(onlineGauge);
        return mfs;
    }
}
