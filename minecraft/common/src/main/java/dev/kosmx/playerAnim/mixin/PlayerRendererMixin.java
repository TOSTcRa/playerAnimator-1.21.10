package dev.kosmx.playerAnim.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.impl.IAnimatedPlayer;
import dev.kosmx.playerAnim.impl.IPlayerModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, AvatarRenderState, PlayerModel> {
    public PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel entityModel, float f) {
        super(context, entityModel, f);
    }
    // TODO: Port to 1.21.10 - render() method removed, need to find new injection point
    // This controlled first person visibility - may need to move to setupAnim or extractRenderState
    /*
    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
    private void hideBonesInFirstPerson(AbstractClientPlayer entity,
                                        float f, float g, PoseStack matrixStack,
                                        MultiBufferSource vertexConsumerProvider,
                                        int i, CallbackInfo ci) {
        if (FirstPersonMode.isFirstPersonPass()) {
            var animationApplier = ((IAnimatedPlayer) entity).playerAnimator_getAnimation();
            var config = animationApplier.getFirstPersonConfiguration();

            if (entity == Minecraft.getInstance().getCameraEntity()) {
                // Hiding all parts, because they should not be visible in first person
                setAllPartsVisible(false);
                // Showing arms based on configuration
                var showRightArm = config.isShowRightArm();
                var showLeftArm = config.isShowLeftArm();
                this.getModel().rightArm.visible = showRightArm;
                this.getModel().rightSleeve.visible = showRightArm;
                this.getModel().leftArm.visible = showLeftArm;
                this.getModel().leftSleeve.visible = showLeftArm;
            }
        }

        // No `else` case needed to show parts, since the default state should be correct already
    }
    */

    @Unique
    private void setAllPartsVisible(boolean visible) {
        this.getModel().head.visible = visible;
        this.getModel().body.visible = visible;
        this.getModel().leftLeg.visible = visible;
        this.getModel().rightLeg.visible = visible;
        this.getModel().rightArm.visible = visible;
        this.getModel().leftArm.visible = visible;

        this.getModel().hat.visible = visible;
        this.getModel().leftSleeve.visible = visible;
        this.getModel().rightSleeve.visible = visible;
        this.getModel().leftPants.visible = visible;
        this.getModel().rightPants.visible = visible;
        this.getModel().jacket.visible = visible;
    }


    @Inject(method = "setupRotations(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;FF)V", at = @At("RETURN"))
    private void applyBodyTransforms(AvatarRenderState avatarRenderState, PoseStack matrixStack, float f, float g, CallbackInfo ci){
        var animationPlayer = ((dev.kosmx.playerAnim.impl.IRenderStateWithAnimation) avatarRenderState).playerAnimator_getAnimation();
        if(animationPlayer != null && animationPlayer.isActive()){

            //These are additive properties
            Vec3f vec3e = animationPlayer.get3DTransform("body", TransformType.SCALE,
                    new Vec3f(ModelPart.DEFAULT_SCALE, ModelPart.DEFAULT_SCALE, ModelPart.DEFAULT_SCALE)
            );
            matrixStack.scale(vec3e.getX(), vec3e.getY(), vec3e.getZ());
            Vec3f vec3d = animationPlayer.get3DTransform("body", TransformType.POSITION, Vec3f.ZERO);
            matrixStack.translate(vec3d.getX(), vec3d.getY() + 0.7, vec3d.getZ());
            Vec3f vec3f = animationPlayer.get3DTransform("body", TransformType.ROTATION, Vec3f.ZERO);
            matrixStack.mulPose(Axis.ZP.rotation(vec3f.getZ()));    //roll
            matrixStack.mulPose(Axis.YP.rotation(vec3f.getY()));    //pitch
            matrixStack.mulPose(Axis.XP.rotation(vec3f.getX()));    //yaw
            matrixStack.translate(0, - 0.7d, 0);
        }
    }

    // TODO: Port to 1.21.10 - renderHand() split into renderRightHand() and renderLeftHand()
    /*
    @Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/PlayerModel;setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V"))
    private void notifyModelOfFirstPerson(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, AbstractClientPlayer abstractClientPlayer, ModelPart modelPart, ModelPart modelPart2, CallbackInfo ci) {
        if (this.getModel() instanceof IPlayerModel playerModel && !((IAnimatedPlayer)abstractClientPlayer).playerAnimator_getAnimation().getFirstPersonMode().isEnabled()) {
            playerModel.playerAnimator_prepForFirstPersonRender();
        }
    }
    */
}
