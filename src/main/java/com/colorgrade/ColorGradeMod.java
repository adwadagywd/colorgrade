package com.colorgrade;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ColorGradeMod implements ClientModInitializer {
    public static KeyBinding openKey;

    @Override
    public void onInitializeClient() {
        ColorGradeConfig.load();

        openKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.colorgrade.open",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Y,
                "category.colorgrade"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openKey.wasPressed()) {
                if (client.currentScreen == null && client.world != null) {
                    client.setScreen(new ColorGradeScreen());
                }
            }
        });
    }
}
