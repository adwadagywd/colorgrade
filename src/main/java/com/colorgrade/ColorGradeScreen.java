package com.colorgrade;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.Locale;
import java.util.function.Consumer;

public class ColorGradeScreen extends Screen {

    public ColorGradeScreen() {
        super(Text.translatable("colorgrade.title"));
    }

    private static Text toggleText() {
        return Text.translatable(ColorGradeConfig.enabled ? "colorgrade.enabled" : "colorgrade.disabled");
    }

    @Override
    protected void init() {
        int w = 180, h = 20, gap = 24;
        int x = this.width - w - 12;
        int y = 36;

        addDrawableChild(ButtonWidget.builder(toggleText(), b -> {
            ColorGradeConfig.enabled = !ColorGradeConfig.enabled;
            b.setMessage(toggleText());
        }).dimensions(x, y, w, h).build());
        y += gap + 4;

        addDrawableChild(new ParamSlider(x, y, w, h, "brightness", -1f, 1f, ColorGradeConfig.brightness, "%.2f",
                v -> ColorGradeConfig.brightness = v));
        y += gap;
        addDrawableChild(new ParamSlider(x, y, w, h, "contrast", 0f, 2f, ColorGradeConfig.contrast, "%.2f",
                v -> ColorGradeConfig.contrast = v));
        y += gap;
        addDrawableChild(new ParamSlider(x, y, w, h, "saturation", 0f, 2f, ColorGradeConfig.saturation, "%.2f",
                v -> ColorGradeConfig.saturation = v));
        y += gap;
        addDrawableChild(new ParamSlider(x, y, w, h, "hue", -180f, 180f, ColorGradeConfig.hue, "%.0f°",
                v -> ColorGradeConfig.hue = v));
        y += gap;
        addDrawableChild(new ParamSlider(x, y, w, h, "gamma", 0.5f, 2f, ColorGradeConfig.gamma, "%.2f",
                v -> ColorGradeConfig.gamma = v));
        y += gap + 4;

        addDrawableChild(ButtonWidget.builder(Text.translatable("colorgrade.reset"), b -> {
            ColorGradeConfig.reset();
            clearAndInit();
        }).dimensions(x, y, w / 2 - 2, h).build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("colorgrade.done"), b -> close())
                .dimensions(x + w / 2 + 2, y, w / 2 - 2, h).build());
    }

    // Bez rozmycia/przyciemnienia tła, żeby było widać efekt na żywo.
    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width - 102, 20, 0xFFFFFF);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (ColorGradeMod.openKey.matchesKey(keyCode, scanCode)) {
            close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void removed() {
        ColorGradeConfig.save();
    }

    private static class ParamSlider extends SliderWidget {
        private final String key;
        private final float min, max;
        private final String fmt;
        private final Consumer<Float> setter;

        ParamSlider(int x, int y, int w, int h, String key, float min, float max, float value,
                    String fmt, Consumer<Float> setter) {
            super(x, y, w, h, Text.empty(), (value - min) / (max - min));
            this.key = key;
            this.min = min;
            this.max = max;
            this.fmt = fmt;
            this.setter = setter;
            updateMessage();
        }

        private float real() {
            return min + (float) this.value * (max - min);
        }

        @Override
        protected void updateMessage() {
            setMessage(Text.translatable("colorgrade.option." + key)
                    .append(": " + String.format(Locale.ROOT, fmt, real())));
        }

        @Override
        protected void applyValue() {
            setter.accept(real());
        }
    }
}
