package dev.kosmx.playerAnim.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.impl.animation.AnimationApplier;
import dev.kosmx.playerAnim.core.util.Pair;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.impl.Helper;
import dev.kosmx.playerAnim.impl.IRenderStateWithAnimation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.HumanoidArm;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public class HeldItemMixin {

    // Port to 1.21.10: renderArmWithItem was removed, using submit method instead
    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V",
            at = @At(value = "HEAD"))
    private void applyBendTransform(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, HumanoidRenderState renderState, float yRot, float partialTick, CallbackInfo ci){
        if(Helper.isBendEnabled() && renderState instanceof net.minecraft.client.renderer.entity.state.AvatarRenderState avatarState){
            AnimationApplier emote = ((IRenderStateWithAnimation)avatarState).playerAnimator_getAnimation();
            if(emote != null && emote.isActive()){
                // Determine which arm - we need to check the item being held
                // For now apply bend to both arms (the layer will only render the arm that has an item)
                HumanoidArm mainArm = avatarState.mainArm;

                // Apply bend transformation for the arm with item
                Vec3f data = emote.get3DTransform(mainArm == HumanoidArm.LEFT ? "leftArm" : "rightArm", TransformType.BEND, new Vec3f(0f, 0f, 0f));

                Pair<Float, Float> pair = new Pair<>(data.getX(), data.getY());

                float offset = 0.25f;
                poseStack.translate(0, offset, 0);
                float bend = pair.getRight();
                float axisf = - pair.getLeft();
                Vector3f axis = new Vector3f((float) Math.cos(axisf), 0, (float) Math.sin(axisf));
                poseStack.mulPose(new Quaternionf().rotateAxis(bend, axis));
                poseStack.translate(0, - offset, 0);
            }
        }
    }
}
