# Publishing Checklist

## ✅ What's Done (Automated)

- [x] README.md created with port information
- [x] PORTING_NOTES.md created with technical details
- [x] RELEASE_DESCRIPTION.md created with templates for all platforms
- [x] Original README renamed to README_ORIGINAL.md
- [x] LICENSE file preserved with original copyright
- [x] Project compiled successfully
- [x] JAR file built: `minecraft/fabric/build/libs/player-animation-lib-fabric-2.0.1+1.21.10.jar`

## 📝 What You Need To Do

### 1. Update Author Information

Edit these files and replace `[Your Username]` / `[Port Maintainer]` with your name/username:

**README.md** (line near bottom):
```markdown
Copyright (c) 2025 [Port Maintainer] (1.21.10 port)
```

**RELEASE_DESCRIPTION.md** (multiple places):
- Look for `[Your Username]` placeholders
- Replace with your GitHub username or name

### 2. Test the Build

```bash
# Test in game one more time
./gradlew :minecraft:fabric:runTestmodClient
```

**Verify:**
- [ ] Game launches
- [ ] No mixin errors
- [ ] Animations work (if you have test animations)
- [ ] Items in hands follow animations
- [ ] Elytra stay on back

### 3. Create GitHub Repository (if needed)

If you don't have a GitHub repo yet:

1. Go to https://github.com/new
2. Name: `playerAnimator` or `playerAnimator-1.21.10`
3. Description: `Unofficial port of PlayerAnimator for Minecraft 1.21.10`
4. Public repository
5. Don't initialize with README (we have one)

Then push your code:
```bash
git init
git add .
git commit -m "Initial port to Minecraft 1.21.10"
git branch -M 1.21.10
git remote add origin https://github.com/YourUsername/playerAnimator.git
git push -u origin 1.21.10
```

### 4. Create GitHub Release

1. Go to your repository
2. Click "Releases" → "Create a new release"
3. Fill in:
   - **Tag:** `v2.0.1-1.21.10-unofficial`
   - **Title:** Copy from `RELEASE_DESCRIPTION.md` (GitHub Release section)
   - **Description:** Copy from `RELEASE_DESCRIPTION.md` (GitHub Release section)
4. Upload file: `minecraft/fabric/build/libs/player-animation-lib-fabric-2.0.1+1.21.10.jar`
5. Check "This is a pre-release" (since it's a port)
6. Publish release

### 5. Publish on Modrinth

1. Go to https://modrinth.com/
2. Sign in / Create account
3. Click "Create a project"
4. Fill in details from `RELEASE_DESCRIPTION.md` (Modrinth section)
5. Upload version:
   - **File:** `player-animation-lib-fabric-2.0.1+1.21.10.jar`
   - **Version number:** `2.0.1+1.21.10-unofficial`
   - **Loader:** Fabric
   - **Game versions:** 1.21.10
   - **Dependencies:** Fabric API (Required)
   - **Changelog:** Copy from `RELEASE_DESCRIPTION.md`
6. Publish

### 6. Publish on CurseForge (Optional)

1. Go to https://www.curseforge.com/
2. Sign in / Create account
3. Go to https://authors.curseforge.com/
4. Create Project
5. Fill in details from `RELEASE_DESCRIPTION.md` (CurseForge section)
6. Upload file:
   - **File:** `player-animation-lib-fabric-2.0.1+1.21.10.jar`
   - **Display Name:** `PlayerAnimator 2.0.1 for MC 1.21.10 (Unofficial)`
   - **Game Version:** 1.21.10
   - **Mod Loader:** Fabric
   - **Release Type:** Beta
   - **Changelog:** Copy from `RELEASE_DESCRIPTION.md`
7. Publish

### 7. Optional: Join Discord

Consider joining the official Discord to:
- Announce your port
- Get help if needed
- Connect with other mod developers

Discord: https://discord.com/invite/x22jkxRpsD

## 📋 Pre-Publishing Checklist

Before publishing, verify:

- [ ] README.md has correct author name
- [ ] RELEASE_DESCRIPTION.md has correct GitHub username
- [ ] LICENSE file is unchanged (contains original copyright)
- [ ] JAR file builds without errors
- [ ] Tested in game successfully
- [ ] GitHub repository created (if using)
- [ ] Git commits made
- [ ] All markdown files reviewed

## 🔍 Post-Publishing Checklist

After publishing:

- [ ] GitHub Release created with JAR attached
- [ ] Modrinth project published
- [ ] CurseForge project published (optional)
- [ ] Links in README.md updated to point to your releases
- [ ] Announcement in Discord (optional)

## ⚠️ Important Reminders

1. **Always mention it's unofficial** in titles and descriptions
2. **Credit KosmX** as original author
3. **Link to original project** in all descriptions
4. **Mention PAL as recommended alternative**
5. **Include MIT License** in all distributions

## 📧 Communication Template

If you want to inform the original author (optional):

```
Subject: Unofficial Port for Minecraft 1.21.10

Hi KosmX,

I've created an unofficial community port of PlayerAnimator for Minecraft 1.21.10.

Repository: [Your GitHub link]
Release: [Your release link]

I've:
- Maintained MIT License with your copyright
- Clearly marked it as unofficial
- Linked to original project
- Recommended PAL as alternative

Let me know if you have any concerns.

Thanks for the amazing library!
```

## 📊 Tracking Success

Monitor these metrics after release:
- GitHub stars/forks
- Modrinth downloads
- CurseForge downloads
- Issues reported
- Community feedback

## 🎉 You're Ready!

Everything is prepared. Just fill in your details and publish!

Good luck with your port! 🚀
