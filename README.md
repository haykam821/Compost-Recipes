# Compost Recipes

[![GitHub release](https://img.shields.io/github/release/haykam821/Compost-Recipes.svg?style=popout&label=github)](https://github.com/haykam821/Compost-Recipes/releases/latest)
[![CurseForge](https://img.shields.io/static/v1?style=popout&label=curseforge&message=project&color=6441A4)](https://www.curseforge.com/minecraft/mc-mods/compost-recipes)
[![Discord](https://img.shields.io/static/v1?style=popout&label=chat&message=discord&color=7289DA)](https://discord.gg/3WMUMgb)

Adds recipe support for the composter.

Compost Recipes requires the [Fabric modloader](https://fabricmc.net/use/) and [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api).

## Installation

1. Install [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) if it is not installed.
2. Download Compost Recipes from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/compost-recipes/files) or [GitHub](https://github.com/haykam821/Compost-Recipes/releases).
3. Place the downloaded file in your `mods` folder.

## Usage

Add a recipe using the following format to your datapack:

```json
{
	"type": "compostrecipes:composting",
	"ingredient": {
		"item": "minecraft:dead_bush"
	},
	"chance": 0.5,
	"layers": 3
}
```
