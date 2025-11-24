package dev.kosmx.playerAnim.mixin.firstPerson;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.impl.IAnimatedPlayer;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Unique
    private boolean defaultCameraState = false;

    // @Redirect(at = @At(target = "Lnet/minecraft/client/Camera;isDetached()Z")) is forbidden

    @Inject(method = "extractVisibleEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;isDetached()Z"))
    private void fakeThirdPersonMode(Camera camera, net.minecraft.client.renderer.culling.Frustum frustum, DeltaTracker deltaTracker, net.minecraft.client.renderer.state.LevelRenderState levelRenderState, CallbackInfo ci) {
        // mods may need to redirect that method, I want to avoid compatibility issues as long as possible
        defaultCameraState = camera.isDetached();
        if (camera.getEntity() instanceof IAnimatedPlayer player && (player.playerAnimator_getAnimation().getFirstPersonMode() == FirstPersonMode.THIRD_PERSON_MODEL)) {
            FirstPersonMode.setFirstPersonPass(!camera.isDetached() && (!(camera.getEntity() instanceof LivingEntity) || !((LivingEntity) camera.getEntity()).isSleeping())); // this will cause a lot of pain
            ((CameraAccessor) camera).setDetached(true);
        }
    }

    @Inject(method = "extractVisibleEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;isDetached()Z", shift = At.Shift.AFTER))
    private void resetThirdPerson(Camera camera, net.minecraft.client.renderer.culling.Frustum frustum, DeltaTracker deltaTracker, net.minecraft.client.renderer.state.LevelRenderState levelRenderState, CallbackInfo ci) {
        ((CameraAccessor) camera).setDetached(defaultCameraState);
    }


    @Inject(method = "extractVisibleEntities", at = @At("RETURN"))
    private void dontRenderEntity_End(Camera camera, net.minecraft.client.renderer.culling.Frustum frustum, DeltaTracker deltaTracker, net.minecraft.client.renderer.state.LevelRenderState levelRenderState, CallbackInfo ci) {
        // After all entities are extracted, unmark first-person pass
        FirstPersonMode.setFirstPersonPass(false);
    }
}
