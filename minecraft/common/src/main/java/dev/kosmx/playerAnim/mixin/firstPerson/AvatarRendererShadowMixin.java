package dev.kosmx.playerAnim.mixin.firstPerson;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class AvatarRendererShadowMixin {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("RETURN"))
    private void removeShadowInFirstPerson(Avatar avatar, AvatarRenderState avatarRenderState, float partialTick, CallbackInfo ci) {
        if (FirstPersonMode.isFirstPersonPass()) {
            // Shadow doesn't render in first person,
            // so we don't want to make it appear during first person animation
            avatarRenderState.shadowRadius = 0.0f;
            avatarRenderState.shadowPieces.clear();
        }
    }
}
