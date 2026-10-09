package com.colorgrade.mixin;

import com.colorgrade.ColorGradeRenderer;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    // Po wyrenderowaniu świata (z ręką), przed HUD i GUI -> interfejs zostaje bez zmian kolorów.
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/GameRenderer;renderWorld(Lnet/minecraft/client/render/RenderTickCounter;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void colorgrade$afterWorld(CallbackInfo ci) {
        ColorGradeRenderer.apply();
    }
}
