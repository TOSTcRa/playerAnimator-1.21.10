# ⚠️ UNOFFICIAL PORT: PlayerAnimator for Minecraft 1.21.10

**This is an unofficial community port of the PlayerAnimator library for Minecraft 1.21.10**

## 📢 Important Notice

- ⚠️ **UNOFFICIAL PORT** - Not supported by the original author
- 👤 **Original Author:** [KosmX](https://github.com/KosmX)
- 📦 **Original Project:** [minecraftPlayerAnimator](https://github.com/KosmX/minecraftPlayerAnimator)
- ⚖️ **License:** MIT License (Copyright © 2022 KosmX)
- 🔄 **Recommended Alternative:** [PAL (Player Animator Library)](https://docs.zigythebird.com/pal/)

> The original author is no longer maintaining PlayerAnimator and recommends using PAL instead.

## 📦 What is this?

PlayerAnimator is a Minecraft library to animate the **player** while trying to break as few mods as possible.

This port adapts the library to work with Minecraft 1.21.10 (Fabric).

## ✅ What Works

- ✅ Basic player animation system
- ✅ Body part animations (head, torso, arms, legs)
- ✅ Item in hand animations
- ✅ Elytra animations (correctly positioned on back)
- ✅ Animation layer system with priorities
- ✅ Animation loading from JSON (GeckoLib and Emotecraft formats)
- ✅ First-person animation mode
- ✅ Bend transformations for body parts

## ⚠️ Known Limitations

- ❌ **Without bendy-lib** - The smooth bending library is not ported to 1.21.10
  - Animations will look less smooth
  - Body parts won't "bend" in the middle
- ❌ **ArmorFeatureRendererMixin disabled** - The `setPartVisibility` method was removed in 1.21.10
  - Armor visibility control in first-person mode may not work correctly

## 📥 Installation

### For Players

1. Download the latest version from [Releases](../../releases)
2. Install [Fabric Loader](https://fabricmc.net/use/) 0.16.0+
3. Install [Fabric API](https://modrinth.com/mod/fabric-api) 0.138.3+
4. Place the JAR file in your `mods` folder

### For Developers

This port maintains API compatibility with the original library.

#### Gradle Setup (Fabric)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    modImplementation "com.github.TOSTcRa:playerAnimator-1.21.10:v2.0.1-1.21.10-unofficial"
    include "com.github.TOSTcRa:playerAnimator-1.21.10:v2.0.1-1.21.10-unofficial"
}
```

**Note:** JitPack automatically builds from GitHub releases. Make sure you're using a valid release tag.

#### Basic Usage

The API remains the same as the original:

```java
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;

// Get animation stack for player
AnimationStack animationStack = PlayerAnimationAccess.getPlayerAnimLayer(clientPlayer);

// Add animation layer
animationStack.addAnimLayer(new ModifierLayer<>(animation));
```

For full API documentation, see the [original README](README_ORIGINAL.md).

## 📝 Changelog (1.21.10 Port)

### Major Changes from 1.21.7 → 1.21.10:

- ✅ Ported to new Entity → RenderState system (1.21.2+)
- ✅ Updated PlayerModel.setupAnim() to new signature
- ✅ Ported HeldItemMixin (renderArmWithItem → submit)
- ✅ Fixed elytra positioning
- ✅ Updated all render/submit methods for new architecture
- ✅ Created IRenderStateWithAnimation interface for animation data flow
- ✅ Ported all mixin injections to submit() methods
- ❌ Temporarily disabled bendy-lib (no 1.21.10 version available)
- ❌ Temporarily disabled ArmorFeatureRendererMixin (setPartVisibility removed)

See detailed changes in [PORTING_NOTES.md](PORTING_NOTES.md)

## 🎮 Model Structure

The player model consists of 6 body parts:
- **head** - Player's head
- **torso** - Upper body/chest
- **right_arm** / **rightArm** - Right arm
- **left_arm** / **leftArm** - Left arm
- **right_leg** / **rightLeg** - Right leg
- **left_leg** / **leftLeg** - Left leg

Plus an extra part: **body**
- This is a bone for the whole player
- Transforming it will transform every part

> Most Blockbench player models use the name `body` for what we call `torso`. Rename it to `torso` to fix compatibility.

### Supported Transformations

- **Position** (offset)
- **Rotation**
- **Scale**
- **Bend** (if bendy-lib is loaded)

### Animation Formats

You can use **GeckoLib** or **Emotecraft** format to create animations.

Place animation files in: `assets/modid/player_animation/`

Load animations with:
```java
AnimationContainer animation = PlayerAnimationRegistry.getAnimation(
    new Identifier("modid", "animation_name")
);
```

## 🔗 Links

### Original Project
- [GitHub](https://github.com/KosmX/minecraftPlayerAnimator)
- [Modrinth](https://modrinth.com/mod/playeranimator)
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/playeranimator)
- [KosmX's Maven](https://maven.kosmx.dev/dev/kosmx/player-anim/)

### Recommended Alternative
- [PAL (Player Animator Library)](https://docs.zigythebird.com/pal/)
- [Migration Guide from PlayerAnimator to PAL](https://docs.zigythebird.com/pal/how_to_port_from_player_animator/)

### Community
- [Discord Server](https://discord.com/invite/x22jkxRpsD)

## 🙏 Credits

- **KosmX** - For creating the original PlayerAnimator library
- All contributors to the original project
- Fabric team for excellent modding tools

## 🐛 Bug Reports

For bugs specific to the 1.21.10 port, please open an issue in this repository.

For general PlayerAnimator questions or API usage, refer to the [original project](https://github.com/KosmX/minecraftPlayerAnimator) or join the [Discord](https://discord.com/invite/x22jkxRpsD).

## ⚖️ License

MIT License

```
Copyright (c) 2022 KosmX (original project)
Copyright (c) 2025 TOSTcR (1.21.10 port)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

Full license text in [LICENSE](LICENSE) file.
