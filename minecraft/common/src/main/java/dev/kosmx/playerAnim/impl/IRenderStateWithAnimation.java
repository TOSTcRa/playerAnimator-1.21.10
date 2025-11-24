package dev.kosmx.playerAnim.impl;

import dev.kosmx.playerAnim.impl.animation.AnimationApplier;

public interface IRenderStateWithAnimation {
    AnimationApplier playerAnimator_getAnimation();
    void playerAnimator_setAnimation(AnimationApplier animation);
}
