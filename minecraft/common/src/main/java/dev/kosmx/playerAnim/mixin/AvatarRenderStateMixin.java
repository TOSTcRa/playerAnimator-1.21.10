package dev.kosmx.playerAnim.mixin;

import dev.kosmx.playerAnim.impl.IRenderStateWithAnimation;
import dev.kosmx.playerAnim.impl.animation.AnimationApplier;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AvatarRenderState.class)
public class AvatarRenderStateMixin implements IRenderStateWithAnimation {
    @Unique
    private AnimationApplier playerAnimator_animation;

    public AnimationApplier playerAnimator_getAnimation() {
        return playerAnimator_animation;
    }

    public void playerAnimator_setAnimation(AnimationApplier animation) {
        this.playerAnimator_animation = animation;
    }
}
