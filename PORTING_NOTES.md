# Porting Notes: Minecraft 1.21.10

## Overview

This document describes the technical changes made to port PlayerAnimator from Minecraft 1.21.7 to 1.21.10.

## Major Architectural Changes in Minecraft 1.21.2+

### 1. Entity → RenderState Separation

Minecraft 1.21.2+ introduced a significant rendering architecture change:

**Before (1.21.1 and earlier):**
```java
void render(Entity entity, PoseStack poseStack, MultiBufferSource buffer, ...)
```

**After (1.21.2+):**
```java
void submit(RenderState renderState, SubmitNodeCollector collector, ...)
void extractRenderState(Entity entity, RenderState renderState, float partialTick)
```

**Key concepts:**
- **Entities** contain game logic only
- **RenderState** contains rendering data extracted from entities
- `extractRenderState()` method extracts data from entity to render state
- `submit()` method uses render state for rendering
- This separation improves performance and allows better culling

### 2. AbstractClientPlayer → Avatar / AvatarRenderState

**Player classes renamed:**
- `AbstractClientPlayer` → `Avatar` (entity)
- Player-specific render state: `AvatarRenderState`
- `PlayerRenderer` → `AvatarRenderer`

## PlayerAnimator Port Changes

### 1. Created IRenderStateWithAnimation Interface

**File:** `minecraft/common/src/main/java/dev/kosmx/playerAnim/impl/IRenderStateWithAnimation.java`

```java
public interface IRenderStateWithAnimation {
    AnimationApplier playerAnimator_getAnimation();
    void playerAnimator_setAnimation(AnimationApplier animation);
}
```

**Purpose:** Bridge pattern to pass animation data through RenderState

**Implementation:** `AvatarRenderStateMixin` implements this interface

```java
@Mixin(AvatarRenderState.class)
public class AvatarRenderStateMixin implements IRenderStateWithAnimation {
    @Unique
    private AnimationApplier playerAnimator_animation;

    // getter/setter implementation
}
```

### 2. AvatarRendererMixin - Animation Extraction

**File:** `minecraft/common/src/main/java/dev/kosmx/playerAnim/mixin/AvatarRendererMixin.java`

**Change:** Added injection to extract animation from entity to render state

```java
@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;...)")
private void onExtractRenderState(Avatar avatar, AvatarRenderState state, ...) {
    if (avatar instanceof IAnimatedPlayer animatedPlayer) {
        ((IRenderStateWithAnimation)state).playerAnimator_setAnimation(
            animatedPlayer.playerAnimator_getAnimation()
        );
    }
}
```

**Location:** `extractRenderState()` method at RETURN

### 3. PlayerModelMixin - setupAnim() Signature Change

**File:** `minecraft/common/src/main/java/dev/kosmx/playerAnim/mixin/PlayerModelMixin.java`

**Before (1.21.7):**
```java
@Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V")
private void setDefaultBeforeRender(LivingEntity entity, float f, float g, ...) {
    setDefaultPivot();
}

@Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V",
        at = @At(value = "INVOKE", target = "...ModelPart;loadPose..."))
private void setEmote(LivingEntity entity, ...) {
    if (entity instanceof AbstractClientPlayer) {
        AnimationApplier emote = ((IAnimatedPlayer)entity).playerAnimator_getAnimation();
        // Apply animation
    }
}
```

**After (1.21.10):**
```java
@Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V",
        at = @At("HEAD"))
private void setDefaultBeforeRender(AvatarRenderState renderState, CallbackInfo ci) {
    setDefaultPivot();
}

@Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V",
        at = @At("RETURN"))
private void setEmote(AvatarRenderState avatarRenderState, CallbackInfo ci) {
    AnimationApplier emote = ((IRenderStateWithAnimation)avatarRenderState).playerAnimator_getAnimation();
    if (emote != null && emote.isActive()) {
        // Apply animation
    }
}
```

**Key changes:**
- Method signature: `setupAnim(LivingEntity, FFFFF)` → `setupAnim(AvatarRenderState)`
- Animation source: Entity → RenderState
- Injection point changed from specific INVOKE to RETURN (simpler and more reliable)

### 4. HeldItemMixin - renderArmWithItem() Removed

