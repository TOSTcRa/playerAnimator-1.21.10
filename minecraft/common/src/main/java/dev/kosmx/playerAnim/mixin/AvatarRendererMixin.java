package dev.kosmx.playerAnim.mixin;

import dev.kosmx.playerAnim.impl.IAnimatedPlayer;
import dev.kosmx.playerAnim.impl.IRenderStateWithAnimation;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("RETURN"))
    private void onExtractRenderState(Avatar avatar, AvatarRenderState avatarRenderState, float partialTick, CallbackInfo ci) {
        // Копируем данные анимации из entity в renderState
        if (avatar instanceof IAnimatedPlayer animatedPlayer) {
            ((IRenderStateWithAnimation)avatarRenderState).playerAnimator_setAnimation(
                animatedPlayer.playerAnimator_getAnimation()
            );
        }
    }
}
