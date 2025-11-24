package dev.kosmx.playerAnim.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.kosmx.playerAnim.impl.Helper;
import dev.kosmx.playerAnim.impl.IUpperPartHelper;
import dev.kosmx.playerAnim.impl.animation.IBendHelper;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Ported to 1.21.10: Now wraps layer.submit() calls instead of Iterator manipulation
 * Applies bend transformations to upper body parts during rendering
 *
 * @param <T>
 * @param <M>
 */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRenderRedirect_bendOnly<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> implements RenderLayerParent<S, M> {

    @Unique
    private final ThreadLocal<Boolean> playerAnimator_bendPushed = ThreadLocal.withInitial(() -> false);

    protected LivingEntityRenderRedirect_bendOnly(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
        at = @At("HEAD"))
    private void resetBendFlag(S livingEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci){
        playerAnimator_bendPushed.set(false);
    }

    @WrapOperation(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/RenderLayer;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/EntityRenderState;FF)V"))
    private void wrapLayerSubmit(RenderLayer<S, M> layer, PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, EntityRenderState renderState, float yRot, float xRot, Operation<Void> original){
        if (Helper.isBendEnabled()) {
            // Initial push before first layer
            if (!playerAnimator_bendPushed.get()) {
                poseStack.pushPose();
                playerAnimator_bendPushed.set(true);
            }

            poseStack.pushPose();

            // Check if this is a player with active animation and upper part layer
            if (renderState instanceof dev.kosmx.playerAnim.impl.IRenderStateWithAnimation animRenderState) {
                var animation = animRenderState.playerAnimator_getAnimation();
                if (animation != null && animation.isActive() && layer instanceof IUpperPartHelper && ((IUpperPartHelper) layer).isUpperPart()) {
                    IBendHelper.rotateMatrixStack(poseStack, animation.getBend("body"));
                }
            }

            original.call(layer, poseStack, collector, lightCoords, renderState, yRot, xRot);
            poseStack.popPose();
        } else {
            original.call(layer, poseStack, collector, lightCoords, renderState, yRot, xRot);
        }
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;submit(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", shift = At.Shift.BEFORE))
    private void popMatrixStack(S livingEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci){
        if (Helper.isBendEnabled() && playerAnimator_bendPushed.get()) {
            poseStack.popPose();
            playerAnimator_bendPushed.set(false);
        }
    }
}