**File:** `minecraft/common/src/main/java/dev/kosmx/playerAnim/mixin/HeldItemMixin.java`

**Issue:** The `renderArmWithItem()` method was removed from `ItemInHandLayer`

**Before (1.21.7):**
```java
@Inject(method = "renderArmWithItem",
        at = @At(value = "INVOKE", target = "...PoseStack;mulPose..."))
private void renderMixin(LivingEntity entity, ItemStack stack, ...) {
    if (entity instanceof IAnimatedPlayer player) {
        // Apply bend transformation
    }
}
```

**After (1.21.10):**
```java
@Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;...
                        Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V",
        at = @At("HEAD"))
private void applyBendTransform(PoseStack poseStack, ..., HumanoidRenderState renderState, ...) {
    if (renderState instanceof AvatarRenderState avatarState) {
        AnimationApplier emote = ((IRenderStateWithAnimation)avatarState).playerAnimator_getAnimation();
        // Apply bend transformation
    }
}
```

**Changes:**
- Method: `renderArmWithItem()` → `submit()`
- Target: Entity → RenderState
- Injection point: HEAD (apply before layer renders)

### 5. ElytraLayerMixin - Fixed Positioning

**File:** `minecraft/common/src/main/java/dev/kosmx/playerAnim/mixin/ElytraLayerMixin.java`

**Issue:** Elytra were appearing on chest instead of back during animations

**Root cause:** Translation transformation was moving elytra forward

**Fix:** Removed position transformation, kept only rotation and bend

```java
@Inject(method = "submit(...)", at = @At("HEAD"))
private void onSubmit(PoseStack poseStack, ..., EntityRenderState entityRenderState, ...) {
    if (entityRenderState instanceof AvatarRenderState avatarState) {
        AnimationApplier emote = ((IRenderStateWithAnimation)avatarState).playerAnimator_getAnimation();
        if (emote != null && emote.isActive()) {
            // Only apply rotation and bend, NOT position
            Vec3f rotation = emote.get3DTransform("torso", TransformType.ROTATION, Vec3f.ZERO);

            poseStack.translate(0.0F, 0.0F, 0.125F);  // Keep on back
            poseStack.mulPose((new Quaternionf()).rotateXYZ(rotation.getX(), rotation.getY(), rotation.getZ()));
            IBendHelper.rotateMatrixStack(poseStack, emote.getBend("torso"));
            poseStack.translate(0.0F, 0.0F, -0.125F);
        }
    }
}
```

### 6. CapeLayerMixin - Removed Cloak References

**File:** `minecraft/common/src/main/java/dev/kosmx/playerAnim/mixin/CapeLayerMixin.java`

**Issue:** The `cloak` ModelPart was removed from `PlayerModel` in 1.21.10

**Change:** Cape is now rendered directly by `CapeLayer`, not as part of PlayerModel

**Comments added:**
```java
// Cape is no longer part of PlayerModel in 1.21.10
// It's rendered directly by CapeLayer, transformations applied via poseStack
// Bend transformations are applied through IBendHelper.rotateMatrixStack
```

### 7. All RenderLayer Mixins Updated

**Changed files:**
- `LivingEntityRenderRedirect_bendOnly.java`
- `firstPerson/LivingEntityRendererMixin.java`
- `firstPerson/ItemInHandRendererMixin.java`
- `firstPerson/LevelRendererMixin.java`

**Pattern:** All `render()` methods → `submit()` methods

**Common changes:**
- Method signature updated
- Entity parameter → RenderState parameter
- MultiBufferSource → SubmitNodeCollector
- ThreadLocal pattern for state tracking where needed

### 8. ModelMixin - New File

**File:** `minecraft/common/src/main/java/dev/kosmx/playerAnim/mixin/ModelMixin.java`

**Reason:** `renderToBuffer()` is now `final` in `Model` class, cannot inject in `HumanoidModel` directly

**Solution:** Created separate mixin targeting `Model` class

```java
@Mixin(Model.class)
public abstract class ModelMixin {
    @Inject(method = "renderToBuffer(...)", at = @At("HEAD"), cancellable = true)
    private void onRenderToBuffer(PoseStack matrices, ...) {
        if (!((Object)this instanceof HumanoidModel)) return;
        // Custom rendering with bend
    }
}
```

