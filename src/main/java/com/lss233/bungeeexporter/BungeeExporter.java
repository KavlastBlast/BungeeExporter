package com.lss233.bungeeexporter;

import com.lss233.bungeeexporter.collectors.PlayerCollector;
import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.HTTPServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public final class BungeeExporter extends Plugin {
    private final List<Collector> collectors = new ArrayList<>();
    private HTTPServer server;
    @Override
    public void onEnable() {
        // Plugin startup logic
        collectors.add(new PlayerCollector());
        for (Collector collector : collectors) {
            collector.register();
        }
        try {
            server = new HTTPServer(1239);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            server.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Collector collector : collectors) {
            try {
                CollectorRegistry.defaultRegistry.unregister(collector);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
