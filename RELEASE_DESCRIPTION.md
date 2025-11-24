# Release Description Templates

Copy-paste these for publishing on various platforms.

---

## GitHub Release

**Tag:** `v2.0.1-1.21.10-unofficial`

**Title:** `PlayerAnimator 2.0.1 for Minecraft 1.21.10 (Unofficial Port)`

**Description:**

```markdown
# PlayerAnimator 2.0.1 for Minecraft 1.21.10 (UNOFFICIAL PORT)

⚠️ **This is an unofficial community port. Original author: [KosmX](https://github.com/KosmX)**

## 📥 Download

- **Fabric:** `player-animation-lib-2.0.1+1.21.10-fabric.jar`

## ✅ What Works

- ✅ Basic animation system
- ✅ Body part animations (head, torso, arms, legs)
- ✅ Item in hand animations
- ✅ Elytra (correct positioning on back)
- ✅ First-person animation mode
- ✅ Animation layers with priorities
- ✅ JSON animation loading (GeckoLib & Emotecraft formats)

## ⚠️ Known Limitations

- ❌ Without bendy-lib (no 1.21.10 version available)
  - Animations will look less smooth
  - Body parts won't bend in the middle
- ❌ ArmorFeatureRendererMixin disabled (setPartVisibility removed in 1.21.10)
  - Armor visibility control may not work correctly in first-person

## 📋 Requirements

- **Minecraft:** 1.21.10
- **Fabric Loader:** 0.16.0+
- **Fabric API:** 0.138.3+

## 🔧 For Developers

API remains compatible with original PlayerAnimator:

```java
AnimationStack stack = PlayerAnimationAccess.getPlayerAnimLayer(player);
stack.addAnimLayer(new ModifierLayer<>(animation));
```

Full API docs in README.md

## 📝 Changes in This Port

- ✅ Ported to Entity → RenderState architecture (1.21.2+)
- ✅ Updated all render methods to submit methods
- ✅ Fixed elytra positioning
- ✅ Ported PlayerModel.setupAnim() to new signature
- ✅ Ported HeldItemMixin (renderArmWithItem → submit)
- ❌ Disabled bendy-lib (not available for 1.21.10)
- ❌ Disabled ArmorFeatureRendererMixin (method removed)

See PORTING_NOTES.md for technical details.

## 🔗 Links

- **Original Project:** https://github.com/KosmX/minecraftPlayerAnimator
- **Original Modrinth:** https://modrinth.com/mod/playeranimator
- **Recommended Alternative:** [PAL](https://docs.zigythebird.com/pal/)
- **Discord:** https://discord.com/invite/x22jkxRpsD

## ⚖️ License

MIT License - Copyright (c) 2022 KosmX

## 🙏 Credits

- **KosmX** - Original PlayerAnimator creator
- All contributors to the original project
- Fabric team
```

---

## Modrinth Description

**Project Name:** `PlayerAnimator (Unofficial 1.21.10 Port)`

**Summary:** `Unofficial community port of PlayerAnimator library for Minecraft 1.21.10 - Animate players while maintaining mod compatibility`

**Description:**

```markdown
# ⚠️ UNOFFICIAL PORT

**This is an unofficial community port for Minecraft 1.21.10**

- **Original Author:** [KosmX](https://github.com/KosmX)
- **Original Project:** [GitHub](https://github.com/KosmX/minecraftPlayerAnimator) | [Modrinth](https://modrinth.com/mod/playeranimator)
- **License:** MIT License (Copyright © 2022 KosmX)

> The original author is no longer maintaining PlayerAnimator and recommends using [PAL](https://docs.zigythebird.com/pal/) instead.

---

## What is PlayerAnimator?

PlayerAnimator is a Minecraft library to animate the **player** while trying to break as few mods as possible.

This port adapts the library to work with Minecraft 1.21.10 (Fabric).

## Features

### ✅ Working
- Complete animation system for player model
- Position, rotation, scale, and bend transformations
- Animation layers with priority system
- JSON animation support (GeckoLib & Emotecraft formats)
- First-person animation mode
- Item in hand animations
- Proper elytra positioning during animations

### ⚠️ Limitations
- **No bendy-lib** - Smooth body bending not available (no 1.21.10 version)
  - Animations look less smooth
  - Body parts don't bend in the middle
- **Armor visibility** - First-person armor visibility may not work correctly

## For Players

Just install this mod as a library - it does nothing on its own. Other mods that use PlayerAnimator will enable their animations when this is installed.

## For Developers

The API is fully compatible with original PlayerAnimator:

```java
// Get player's animation stack
AnimationStack stack = PlayerAnimationAccess.getPlayerAnimLayer(player);

// Add animation layer
stack.addAnimLayer(new ModifierLayer<>(animation));

// Load animation from JSON
AnimationContainer animation = PlayerAnimationRegistry.getAnimation(
    new Identifier("modid", "animation_name")
);
```

### Gradle Setup

```groovy
repositories {
    // Use your repository here when published
    maven { url = 'https://maven.example.com/' }
}