### 9. BipedEntityModelMixin - Removed Fields

**File:** `minecraft/common/src/main/java/dev/kosmx/playerAnim/mixin/BipedEntityModelMixin.java`

**Removed @Shadow fields:**
- `jacket`, `leftSleeve`, `rightSleeve`, `leftPants`, `rightPants`

**Reason:** These fields no longer exist in `HumanoidModel`, moved to `PlayerModel` only

**Also removed:** `copyPropertiesTo()` injection (method was removed)

## Disabled Features

### 1. ArmorFeatureRendererMixin - DISABLED

**File:** `minecraft/common/src/main/java/dev/kosmx/playerAnim/mixin/ArmorFeatureRendererMixin.java`

**Status:** Commented out in `playerAnimator-common.mixins.json`

**Reason:** The `setPartVisibility(HumanoidModel, EquipmentSlot)` method was removed from `HumanoidArmorLayer`

**Impact:** Armor visibility control in first-person animations may not work correctly

**TODO:** Research new armor visibility mechanism in 1.21.10

### 2. bendy-lib Integration - DISABLED

**File:** `minecraft/fabric/build.gradle`

**Status:** Dependency commented out

```groovy
//modLocalRuntime "io.github.kosmx.bendy-lib:bendy-lib-fabric:${project.bendy_lib}"
```

**Reason:** bendy-lib is not available for Minecraft 1.21.10

**Impact:**
- Body parts won't "bend" smoothly in the middle
- Animations will look more rigid/robotic
- Visible stepping between keyframes

**Workaround:** Wait for bendy-lib update or port it separately

## Testing Status

### ✅ Tested and Working
- [x] Game launches without errors
- [x] Basic player animations
- [x] Body part transformations (position, rotation, scale)
- [x] Item in hand follows arm animations
- [x] Elytra stay on back during animations
- [x] Animation layers and priorities
- [x] JSON animation loading (Emotecraft format)
- [x] First-person mode (basic)

### ⚠️ Needs More Testing
- [ ] Compatibility with mods using playerAnimator API
- [ ] Complex multi-layer animations
- [ ] First-person mode edge cases
- [ ] Multiplayer synchronization
- [ ] Performance with many animated players

### ❌ Known Issues
- [ ] Animations look less smooth without bendy-lib
- [ ] Armor visibility in first-person mode (ArmorFeatureRendererMixin disabled)

## Performance Notes

The Entity → RenderState separation in 1.21.2+ should improve performance:
- Better render culling
- Less data passed to render thread
- Cleaner separation of concerns

PlayerAnimator port maintains this architecture, so performance should be good or better than original.

## Compatibility Notes

### Mods Using PlayerAnimator API

The public API remains unchanged:
```java
PlayerAnimationAccess.getPlayerAnimLayer(player)
PlayerAnimationRegistry.getAnimation(identifier)
AnimationStack.addAnimLayer(layer)
```

Mods using these APIs should work without changes.

### Mods Mixing into PlayerAnimator

Mods that mixin into PlayerAnimator internals may need updates:
- Check for Entity → RenderState changes
- Update any render method injections
- Check for removed/renamed classes

## Future Work

### High Priority
1. **Port bendy-lib** or wait for official update
2. **Fix ArmorFeatureRendererMixin** - research new armor visibility system
3. **Comprehensive testing** with popular animation mods

### Medium Priority
4. Test multiplayer synchronization
5. Performance profiling with many players
6. Edge case testing (death, dimension changes, etc.)

### Low Priority
7. Optimize animation data flow
8. Add more debug logging options
9. Update documentation with 1.21.10 specific notes

## References

- [Minecraft 1.21 Migration Primer](https://gist.github.com/ChampionAsh5357/d895a7b1a34341e19c80870720f9880f)
- [Fabric Wiki - Rendering](https://fabricmc.net/wiki/tutorial:rendering)
- [SpongePowered Mixin Documentation](https://github.com/SpongePowered/Mixin/wiki)
- [Original PlayerAnimator](https://github.com/KosmX/minecraftPlayerAnimator)

## Questions?

For questions about this port, open an issue in the repository.

For general PlayerAnimator questions, join the [Discord](https://discord.com/invite/x22jkxRpsD).
