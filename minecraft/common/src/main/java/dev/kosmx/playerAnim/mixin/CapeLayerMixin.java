package dev.kosmx.playerAnim.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.kosmx.playerAnim.core.util.Pair;
import dev.kosmx.playerAnim.impl.IAnimatedPlayer;
import dev.kosmx.playerAnim.impl.animation.AnimationApplier;
import dev.kosmx.playerAnim.impl.animation.IBendHelper;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeLayer.class)
public abstract class CapeLayerMixin extends RenderLayer<net.minecraft.client.renderer.entity.state.AvatarRenderState, PlayerModel> {
    public CapeLayerMixin(RenderLayerParent<net.minecraft.client.renderer.entity.state.AvatarRenderState, PlayerModel> renderLayerParent) {
        super(renderLayerParent);
    }

    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V", at = @At("HEAD"))
    private void onSubmit(PoseStack poseStack, net.minecraft.client.renderer.SubmitNodeCollector submitNodeCollector, int i, net.minecraft.client.renderer.entity.state.AvatarRenderState avatarRenderState, float f, float g, CallbackInfo ci) {
        AnimationApplier emote = ((dev.kosmx.playerAnim.impl.IRenderStateWithAnimation)avatarRenderState).playerAnimator_getAnimation();
        if (emote == null) return;
        if (emote.isActive()) {
            ModelPart torso = this.getParentModel().body;
            Pair<Float, Float> torsoBend = emote.getBend("torso");
            Pair<Float, Float> bodyBend = emote.getBend("body");
            Pair<Float, Float> bend = new Pair<>(torsoBend.getLeft() + bodyBend.getLeft(), torsoBend.getRight() + bodyBend.getRight());

            poseStack.translate(torso.x / 16, torso.y / 16, torso.z / 16);
            poseStack.mulPose((new Quaternionf()).rotateXYZ(torso.xRot, torso.yRot, torso.zRot));
            IBendHelper.rotateMatrixStack(poseStack, torsoBend);
            poseStack.translate(0.0F, 0.0F, 0.125F);
            poseStack.mulPose(Axis.YP.rotationDegrees(180));

            // Cape is no longer part of PlayerModel in 1.21.10
            // It's rendered directly by CapeLayer, transformations applied via poseStack
            // Bend transformations are applied through IBendHelper.rotateMatrixStack called above
        }
    }

    // TODO: Возможно нужно будет добавить wrap условия для submit метода если понадобится
}
