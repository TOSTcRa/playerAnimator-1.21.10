package dev.kosmx.playerAnim.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.impl.animation.AnimationApplier;
import dev.kosmx.playerAnim.impl.animation.IBendHelper;
import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Миксин для SimpleEquipmentLayer чтобы применять трансформации к элитрам
@Mixin(SimpleEquipmentLayer.class)
public class ElytraLayerMixin {
    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/EntityRenderState;FF)V", at = @At("HEAD"))
    private void onSubmit(PoseStack poseStack, net.minecraft.client.renderer.SubmitNodeCollector submitNodeCollector, int i, net.minecraft.client.renderer.entity.state.EntityRenderState entityRenderState, float f, float g, CallbackInfo ci) {
        // Применяем трансформацию только для AvatarRenderState (игроки)
        if (entityRenderState instanceof AvatarRenderState avatarRenderState) {
            AnimationApplier emote = ((dev.kosmx.playerAnim.impl.IRenderStateWithAnimation)avatarRenderState).playerAnimator_getAnimation();
            if (emote != null && emote.isActive()) {
                // Элитры должны следовать за торсом, применяем только ротацию и бенд
                // Не применяем позицию чтобы элитры оставались на спине
                Vec3f rotation = emote.get3DTransform("torso", TransformType.ROTATION, Vec3f.ZERO);

                // Сохраняем позицию элитр на спине (0, 0, 0.125 - дефолтная позиция)
                poseStack.translate(0.0F, 0.0F, 0.125F);
                poseStack.mulPose((new Quaternionf()).rotateXYZ(rotation.getX(), rotation.getY(), rotation.getZ()));
                IBendHelper.rotateMatrixStack(poseStack, emote.getBend("torso"));
                poseStack.translate(0.0F, 0.0F, -0.125F);
            }
        }
    }
}
