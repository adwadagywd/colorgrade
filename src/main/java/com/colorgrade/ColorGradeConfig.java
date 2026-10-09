package com.colorgrade;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ColorGradeConfig {
    public static boolean enabled = true;
    public static float brightness = 0f;   // -1 .. 1
    public static float contrast = 1f;     // 0 .. 2
    public static float saturation = 1f;   // 0 .. 2
    public static float hue = 0f;          // -180 .. 180 (stopnie)
    public static float gamma = 1f;        // 0.5 .. 2

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("colorgrade.json");

    private static class Data {
        boolean enabled = true;
        float brightness = 0f, contrast = 1f, saturation = 1f, hue = 0f, gamma = 1f;
    }

    private ColorGradeConfig() {}

    public static void load() {
        if (!Files.exists(PATH)) return;
        try {
            Data d = GSON.fromJson(Files.readString(PATH), Data.class);
            if (d == null) return;
            enabled = d.enabled;
            brightness = d.brightness;
            contrast = d.contrast;
            saturation = d.saturation;
            hue = d.hue;
            gamma = d.gamma;
        } catch (Exception e) {
            System.err.println("[ColorGrade] Nie udało się wczytać configu: " + e);
        }
    }

    public static void save() {
        Data d = new Data();
        d.enabled = enabled;
        d.brightness = brightness;
        d.contrast = contrast;
        d.saturation = saturation;
        d.hue = hue;
        d.gamma = gamma;
        try {
            Files.writeString(PATH, GSON.toJson(d));
        } catch (IOException e) {
            System.err.println("[ColorGrade] Nie udało się zapisać configu: " + e);
        }
    }

    public static void reset() {
        brightness = 0f;
        contrast = 1f;
        saturation = 1f;
        hue = 0f;
        gamma = 1f;
    }

    public static boolean isNeutral() {
        return Math.abs(brightness) < 0.001f
                && Math.abs(contrast - 1f) < 0.001f
                && Math.abs(saturation - 1f) < 0.001f
                && Math.abs(hue) < 0.01f
                && Math.abs(gamma - 1f) < 0.001f;
    }
}