dependencies {
    modImplementation "dev.kosmx.player-anim:player-animation-lib-fabric:2.0.1+1.21.10"
    include "dev.kosmx.player-anim:player-animation-lib-fabric:2.0.1+1.21.10"
}
```

## Model Structure

Animations use these body parts:
- `head` / `body` (whole player) / `torso` (chest)
- `rightArm` / `leftArm` / `rightLeg` / `leftLeg`

Place animations in: `assets/modid/player_animation/`

## Links

- [Original GitHub](https://github.com/KosmX/minecraftPlayerAnimator)
- [Recommended Alternative: PAL](https://docs.zigythebird.com/pal/)
- [Discord Community](https://discord.com/invite/x22jkxRpsD)
- [Port Technical Details](https://github.com/YourUsername/playerAnim/blob/1.21.10/PORTING_NOTES.md)

## Credits

- **KosmX** - Original PlayerAnimator creator
- Fabric team for excellent modding tools
- All contributors to the original project
```

**Categories:**
- Library
- Utility

**Tags:**
- animation
- player
- library
- fabric
- unofficial

---

## CurseForge Description

**Project Name:** `PlayerAnimator [Unofficial 1.21.10]`

**Summary:** `Unofficial community port of PlayerAnimator library for Minecraft 1.21.10 - Player animation system`

**Description:**

```
⚠️ UNOFFICIAL COMMUNITY PORT ⚠️

Original Author: KosmX
Original Project: https://github.com/KosmX/minecraftPlayerAnimator

This is an unofficial port for Minecraft 1.21.10. The original author is no longer maintaining PlayerAnimator and recommends using PAL (https://docs.zigythebird.com/pal/) instead.

═══════════════════════════════════════

WHAT IS THIS?

PlayerAnimator is a Minecraft library to animate the player while trying to break as few mods as possible.

This mod is a LIBRARY - it does nothing on its own. Other mods use it to enable player animations.

═══════════════════════════════════════

✅ FEATURES

• Complete player animation system
• Position, rotation, scale transformations
• Animation priority and layering
• JSON animation support (GeckoLib & Emotecraft)
• First-person animation mode
• Item in hand animations
• Proper elytra positioning

⚠️ LIMITATIONS

• No bendy-lib support (not available for 1.21.10)
  - Animations less smooth without body bending
• Armor visibility in first-person may not work correctly

═══════════════════════════════════════

📥 INSTALLATION

1. Install Fabric Loader 0.16.0+
2. Install Fabric API 0.138.3+
3. Install this mod
4. Install mods that use PlayerAnimator

═══════════════════════════════════════

🔧 FOR DEVELOPERS

API is compatible with original PlayerAnimator:

AnimationStack stack = PlayerAnimationAccess.getPlayerAnimLayer(player);
stack.addAnimLayer(new ModifierLayer<>(animation));

See GitHub for full documentation and Gradle setup.

═══════════════════════════════════════

🔗 LINKS

Original Project: https://github.com/KosmX/minecraftPlayerAnimator
Recommended Alternative: PAL - https://docs.zigythebird.com/pal/
Discord: https://discord.com/invite/x22jkxRpsD

═══════════════════════════════════════

⚖️ LICENSE

MIT License - Copyright (c) 2022 KosmX

═══════════════════════════════════════

🙏 CREDITS

• KosmX - Original creator
• Fabric team
• All contributors to original project
```

**Categories:**
- API and Library

---

## Version Changelog (for all platforms)

**Version:** `2.0.1+1.21.10-unofficial`

**Title:** `PlayerAnimator 2.0.1 for Minecraft 1.21.10 (Unofficial Port)`

**Changelog:**

```markdown
# PlayerAnimator 2.0.1 - Minecraft 1.21.10 (Unofficial Port)

## ⚠️ Important
This is an UNOFFICIAL community port. Original author: KosmX

## ✅ Port Changes

### Implemented
- ✅ Ported to Entity → RenderState architecture (Minecraft 1.21.2+)
- ✅ Updated PlayerModel.setupAnim() to new signature
- ✅ Ported all render() methods to submit() methods
- ✅ Fixed elytra positioning (stay on back during animations)
- ✅ Ported HeldItemMixin (renderArmWithItem removed → submit)
- ✅ Created IRenderStateWithAnimation interface for data flow
- ✅ Updated all RenderLayer mixins

### Disabled (Temporarily)
- ❌ bendy-lib support (no 1.21.10 version available)
- ❌ ArmorFeatureRendererMixin (setPartVisibility method removed)

## 🎮 Features

- Full player animation system
- Body part transformations (position, rotation, scale)
- Animation layers with priorities
- JSON animation loading (GeckoLib & Emotecraft)
- First-person mode
- Item animations
- Elytra animations

## ⚠️ Known Issues

- Animations less smooth without bendy-lib
- Body parts don't bend in middle
- Armor visibility in first-person may not work

## 📋 Requirements

- Minecraft 1.21.10
- Fabric Loader 0.16.0+
- Fabric API 0.138.3+

## 🔗 Links

- Original: https://github.com/KosmX/minecraftPlayerAnimator
- Alternative: https://docs.zigythebird.com/pal/
- Discord: https://discord.com/invite/x22jkxRpsD

## ⚖️ License
MIT License - Copyright (c) 2022 KosmX
```

---

## Social Media Post Template

```
🎮 PlayerAnimator for Minecraft 1.21.10 is here!

⚠️ Unofficial community port
👤 Original by KosmX

✅ Player animations working
✅ Fabric support
⚠️ No bendy-lib yet

Download: [link]
Original: https://github.com/KosmX/minecraftPlayerAnimator

#Minecraft #FabricMC #Modding
```

---

Use these templates when publishing to GitHub, Modrinth, or CurseForge!
