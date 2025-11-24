package dev.kosmx.playerAnim.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.kosmx.playerAnim.core.impl.AnimationProcessor;
import dev.kosmx.playerAnim.core.util.SetableSupplier;
import dev.kosmx.playerAnim.impl.Helper;
import dev.kosmx.playerAnim.impl.IMutableModel;
import dev.kosmx.playerAnim.impl.IUpperPartHelper;
import dev.kosmx.playerAnim.impl.animation.IBendHelper;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Model.class)
public abstract class ModelMixin {
    
    @Inject(method = "renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V", at = @At("HEAD"), cancellable = true)
    private void onRenderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, int color, CallbackInfo ci){
        // Only apply to HumanoidModel instances
        if (!((Object)this instanceof HumanoidModel)) return;
        
        HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;
        
        if (!(model instanceof IMutableModel)) return;
        SetableSupplier<AnimationProcessor> animation = ((IMutableModel) model).getEmoteSupplier();
        
        if(Helper.isBendEnabled() && animation != null && animation.get() != null && animation.get().isActive()){
            List<ModelPart> headParts = List.of(model.head, model.hat);
            List<ModelPart> bodyParts = List.of(model.body, model.rightArm, model.leftArm, model.rightLeg, model.leftLeg);
            
            headParts.forEach((part)->{
                if(! ((IUpperPartHelper) part).isUpperPart()){
                    part.render(matrices, vertices, light, overlay, color);
                }
            });
            bodyParts.forEach((part)->{
                if(! ((IUpperPartHelper) part).isUpperPart()){
                    part.render(matrices, vertices, light, overlay, color);
                }
            });

            matrices.pushPose();
            IBendHelper.rotateMatrixStack(matrices, animation.get().getBend("body"));
            headParts.forEach((part)->{
                if(((IUpperPartHelper) part).isUpperPart()){
                    part.render(matrices, vertices, light, overlay, color);
                }
            });
            bodyParts.forEach((part)->{
                if(((IUpperPartHelper) part).isUpperPart()){
                    part.render(matrices, vertices, light, overlay, color);
                }
            });
            matrices.popPose();
            ci.cancel();
        }
    }
}
