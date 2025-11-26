package com.vaibhavparashar.plexaframe.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public final class PlexaConfig {
    public boolean enableThreading = true;
    public int workerCount = 1;
    public boolean enableAsyncPreload = true;
    public int preloadDistance = 48;

    private static final File CONFIG_FILE = new File("config/plexaframe.json");
    private static PlexaConfig INSTANCE;
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();

    public static synchronized PlexaConfig load() {
        if (INSTANCE != null) return INSTANCE;
        try {
            if (CONFIG_FILE.exists()) {
                try (FileReader r = new FileReader(CONFIG_FILE)) {
                    INSTANCE = G.fromJson(r, PlexaConfig.class);
                }
            }
        } catch (Throwable ignored) {}
        if (INSTANCE == null) INSTANCE = new PlexaConfig();
        save();
        return INSTANCE;
    }

    public static synchronized void save() {
        if (INSTANCE == null) return;
        try (FileWriter w = new FileWriter(CONFIG_FILE)) {
            G.toJson(INSTANCE, w);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static synchronized PlexaConfig instance() {
        if (INSTANCE == null) load();
        return INSTANCE;
    }
}
