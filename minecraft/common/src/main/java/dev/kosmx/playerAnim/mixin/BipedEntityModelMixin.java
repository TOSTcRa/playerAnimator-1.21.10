package dev.kosmx.playerAnim.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.kosmx.playerAnim.core.impl.AnimationProcessor;
import dev.kosmx.playerAnim.core.util.SetableSupplier;
import dev.kosmx.playerAnim.impl.Helper;
import dev.kosmx.playerAnim.impl.IMutableModel;
import dev.kosmx.playerAnim.impl.IUpperPartHelper;
import dev.kosmx.playerAnim.impl.animation.IBendHelper;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Function;

@Mixin(HumanoidModel.class)
public abstract class BipedEntityModelMixin implements IMutableModel {
    @Final
    @Shadow
    public ModelPart rightArm;
    @Final
    @Shadow
    public ModelPart leftArm;
    @Unique
    private SetableSupplier<AnimationProcessor> animation = new SetableSupplier<>();

    @Inject(method = "<init>(Lnet/minecraft/client/model/geom/ModelPart;Ljava/util/function/Function;)V", at = @At("RETURN"))
    private void initBend(ModelPart modelPart, Function<ResourceLocation, RenderType> function, CallbackInfo ci){
        IBendHelper.INSTANCE.initBend(modelPart.getChild("body"), Direction.DOWN);
        IBendHelper.INSTANCE.initBend(modelPart.getChild("right_arm"), Direction.UP);
        IBendHelper.INSTANCE.initBend(modelPart.getChild("left_arm"), Direction.UP);
        IBendHelper.INSTANCE.initBend(modelPart.getChild("right_leg"), Direction.UP);
        IBendHelper.INSTANCE.initBend(modelPart.getChild("left_leg"), Direction.UP);
        ((IUpperPartHelper)rightArm).setUpperPart(true);
        ((IUpperPartHelper)leftArm).setUpperPart(true);
        ((IUpperPartHelper)head).setUpperPart(true);
        ((IUpperPartHelper)hat).setUpperPart(true);
    }

    @Override
    public void setEmoteSupplier(SetableSupplier<AnimationProcessor> emoteSupplier){
        this.animation = emoteSupplier;
    }

    // TODO: Port to 1.21.10 - copyPropertiesTo method removed from HumanoidModel
    // This controlled copying animation supplier when model properties are copied
    // Need to find new model copying mechanism or hook
    /*
    @Inject(method = "copyPropertiesTo", at = @At("RETURN"))
    private void copyMutatedAttributes(HumanoidModel bipedEntityModel, CallbackInfo ci){
        if(animation != null) {
            ((IMutableModel) bipedEntityModel).setEmoteSupplier(animation);
        }
    }
    */

    // renderToBuffer injection moved to ModelMixin since the method is final in Model class

    @Final
    @Shadow public ModelPart body;

    @Shadow @Final public ModelPart head;

    @Shadow @Final public ModelPart hat;

    @Shadow @Final public ModelPart rightLeg;

    @Shadow @Final public ModelPart leftLeg;

    @Override
    public SetableSupplier<AnimationProcessor> getEmoteSupplier(){
        return animation;
    }
}
