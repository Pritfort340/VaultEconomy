# VaultEconomy#

### Economy, server utilities, and custom gameplay in one plugin.

**VaultEconomy 3.0.0-rc1** combines a persistent economy with auctions, clans, teleportation, moderation tools, configurable items, custom recipes, brewing, interactive fishing, achievements, and visual effects.

Build an economy for a survival server, create progression for an RPG world, reward fishing and exploration, or connect several servers to a shared SQL economy.

**Server-side gameplay • Optional integrations • 11 storage backends • English and Russian translations**

> **Release status:** This version is a release candidate. The source archive includes automated test results and API compatibility checks. These checks do not establish live compatibility with every server version, plugin combination, or Folia configuration.

---

## ✨ What Is Included?

| System | Features |
|---|---|
| Economy | Balances, payments, leaderboards, administrative money management, custom currencies |
| Vault integration | Economy provider for compatible shops and other plugins |
| Auction house | Listings, purchase confirmation, taxes, expiration, returns, delivery queue |
| Clans | Roles, invitations, treasury, clan home, storage upgrades, banners, diplomacy |
| Social tools | Private messages, mail, marriage, nicknames, prefixes, staff chat |
| Teleportation | Spawn, homes, warps, requests, previous locations, random teleportation |
| Administration | Moderation, inventory inspection, player controls, item editing, jail |
| Custom items | YAML definitions, potions, click actions, command actions, custom furnaces |
| Recipes | Shaped crafting, shapeless crafting, smelting, recipe blocking |
| Brewing | Custom ingredients, results, timing, and fuel settings |
| Fishing | Timing minigame, configurable species, sizes, prices, buyer menu |
| Achievements | Event-based progress, money rewards, item rewards, console commands |
| Visual effects | 15 particle animation patterns, sounds, titles, spear charging |
| Storage | Five file formats, two embedded databases, four remote database types |
| Localization | Included English and Russian translations, additional language files |
| Proxy module | Network commands for Velocity and BungeeCord |

The Bukkit plugin registers **108 root commands**. Its extension catalog contains **391 distinct command paths**, including individual effect and attribute operations.

There are **819 static permission definitions**, with additional permissions registered for configured items, recipes, and achievements.

These counts exclude command aliases from the extension-path total.

---

## 🎮 What Can You Use It For?

### Survival servers

Provide balances, player trading, homes, warps, clans, kits, private storage, and custom fishing rewards.

### RPG and progression servers

Create potions, collectible tokens, special tools, crafting chains, brewing recipes, and achievements with configurable rewards.

### Community servers

Manage messages, mail, nicknames, staff communication, moderation, marriage, and shared clan activities.

### Events and custom gameplay

Use charged spears, particles, titles, player effects, item actions, and command rewards to build server events.

### Server networks

Use the proxy module for server switching and network messaging. A shared remote SQL backend can connect the economy across participating servers.

> VaultEconomy provides these systems and configuration tools. It does not automatically create a complete RPG campaign, NPC system, land-claim system, or custom resource pack.

---

## 🧩 Platforms and Minecraft Compatibility

### Gameplay servers

| Platform | Implementation and verification |
|---|---|
| Bukkit / CraftBukkit | Bukkit entry point with `api-version: 1.13` |
| Spigot | Bukkit gameplay implementation; direct API references checked against 1.13 and 1.21.11 |
| Paper | Bukkit gameplay implementation with optional use of available Paper APIs |
| Purpur | Uses the Bukkit/Paper gameplay implementation |
| Folia | Declares Folia support and includes entity, region, and global scheduling adapters |
| Paper 26.2 | Included static API report checks 519 direct Bukkit references without reported errors |

The code targets a **Bukkit 1.13 API baseline**, but individual features depend on the APIs available in the running server.

The included reports cover **Spigot 1.13, Spigot 1.21.11, and Paper 26.2**. They are API checks, not evidence that every version between those releases has been tested in a running server.

### Proxy servers

| Platform | Included module |
|---|---|
| Velocity | Separate proxy entry point; compiled against the included Velocity API |
| BungeeCord | Separate proxy entry point; compiled against the included BungeeCord API |

The proxy module provides network commands and optional shared economy access.

**Crafting, brewing, fishing, inventories, custom items, and jail mechanics run on the gameplay servers.** Installing the plugin only on a proxy does not enable those mechanics on its backends.

### Java requirements

Use the Java version required by your server software and selected dependencies.

The plugin’s own classes are compiled to Java 8 bytecode. This does not mean that a modern Minecraft server or every bundled dependency can run on Java 8.

### Features with additional API requirements

- **CustomModelData:** requires a compatible API, normally Minecraft 1.14 or newer.
- **Custom furnace recovery:** requires `BlockDropItemEvent`; placement is disabled on the original 1.13 API when that support is unavailable.
- **Furnace speed changes:** require `FurnaceStartSmeltEvent`.
- **Virtual anvil:** requires the appropriate `openAnvil` API.
- **New attributes and potion effects:** available only when supported by the server.
- **Darkness:** falls back to a different visual approximation on older versions.
- **Mob disguises:** require compatible LibsDisguises and its dependencies.

Live testing across Paper, Purpur, Spigot, Folia, Velocity, and BungeeCord is not established by the supplied automated reports.

---

## 📥 Installation

1. Stop the server.
2. Back up the existing `plugins/VaultEconomy` directory and relevant world/player data.
3. Remove the previous VaultEconomy JAR.
4. Place `VaultEconomy-3.0.0-rc1.jar` in the server’s `plugins` directory.
5. Start the server.
6. Check the console for startup errors.
7. Run:

```text
/ve status
/vex status
```

8. Configure permissions and test with a normal, non-operator account.

Only one VaultEconomy version should be installed at a time.

### Default storage

The default backend is **SQLite**.

It creates a local database automatically, so you do not need to install or configure an external database server.

### Optional dependencies

| Plugin | Purpose |
|---|---|
| Vault | Exposes VaultEconomy as an economy provider |
| PlaceholderAPI | Enables the included placeholders |
| LuckPerms | Optional permission management |
| LibsDisguises | Required for actual mob disguises |
| ProtocolLib | Listed as an optional dependency; requirements depend on the disguise setup |

The built-in economy commands work without Vault.

If several installed plugins provide a Vault economy, check which provider your shops and other integrations actually use.

### Command conflicts

Use the plugin namespace when another plugin registers the same command:

```text
/vaulteconomy:balance
/vaulteconomy:pay Alex 25
/vaulteconomy:gamemode creative
```

---

## 🚀 First-Time Setup

The bundled configuration defaults to Russian. For an English server, change the following values in `config.yml`:

```yaml
language: en

currency:
  symbol: "$"
  singular: coin
  plural: coins

rules:
  - "&6Server Rules"
  - "&fRespect other players."
  - "&fDo not exploit bugs."

motd:
  - "&bWelcome! &fOpen /menu to get started."
```

Then run:

```text
/ve reload
/language reload
/language set en
```

Some bundled item, fish, and achievement names contain both English and Russian. Their names belong to the content definitions, so edit them separately in:

```text
items.yml
fishing.yml
achievements.yml
```

Changing the interface language does not automatically rewrite names or lore on existing items.

---

## 💰 Economy

VaultEconomy provides a main currency and additional configurable currencies.

### Main currency commands

```text
/balance
/balance Alex
/pay Alex 25.50
/baltop

/eco give Alex 100
/eco take Alex 25
/eco set Alex 500
```

The main balance command also has these aliases:

```text
/bal
/money
/bits
```

### Custom currencies

Create separate currencies for events, rewards, or progression:

```text
/point create tokens T
/point list
/point give tokens Alex 50
/point balance tokens Alex
/point pay tokens Alex 5
/baltop tokens
```

Administrative operations include:

```text
/point give <id> <player> <amount>
/point take <id> <player> <amount>
/point set <id> <player> <amount>
```

The Vault bridge exposes the **main money balance**. Additional `/point` currencies are managed through VaultEconomy’s own commands and placeholders.

### Money handling

- Amounts are stored as integer hundredths.
- Commands accept up to two decimal places.
- Invalid negative amounts, non-finite values, and overflow are rejected.
- Payments to yourself are rejected.
- Transfers are committed as transactions.
- A failed storage write is not reported as a successful payment.

### Vault integration

Compatible plugins can use the Vault economy interface to:

- Read balances.
- Check available funds.
- Deposit money.
- Withdraw money.
- Format currency values.

**Vault bank accounts are not implemented.** Clan treasuries use internal accounts and do not expose the Vault bank API.

### Audit history

```text
/ve audit
/ve audit 25
```

The audit command displays recent economy activity. The stored journal retains the latest **2,000 entries**, rather than an unlimited accounting history.

---

## 🛍️ Auction House

Players can sell the item stack in their main hand:

```text
/ah sell 250
```

Open the market:

```text
/ah
```

Other actions:

```text
/ah mine
/ah cancel <listing-id>
/ah claim
/claim
/ahelp
```

### Auction behavior

- Listings show the seller and price.
- Purchases use a confirmation screen.
- Sellers can receive money while offline.
- Purchased items enter a delivery queue.
- Expired listings return through the owner’s delivery queue.
- Cancelled listings are returned through the same mechanism.
- `/claim` retrieves the next queued delivery when inventory space is available.

### Configuration

```yaml
auction:
  tax-percent: 3.0
  lifetime-seconds: 172800

limits:
  auction: 5
```

| Setting | Meaning |
|---|---|
| `tax-percent` | Tax applied to auction sales |
| `lifetime-seconds` | Listing lifetime; `172800` is 48 hours |
| `limits.auction` | Base listing limit per player |

### Recovery tools

For an interrupted operation whose item delivery cannot be determined automatically:

```text
/ah recover list
/ah recover <operation-id> restore
/ah recover <operation-id> discard
```

These administrative operations require:

```text
vaulteconomy.ah.recover
```

Recovery decisions should follow an inventory and log check. The plugin does not automatically repeat an ambiguous item delivery.

---

## 🛡️ Clans

Clans combine social features with shared economic resources.

### Included features

- Four roles: member, officer, deputy, and owner.
- Invitations and open joining.
- Clan treasury.
- Clan home.
- Shared storage from 27 to 54 slots.
- Storage upgrades.
- Clan banner.
- Member promotion and demotion.
- Ownership transfer.
- Clan chat.
- PvP toggle.
- Alliances and war relationships.

### Commands

```text
/clan
/clan help
/clan create <name>
/clan info
/clan invite <player>
/clan accept
/clan deny
/clan join <clan>
/clan leave

/clan balance
/clan deposit <amount>
/clan withdraw <amount>

/clan home
/clan sethome
/clan storage
/clan upgrade

/clan promote <player>
/clan demote <player>
/clan kick <player>
/clan transfer <player>

/clan pvp
/clan open
/clan chat <message>
/clan setbanner
/clan change <name>
/clan delete confirm

/clan ally <clan>
/clan unally <clan>
/clan war <clan>
/clan peace <clan>
```

### Default prices and limits

```yaml
clans:
  create-price: "1000.00"
  storage-upgrade-price: "2000.00"
  max-members: 20
```

### Roles and permissions

Command permissions and clan roles are checked separately.

For example:

- Inviting members and setting the clan home require officer-level authority.
- Withdrawing treasury funds and upgrading storage require deputy-level authority.
- Ownership-sensitive actions have additional role checks.

Granting a command permission does not automatically promote a player within a clan.

Clan storage is opened by one player at a time on the current server.

**Alliances and wars represent clan relationships.** This release does not include territory claiming or a separate territorial warfare system.

---

## 🏠 Teleports, Homes, and Warps

Available systems include:

- Server spawn.
- Named player homes.
- Public warps.
- Teleport requests.
- Return to a previous location or death location.
- Administrative teleports.
- Random teleportation.
- Clan homes.

### Player commands

```text
/spawn
/sethome [name]
/home [name]
/delhome [name]
/homes

/warp <name>
/warps
/back

/tpa <player>
/tpahere <player>
/tpaccept
/tpdeny
/tptoggle
/tpcancel
```

Teleport requests expire after **60 seconds**.

### Administrative commands

```text
/setspawn
/setwarp <name>
/delwarp <name>
/tp <player>
/tphere <player>
/tppos <x> <y> <z>
/rtp
```

`/rtp` is operator-only by default. It searches for a suitable location in a normal world, with a limited number of attempts.

### Configuration

```yaml
teleport:
  delay: 3

cooldowns:
  spawn: 10
  home: 10
  warp: 10
  back: 10
  tpa: 5
  rtp: 120
  clanhome: 15

limits:
  homes: 2
  near: 100
  auction: 5

rtp:
  radius: 2000
```

These values are seconds, except for counts and distances.

The separate `command-cooldowns` section controls command-level throttling:

```yaml
command-cooldowns:
  tpa: 5
  tpahere: 5
  pay: 1
  msg: 1
  rtp: 15
  ah: 1
  heal: 30
  feed: 30
```

---

## ⚔️ Combat Restrictions

```yaml
combat:
  seconds: 15
  kill-on-logout: true
  allowed-commands:
    - balance
    - msg
    - reply
    - clan
```

| Setting | Meaning |
|---|---|
| `seconds` | Combat-tag duration |
| `kill-on-logout` | Whether disconnecting during combat kills the player |
| `allowed-commands` | Commands allowed through the general combat command restriction |

Individual handlers can impose additional restrictions. For example, clan operations also check combat state.

Administrative bypass:

```text
vaulteconomy.combat.bypass
```

---

## 📦 Custom Items

Create reusable item definitions in `items.yml`, or manage them through `/customitem`.

Definitions use existing Minecraft materials. The plugin can add metadata, effects, actions, and custom gameplay behavior.

It does not add entirely new client materials by itself.

### Included definitions

| ID | Purpose |
|---|---|
| `strength_three` | Strength III potion |
| `ocean_elixir` | Water Breathing and Night Vision potion |
| `prison_wand` | Administrative player-targeting jail wand |
| `storm_spear` | Chargeable custom trident |
| `swift_furnace` | Configurable custom furnace |
| `reward_token` | Collectible achievement reward |

### Valid IDs

Definition IDs use:

```text
a-z
0-9
_
-
```

Length: **1–48 characters**.

Example:

```text
frost_potion
```

Use the same ID when referencing the item in recipes, rewards, and permissions.

### Item definition settings

| Field | Purpose |
|---|---|
| `enabled` | Enables creation and configured action handling |
| `public` | Controls default use/place permissions |
| `material` | Minecraft material |
| `name` | Display name |
| `lore` | Description lines |
| `unbreakable` | Prevents normal durability loss |
| `model` | CustomModelData integer where supported |
| `color` | Potion color in hexadecimal RGB |
| `effects` | Custom potion effects |
| `cooldown-seconds` | Delay between configured action activations |
| `kind` | Special behavior marker, such as `spear` |
| `actions` | Click and target actions |
| `furnace` | Custom furnace settings |

`model` selects metadata for a compatible resource pack. It does not generate textures or models.

### Example: a custom potion

Merge this definition under the existing `items:` section in `items.yml`:

```yaml
items:
  frost_potion:
    enabled: true
    public: true
    material: POTION
    name: "&bFrost Potion"

    lore:
      - "&7A cold defensive mixture."
      - "&7Slowness II and Resistance I for 30 seconds."

    color: "66CCFF"

    effects:
      - "SLOWNESS:30:2"
      - "RESISTANCE:30:1"
```

Reload and give the potion:

```text
/customitem reload
/customitem give frost_potion 1
```

Give it to another player:

```text
/customitem give frost_potion 1 Alex
```

### Potion effect syntax

```text
TYPE:seconds:level
```

Example:

```text
STRENGTH:45:3
```

This means **Strength III for 45 seconds**.

For potion definitions:

- Duration: 1–86,400 seconds.
- Level: 1–255.
- Effect availability depends on the server version.

The configured level is human-readable: `1` means level I.

Use `POTION`, `SPLASH_POTION`, or `LINGERING_POTION` when supported.

The custom-item drinking permission is checked when consuming an item. Do not assume the same check covers every vanilla splash or lingering-potion interaction.

### Create the same potion with commands

```text
/customitem create frost_potion POTION
/customitem name frost_potion &bFrost Potion
/customitem lore add frost_potion &7A cold defensive mixture.
/customitem effect add frost_potion SLOWNESS:30:2
/customitem effect add frost_potion RESISTANCE:30:1
/customitem color frost_potion 66CCFF
/customitem give frost_potion 1
```

New command-created definitions default to:

```yaml
public: false
```

To make the potion available to ordinary players, either set `public: true` in YAML and reload, or grant:

```text
vaulteconomy.item.use.frost_potion
```

Item-giving permission is separate.

---

## 🪄 Custom Item Actions

Three action triggers are implemented:

| Trigger | Activation |
|---|---|
| `right_click` | Right-click in the air or on a block |
| `left_click` | Left-click in the air or on a block |
| `target` | Right-click another player |

The target trigger is specifically for **players**, not an arbitrary mob-targeting system.

### Available actions

| Action | Behavior |
|---|---|
| `message <text>` | Sends a message to the item user |
| `effect TYPE:seconds:level` | Applies a potion effect to the user |
| `command <command>` | Runs a command as the user, with their permissions |
| `console <command>` | Runs a server-owner-defined command as console |
| `jail <cell>:<seconds>` | Jails the targeted player |
| `sound <SOUND>` | Plays a sound |
| `animation <style>` | Plays a configured particle animation |

Command actions support:

```text
{player}
{target}
```

Write command text without a leading `/`.

These substitutions apply to command actions; they should not be assumed to work in every arbitrary text field.

### Example: reusable speed wand

```yaml
items:
  wind_wand:
    enabled: true
    public: true
    material: BLAZE_ROD
    name: "&bWind Wand"
    unbreakable: true
    cooldown-seconds: 15

    lore:
      - "&7Right-click to gain a short burst of speed."
      - "&8Cooldown: 15 seconds"

    actions:
      right_click:
        - "effect SPEED:8:2"
        - "sound ENTITY_EXPERIENCE_ORB_PICKUP"
        - "animation spiral"
        - "message &bThe wind carries you forward!"
```

```text
/customitem reload
/customitem give wind_wand 1
```

Configured action use does not automatically consume the item.

The action cooldown is shared per player and item ID. Runtime handling clamps it to **1–3,600 seconds**.

The `effect` action accepts **1–3,600 seconds** and levels **1–20**. These limits differ from the potion-definition limits.

### Example: player-targeting jail wand

```yaml
items:
  prison_wand:
    enabled: true
    public: false
    material: BLAZE_ROD
    name: "&5Prison Wand"
    cooldown-seconds: 5

    lore:
      - "&7Right-click a player to send them to jail."

    actions:
      target:
        - "jail default:60"
        - "sound BLOCK_ANVIL_LAND"
```

Create the cell and obtain the wand:

```text
/jail set default
/customitem give prison_wand 1
```

Using its target action requires:

```text
vaulteconomy.item.use.prison_wand
vaulteconomy.item.target.prison_wand
```

### Item identification

Custom items use an **HMAC-signed lore marker**. The marker identifies plugin content and is verified against a secret stored in the selected backend.

Renaming an ordinary item does not create a valid custom item.

Preserve the signing key and item marker. Losing the key makes previously signed items and fish fail verification.

The signature is not a universal anti-duplication system for every item. Fish additionally use unique serial numbers for sale tracking.

### Updating existing items

Changing a definition affects future item creation and action lookup. Existing display names, lore, and potion metadata are not automatically rebuilt in every inventory.

Use `/itemedit` to change a held item, or issue a newly generated item after changing a definition.

---

## 🧰 Custom Item Management Commands

All commands below are administrative by default.

```text
/customitem list
/customitem inspect <id>
/customitem create <id> <material>
/customitem clone <id> <new-id>
/customitem delete <id> confirm
/customitem give <id> [amount] [player]
/customitem menu
/customitem reload

/customitem material <id> <material>
/customitem name <id> <text>
/customitem enabled <id> <true|false>
/customitem unbreakable <id> <true|false>
/customitem model <id> <integer>
/customitem color <id> <RRGGBB>
/customitem cooldown-seconds <id> <seconds>
/customitem kind <id> <value>

/customitem lore add <id> <text>
/customitem lore clear <id>
/customitem effect add <id> <TYPE:seconds:level>
/customitem effect clear <id>
/customitem action add <id> <right_click|left_click|target> <action>
/customitem action clear <id> <trigger>

/customitem furnace speed <id> <value>
/customitem furnace fuel-multiplier <id> <value>
/customitem furnace output-multiplier <id> <value>
```

Give amounts must also fit the material’s maximum stack size. A potion cannot be issued as a normal 64-item stack through this command.

---

## 🔨 Custom Crafting and Smelting

Recipes are stored in `recipes.yml`.

Supported types:

```text
shaped
shapeless
furnace
```

### Ingredient tokens

```text
IRON_INGOT
POTION
custom:frost_potion
```

A plain material token matches an ordinary item of that material.

A `custom:<id>` token requires the corresponding signed custom item.

Signed custom items are not accepted as ordinary vanilla crafting ingredients merely because their material matches.

### Shaped recipe example

First create `frost_potion` in `items.yml`. Then add:

```yaml
recipes:
  frost_potion_craft:
    enabled: true
    public: true
    type: shaped
    result: frost_potion
    amount: 1

    shape:
      - " I "
      - "IPI"
      - " I "

    ingredients:
      I: ICE
      P: POTION
```

Spaces represent empty slots.

The `result` field contains the **custom item ID**, without the `custom:` prefix.

### Shapeless recipe example

```yaml
recipes:
  ocean_elixir_upgrade:
    enabled: true
    public: true
    type: shapeless
    result: ocean_elixir
    amount: 1

    ingredients-list:
      - "custom:frost_potion"
      - HEART_OF_THE_SEA
```

### Furnace recipe example

This example uses the included `reward_token` item:

```yaml
recipes:
  token_smelting:
    enabled: true
    public: true
    type: furnace
    input: GOLD_NUGGET
    result: reward_token
    amount: 1
    experience: 0.5
    ticks: 200
```

At 20 server ticks per second, `200` ticks is approximately 10 seconds.

Custom furnace recipes require an ordinary material input. `custom:<id>` inputs are supported for crafting and brewing, not for these furnace recipes.

Automatic smelting has no acting player, so do not rely on `public` or a per-player crafting permission to restrict every furnace cycle.

### Create a recipe through commands

```text
/recipe create frost_potion_craft shaped frost_potion
/recipe shape frost_potion_craft _I_/IPI/_I_
/recipe ingredient frost_potion_craft I ICE
/recipe ingredient frost_potion_craft P POTION
/recipe enabled frost_potion_craft true
```

In the shape command:

- `/` separates rows.
- `_` represents an empty slot.

New recipes are initially disabled so that the shape and ingredients can be completed first.

New definitions are also private by default. Set `public: true` in YAML or grant:

```text
vaulteconomy.recipe.craft.frost_potion_craft
```

### Recipe commands

```text
/recipe list
/recipe inspect <id>
/recipe clone <id> <new-id>
/recipe delete <id> confirm

/recipe create <id> <shaped|shapeless|furnace> <result-id>
/recipe shape <id> <ABC/DEF/GHI>
/recipe ingredient <id> <symbol> <MATERIAL|custom:id>
/recipe ingredients <id> <token,token,...>

/recipe enabled <id> <true|false>
/recipe result <id> <item-id>
/recipe amount <id> <amount>
/recipe input <id> <material>
/recipe experience <id> <value>
/recipe ticks <id> <ticks>
/recipe reload
```

### Disable recipes by key or result

```text
/recipe block minecraft:diamond_sword
/recipe unblock minecraft:diamond_sword

/recipe blockresult DIAMOND_SWORD
/recipe unblockresult DIAMOND_SWORD
```

Equivalent configuration:

```yaml
disabled-keys:
  - minecraft:diamond_sword

disabled-results:
  - DIAMOND_SWORD
```

Blocking applies to recipes available through the Bukkit recipe registry. If another plugin registers a recipe later, reapply the recipe reload.

Vanilla brewing is separate from the normal keyed crafting registry.

### Command-only rewards

To make an item obtainable only through commands or rewards, do not create a recipe for it, or disable every recipe that produces it.

---

## ⚗️ Custom Brewing

Custom brewing recipes belong in `brewing.yml`.

### Example

```yaml
recipes:
  frost_brew:
    enabled: true
    public: true
    base: POTION
    ingredient: ICE
    result: frost_potion
    ticks: 200
    fuel: true
```

Both the base and ingredient can use exact custom-item tokens where the brewing inventory supports the relevant item:

```yaml
base: "custom:frost_potion"
ingredient: HEART_OF_THE_SEA
```

### How to brew

1. Put matching base items in the brewing stand’s bottle slots.
2. Add blaze powder when the recipe requires fuel.
3. Place a supported ingredient in its slot.
4. For an ingredient the vanilla interface will not accept, hold it in your main hand.
5. Sneak and right-click the brewing stand, or look at it and run:

```text
/brew start
```

6. Leave the inventory unchanged until the process finishes.

Changing the inventory cancels the pending result. A started fuel-consuming cycle has already spent its fuel unit.

One blaze powder supplies **20 cycles**.

### Commands

```text
/brew create <id> <base> <ingredient> <result-id>
/brew list
/brew inspect <id>
/brew clone <id> <new-id>
/brew delete <id> confirm

/brew enabled <id> <true|false>
/brew base <id> <token>
/brew ingredient <id> <token>
/brew result <id> <item-id>
/brew ticks <id> <ticks>
/brew fuel <id> <true|false>
/brew start
```

Recipe access:

```text
vaulteconomy.recipe.brew.<id>
```

`/brew start` additionally requires the command permissions:

```text
vaulteconomy.brew
vaulteconomy.brew.start
```

The sneak-right-click interaction uses the recipe permission directly.

---

## 🔥 Custom Furnaces

A custom furnace is defined as an item with a `furnace` section:

```yaml
items:
  swift_furnace:
    enabled: true
    public: true
    material: FURNACE
    name: "&6Swift Furnace"

    lore:
      - "&7Processes recipes faster where supported."
      - "&7Fuel lasts twice as long."

    furnace:
      speed: 2
      fuel-multiplier: 2
      output-multiplier: 1
```

| Setting | Meaning |
|---|---|
| `speed` | Divides cooking time by this factor where the required API exists |
| `fuel-multiplier` | Multiplies fuel burn duration |
| `output-multiplier` | Multiplies produced quantity, subject to stack and output-space limits |

A fuel multiplier of `2` makes fuel last longer; it does not double fuel consumption.

The plugin records placed custom furnaces. Normal supported block drops can return their custom identity.

Explosions remove the stored block record, but do not guarantee recovery of the custom furnace item.

Placement permission:

```text
vaulteconomy.item.place.swift_furnace
```

---

## 🎣 Interactive Fishing

VaultEconomy adds a timing minigame when a qualifying vanilla catch occurs.

### How it works

- Watch the moving indicator.
- Right-click or press the swap-hand key, normally **F**, while the indicator is inside the target area.
- Reach the required number of hits to land the fish.
- Misses and missed windows count toward failure.
- Moving more than eight blocks from the starting position ends the attempt.

Default difficulty:

```yaml
game:
  hits: 5
  misses: 3
  period-ticks: 40
  window: 0.28
```

### Included species

| ID | Display name | Default rarity | Selection weight |
|---|---|---|---:|
| `river_carp` | River Carp | Common | 60 |
| `silver_salmon` | Silver Salmon | Uncommon | 25 |
| `moon_ray` | Moon Ray | Rare | 10 |
| `abyss_puffer` | Abyss Puffer | Epic | 4 |
| `golden_leviathan` | Golden Leviathan | Legendary | 1 |

`chance` is a **relative selection weight**, not an independent percentage.

For eligible species, selection probability is:

```text
species weight / total weight of eligible species
```

### Species configuration

```yaml
species:
  crystal_carp:
    enabled: true
    name: "&bCrystal Carp"
    material: COD
    rarity: rare
    chance: 5
    price: 20

    weight:
      min: 0.5
      max: 4.0

    length:
      min: 20
      max: 80

    worlds:
      - world

    biomes:
      - RIVER

    rain-only: false
```

| Field | Meaning |
|---|---|
| `material` | Item appearance |
| `rarity` | Rarity ID used by pricing |
| `chance` | Relative selection weight |
| `price` | Base price |
| `weight` | Random weight range in kilograms |
| `length` | Random length range in centimeters |
| `worlds` | Allowed world names; empty list means unrestricted |
| `biomes` | Allowed biome names; empty list means unrestricted |
| `rain-only` | Requires rain |

### Player commands

```text
/fish menu
/fish list
/fish inspect <species>
/fish stats
/fish price
/fish sell
/fish sellall
/fish cancel
/fish animation <style>
```

`/fish price` quotes the fish held in the main hand.

### Fish buyer

The buyer is a command and menu system. It does not spawn an NPC.

Require players to sell near a specific location:

```text
/fish buyer set
/fish buyer require-location true
```

When location enforcement is enabled, the player must be within six blocks of the saved buyer location.

A separate NPC plugin can be configured to open `/fish menu`.

### Pricing

```text
base price
× weight in kg
× (1 + length in cm / 100)
× rarity multiplier
× (1 + chance factor / sqrt(selection weight))
× buyer multiplier
```

Buyer settings:

```yaml
buyer:
  enabled: true
  require-location: false
  multiplier: 1
  chance-factor: 0.05

  rarity:
    common: 1
    uncommon: 1.4
    rare: 2
    epic: 3
    legendary: 5
```

Fish sales create money in the economy. Adjust these values to suit the server’s intended income rate.

### Sale tracking

Each generated fish receives signed data and a unique serial number.

Payment and the record marking that serial as sold are committed together. A copied fish with an already-sold serial cannot receive another payout.

Sold-serial records are retained, so storage usage grows over time.

### Administrative commands

```text
/fish practice
/fish give <species> [player]
/fish reload

/fish buyer set
/fish buyer remove
/fish buyer enabled <true|false>
/fish buyer multiplier <value>
/fish buyer require-location <true|false>
/fish buyer chance-factor <value>

/fish difficulty hits <value>
/fish difficulty misses <value>
/fish difficulty period-ticks <value>
/fish difficulty window <value>
```

`/fish practice` starts the implemented fishing game. It should not be treated as a guaranteed reward-free simulation.

### Species editor

```text
/fishspecies create <id>
/fishspecies list
/fishspecies inspect <id>
/fishspecies clone <id> <new-id>
/fishspecies delete <id> confirm

/fishspecies enabled <id> <true|false>
/fishspecies material <id> <material>
/fishspecies name <id> <text>
/fishspecies rarity <id> <rarity>
/fishspecies chance <id> <weight>
/fishspecies price <id> <amount>

/fishspecies weight min <id> <value>
/fishspecies weight max <id> <value>
/fishspecies length min <id> <value>
/fishspecies length max <id> <value>

/fishspecies worlds <id> <comma-separated-values>
/fishspecies biomes <id> <comma-separated-values>
/fishspecies rain-only <id> <true|false>
```

Use `clear` for list-setting commands to remove their restrictions.

---

## ✨ Animations and Charged Spears

### Particle animation patterns

```text
ring
spiral
helix
double_helix
wave
orbit
fountain
vortex
star
pulse
figure_eight
rain
comet
crown
ripple
```

Examples:

```text
/fish animation double_helix
/fx preview crown
/fx styles
```

### Visual settings

```yaml
particles: true
sounds: true
points-per-frame: 12

spear:
  charge-ticks: 60
  screen-darkening: true
  max-speed-bonus: 1
  max-damage-bonus: 8

styles:
  spiral:
    particle: FLAME
  ripple:
    particle: CLOUD
```

Edit `effects.yml`, then run:

```text
/vex reload
```

A lower `points-per-frame` value reduces particles generated per animation frame.

### Charged spear

```text
/spear give
/spear styles
/spear style storm
```

To use it:

1. Hold the custom spear.
2. Sneak to charge.
3. Release sneak.
4. Throw the trident within three seconds.

Charge affects projectile speed and damage within the configured limits.

Available spear styles:

```text
ember
storm
void
tidal
solar
```

Permissions:

```text
vaulteconomy.spear.use
vaulteconomy.item.use.storm_spear
vaulteconomy.spear.style.<style>
```

The optional screen-darkening effect uses Darkness where available and short Blindness effects as a fallback. It is not a custom client shader.

### Effect commands

```text
/fx preview <style>
/fx sound <SOUND> [volume] [pitch]
/fx sounds [filter]
/fx particles [filter]
/fx styles
/fx title <title|subtitle>
/fx lightning
/fx firework
/fx stop-sound <SOUND>
/fx clear-title

/fx setting particles <true|false>
/fx setting sounds <true|false>
/fx setting points-per-frame <value>
/fx setting spear screen-darkening <true|false>
/fx setting spear charge-ticks <value>
/fx setting spear max-speed-bonus <value>
/fx setting spear max-damage-bonus <value>
```

Particle display permission:

```text
vaulteconomy.effects.view
```

---

## 🏆 Achievements

Achievements are configured in `achievements.yml`.

### Supported triggers

| Trigger | Tracks |
|---|---|
| `block_break` | Matching blocks broken |
| `block_place` | Matching blocks placed |
| `kill` | Matching entity kills |
| `death` | Player deaths |
| `craft` | Crafting actions |
| `join` | Player joins |
| `vanilla_advancement` | Newly completed vanilla advancements |
| `fish_catch` | Custom fish caught |
| `fish_sell` | Custom fish sold |
| `item_use` | Configured custom-item actions |
| `brew` | Custom brewing results |
| `spear_throw` | Charged spear throws |

### Included achievements

- First Catch.
- Angler.
- Fish Merchant.
- Miner.
- Hunter.
- Alchemist.
- Spear Master.
- Diamonds.

### Example

```yaml
enabled: true

achievements:
  stone_collector:
    enabled: true
    public: true
    name: "&6Stone Collector"
    icon: STONE
    trigger: block_break
    match: STONE
    goal: 500
    auto-claim: false

    rewards:
      money: 100
      items:
        - "reward_token:2"
      commands: []
```

### Field reference

| Field | Meaning |
|---|---|
| `enabled` | Enables the definition |
| `public` | Controls default earn/claim permissions |
| `name` | Display name |
| `icon` | GUI material |
| `trigger` | Event to track |
| `match` | Exact event value, or `*` |
| `goal` | Required progress |
| `auto-claim` | Attempts reward collection when the goal is reached |
| `rewards.money` | Main-currency reward |
| `rewards.items` | Custom item IDs with amounts |
| `rewards.commands` | Console commands |

Reward commands support:

```text
{player}
{uuid}
```

Example:

```yaml
commands:
  - "say {player} completed Stone Collector!"
```

### Player commands

```text
/achievements
/achievements menu
/achievements list
/achievements inspect <id>
/achievements progress <id>
/achievements rewards <id>
/achievements claim <id>
/achievements claimall
```

Custom item rewards enter the delivery queue:

```text
/claim
```

### Administrative commands

```text
/achievementedit create <id> <trigger> <goal>
/achievementedit list
/achievementedit inspect <id>
/achievementedit clone <id> <new-id>
/achievementedit delete <id> confirm

/achievementedit enabled <id> <true|false>
/achievementedit name <id> <text>
/achievementedit icon <id> <material>
/achievementedit trigger <id> <trigger>
/achievementedit match <id> <value>
/achievementedit goal <id> <number>
/achievementedit auto-claim <id> <true|false>

/achievementedit rewards money <id> <amount>
/achievementedit rewards items <id> <comma-separated-values>
/achievementedit rewards commands <id> <comma-separated-values>

/achievementedit grant <id> <player>
/achievementedit reset <id> <player> confirm
/achievementedit pending <player>
/achievementedit reload
```

For complex command rewards, edit the YAML list directly.

### Reward behavior

Money, the claimed marker, and queued item rewards are recorded in one transaction.

External console commands cannot share that transaction with another plugin. Ambiguous command rewards are recorded for administrative inspection instead of being automatically repeated.

Resetting an achievement allows its reward to be earned again.

### Progress details

- Creative-mode block breaking and placement do not count.
- Crafting counts actions, not every produced item in a shift-click stack.
- Previously completed vanilla advancements are not automatically imported.
- Filters and reward values should match the intended progression rules.

---

## 🛠️ Edit the Item in Your Hand

`/itemedit` changes the currently held item.

It is separate from `/customitem`, which edits reusable definitions.

| Commands | Purpose |
|---|---|
| `name`, `name-clear` | Set or remove a display name |
| `lore-add`, `lore-set`, `lore-remove`, `lore-clear` | Edit description lines |
| `amount`, `material` | Change stack size or material |
| `unbreakable`, `repair`, `damage` | Edit durability behavior |
| `model`, `model-clear` | Edit CustomModelData |
| `enchant`, `unenchant`, `enchants-clear` | Edit applied enchantments |
| `flag-add`, `flag-remove`, `flags-clear` | Edit item flags |
| `color` | Change potion color |
| `potion-add`, `potion-remove`, `potion-clear` | Edit custom potion effects |
| `attribute-add`, `attribute-remove`, `attributes-clear` | Edit attribute modifiers |
| `clone`, `info` | Copy or inspect the item |
| `book-title`, `book-author`, `book-page`, `book-add`, `book-clear` | Edit books |
| `leather-color` | Dye leather armor |
| `skull` | Set a player head to an online player |

### Examples

```text
/itemedit name &6Explorer's Sword
/itemedit lore-add &7A reward for a long journey.
/itemedit lore-set 1 &7A treasured expedition reward.
/itemedit lore-remove 1
/itemedit unbreakable true

/itemedit enchant sharpness 5
/itemedit unenchant sharpness
/itemedit enchants-clear

/itemedit potion-add SPEED 30 2
/itemedit potion-remove SPEED
/itemedit potion-clear

/itemedit attribute-add attack_damage 3 ADD_NUMBER
/itemedit attribute-remove attack_damage
/itemedit attributes-clear

/itemedit model 1001
/itemedit model-clear

/itemedit leather-color 3399FF
/itemedit skull Alex
/itemedit info
```

Attribute operations:

```text
ADD_NUMBER
ADD_SCALAR
MULTIPLY_SCALAR_1
```

Lore line numbers start at **1**.

`potion-remove` and `potion-clear` operate on custom potion effects. They do not provide a general rewrite of every vanilla base-potion property.

`unenchant` removes an applied enchantment from item metadata; it should not be advertised as a universal editor for another plugin’s custom enchantment storage.

The editor preserves valid VaultEconomy signed identity when updating an item.

---

## 👤 Player Controls

`/playerctl` provides administrative controls for player state, movement, effects, and appearance.

Most operations accept an optional final `[player]` argument. Omitting it targets yourself when supported.

### Health and survival

```text
/playerctl health <value> [player]
/playerctl maxhealth <value> [player]
/playerctl food <value> [player]
/playerctl saturation <value> [player]
/playerctl exhaustion <value> [player]
/playerctl absorption <value> [player]
/playerctl air <value> [player]
/playerctl fireticks <value> [player]
/playerctl heal [player]
/playerctl feed [player]
/playerctl extinguish [player]
```

### Movement and state

```text
/playerctl walkspeed <value> [player]
/playerctl flyspeed <value> [player]
/playerctl sneakspeed <value> [player]
/playerctl sprintspeed <value> [player]
/playerctl speedreset [player]

/playerctl gravity <true|false> [player]
/playerctl collidable <true|false> [player]
/playerctl glowing <true|false> [player]
/playerctl invulnerable <true|false> [player]
/playerctl silent <true|false> [player]
/playerctl pickup <true|false> [player]
/playerctl fly <true|false> [player]
/playerctl flying <true|false> [player]

/playerctl freeze [player]
/playerctl unfreeze [player]
/playerctl top [player]
/playerctl below [player]
/playerctl jump <level> [player]
/playerctl fallreset [player]
/playerctl velocity <x> <y> <z> [player]
/playerctl knockback <value> [player]

/playerctl sneak <true|false> [player]
/playerctl sprint <true|false> [player]
/playerctl compass <x> <y> <z> [player]
/playerctl bed [player]
/playerctl wake [player]
```

`bed` sets the player’s bed spawn to their current position.

### Appearance, information, and experience

```text
/playerctl nametag <true|false> [player]
/playerctl displayname <text_without_spaces> [player]
/playerctl listname <text_without_spaces> [player]
/playerctl name-reset [player]

/playerctl disguise <mob> [player]
/playerctl undisguise [player]

/playerctl title <title|subtitle> [player]
/playerctl subtitle <text_without_spaces> [player]
/playerctl title-clear [player]

/playerctl level <value> [player]
/playerctl exp <0..1> [player]
/playerctl reset [player]
/playerctl info [player]
```

The glow command uses the vanilla glowing state. It is not a separate configurable per-viewer outline-color system.

Name-tag hiding uses scoreboard teams and can interact with another plugin’s scoreboard configuration.

### Potion effects

```text
/playerctl effect <effect> <seconds> <level> [player]
/playerctl cure <effect> [player]
/playerctl effects-clear [player]
```

Registered effect paths:

```text
speed, slowness, haste, mining_fatigue, strength,
instant_health, instant_damage, jump_boost, nausea,
regeneration, resistance, fire_resistance, water_breathing,
invisibility, blindness, night_vision, hunger, weakness,
poison, wither, health_boost, absorption, saturation,
glowing, levitation, luck, unluck, slow_falling,
conduit_power, dolphins_grace, darkness
```

Example:

```text
/playerctl effect glowing 30 1 Alex
/playerctl cure glowing Alex
```

### Attributes

```text
/playerctl attribute <attribute> <value> [player]
/playerctl attribute-reset <attribute> [player]
```

Registered attribute paths:

```text
max_health
movement_speed
attack_damage
attack_speed
armor
armor_toughness
knockback_resistance
luck
flying_speed
gravity
scale
step_height
jump_strength
safe_fall_distance
fall_damage_multiplier
block_interaction_range
entity_interaction_range
sneaking_speed
submerged_mining_speed
block_break_speed
mining_efficiency
water_movement_efficiency
movement_efficiency
oxygen_bonus
sweeping_damage_ratio
burning_time
explosion_knockback_resistance
```

These commands require the corresponding attribute to exist in the running server API.

---

## 🔒 Jail

Create jail cells at your current position:

```text
/jail set default
```

Manage imprisonment:

```text
/jail send Alex default 60
/jail info Alex
/jail release Alex
/jail list
/jail delete default confirm
```

Jail records preserve the sentence and previous location. The sentence continues to expire while the player is offline.

Jailed players have movement, command, and interaction restrictions.

Configuration in `extensions.yml`:

```yaml
jail:
  radius: 5
  allowed-commands:
    - msg
    - reply
    - ahelp
    - balance

command-cooldown-ms: 150
```

Release prisoners before deleting their cell.

---

## 💬 Social Features and Server Utilities

### Communication

- Private messages and replies.
- Ignore list.
- Offline mail.
- Broadcasts.
- Staff and administrator chat.
- Chat clearing.
- Prefixes, nicknames, and colors.
- Optional chat formatting.

Chat formatting is disabled by default:

```yaml
chat:
  enabled: false
```

### Marriage

```text
/marry info
/marry propose <player>
/marry accept
/marry deny
/marry divorce confirm
/marry chat <message>
```

### Kits

```text
/createkit starter 3600
/kit starter
/kits
/delkit starter
```

Kits save the main inventory contents. Armor and off-hand contents are not included.

Access requires both:

```text
vaulteconomy.kit
vaulteconomy.kit.starter
```

Kit cooldowns are stored persistently.

### Inventory utilities

- Virtual crafting table.
- Virtual anvil where supported.
- Ender chest access.
- Read-only inspection of other players’ inventories.
- Personal 54-slot safe.
- Item repair and editing.
- Queued item delivery.

`/invsee` includes the main inventory, armor, and off-hand slots.

Other players’ inventories and ender chests are inspected without editing in this release.

---

## ⌨️ Core Command Reference

Every core root command uses:

```text
vaulteconomy.<root-command>
```

For example:

```text
/balance → vaulteconomy.balance
/sethome → vaulteconomy.sethome
/repair → vaulteconomy.repair
```

Aliases use the permission of the original command.

**Player** means available by default to ordinary players.  
**OP** means operator-only by default.  
Additional subcommand, target, content, and role checks still apply.

<details>
<summary><strong>Economy, menus, and plugin management</strong></summary>

| Command | Purpose | Default |
|---|---|---|
| `/ve help` | Core help | Player |
| `/ve status` | Storage and runtime status | OP |
| `/ve reload` | Reload core configuration | OP |
| `/ve backup` | Export plugin records | OP |
| `/ve audit [count]` | Recent monetary operations | OP |
| `/menu` | Main menu | Player |
| `/balance [player]` | View a balance | Player |
| `/eco give/take/set <player> <amount>` | Manage money | OP |
| `/pay <player> <amount>` | Transfer money | Player |
| `/baltop [currency]` | Balance leaderboard | Player |
| `/point list` | List custom currencies | Player |
| `/point balance <id> [player]` | Read custom currency balance | Player |
| `/point pay <id> <player> <amount>` | Transfer custom currency | Player |
| `/point create <id> [symbol]` | Create a currency | OP |
| `/point give/take/set <id> <player> <amount>` | Manage custom currency | OP |

The `ve` and `point` roots also require their respective subcommand permissions.

</details>

<details>
<summary><strong>Teleportation</strong></summary>

| Command | Purpose | Default |
|---|---|---|
| `/spawn` | Teleport to spawn | Player |
| `/setspawn` | Set spawn | OP |
| `/sethome [name]` | Save a home | Player |
| `/home [name]` | Visit a home | Player |
| `/delhome [name]` | Delete a home | Player |
| `/homes` | Home menu | Player |
| `/setwarp <name>` | Create a warp | OP |
| `/delwarp <name>` | Delete a warp | OP |
| `/warp <name>` | Visit a warp | Player |
| `/warps` | Warp menu | Player |
| `/back` | Return to a saved previous location | Player |
| `/tpa <player>` | Request teleportation to a player | Player |
| `/tpahere <player>` | Request that a player teleport to you | Player |
| `/tpaccept` | Accept a request | Player |
| `/tpdeny` | Reject a request | Player |
| `/tptoggle` | Toggle incoming requests | Player |
| `/tpcancel` | Cancel requests and active countdown | Player |
| `/tp <player>` | Direct teleport | OP |
| `/tphere <player>` | Bring a player to you | OP |
| `/tppos <x> <y> <z>` | Teleport to coordinates | OP |
| `/rtp` | Random teleport | OP |

</details>

<details>
<summary><strong>Player and inventory utilities</strong></summary>

| Command | Purpose | Default |
|---|---|---|
| `/heal [player]` | Restore health | OP |
| `/feed [player]` | Restore food | OP |
| `/fly [player]` | Toggle flight access | OP |
| `/god [player]` | Toggle god mode | OP |
| `/gamemode <mode> [player]` | Change game mode | OP |
| `/speed <0–10> [player]` | Set walking or flying speed | OP |
| `/vanish` | Toggle the plugin’s vanish state | OP |
| `/afk` | Toggle AFK status | Player |
| `/craft` | Open a crafting table | OP |
| `/anvil` | Open a virtual anvil where supported | OP |
| `/enderchest [player]` | Open or inspect an ender chest | OP |
| `/invsee <player>` | Inspect another inventory | OP |
| `/sejf` | Open a personal safe | OP |
| `/claim` | Collect the next queued delivery | Player |
| `/repair [all]` | Repair held or multiple items | OP |
| `/itemname <text>` | Rename the held item | OP |
| `/itemlore add <text>` | Add held-item lore | OP |
| `/itemlore clear` | Clear held-item lore | OP |
| `/enchant <id> <0–255>` | Apply an enchantment; level 0 removes it | OP |
| `/clearinventory [player]` | Clear inventory | OP |
| `/hat` | Wear one held item as a hat | OP |
| `/more` | Fill the held stack to its maximum | OP |
| `/give <player> <material> [amount]` | Give ordinary items | OP |
| `/spawnmob <type> [amount]` | Spawn entities | OP |
| `/near [radius]` | Find nearby players | OP |
| `/suicide` | Kill your own player | OP |
| `/ext [player]` | Extinguish fire | OP |
| `/exp <levels> [player]` | Set experience level | OP |
| `/ptime day/night/reset` | Personal time | OP |
| `/pweather clear/rain/reset` | Personal weather | OP |
| `/time day/night/<ticks>` | Current-world time | OP |
| `/weather clear/rain/thunder` | Current-world weather | OP |

`/exp` sets a level rather than adding that many levels.

</details>

<details>
<summary><strong>Chat, moderation, kits, and information</strong></summary>

| Command | Purpose | Default |
|---|---|---|
| `/msg <player> <message>` | Private message | Player |
| `/reply <message>` | Reply to a private message | Player |
| `/ignore <player>` | Toggle ignoring a player | Player |
| `/broadcast <message>` | Server announcement | OP |
| `/staffchat <message>` | Staff chat | OP |
| `/adminchat <message>` | Administrator chat | OP |
| `/clearchat` | Clear online players’ chat | OP |
| `/prefix [set] <text>` | Set your chat prefix | OP |
| `/prefix reset` | Remove your custom prefix | OP |
| `/nick <name/off>` | Set or reset a nickname | OP |
| `/color [code]` | Choose a chat color | OP |
| `/kit <name>` | Claim a kit | OP |
| `/kits` | Browse kits | Player |
| `/createkit <name> [cooldown-seconds]` | Save a kit | OP |
| `/delkit <name>` | Delete a kit | OP |
| `/mail read` | Read stored mail | Player |
| `/mail clear` | Clear stored mail | Player |
| `/mail send <player> <message>` | Send mail | Player |
| `/rules` | Show configured rules | Player |
| `/motd` | Show configured welcome text | Player |
| `/list` | Online list respecting the plugin’s vanish | Player |
| `/ping` | Display ping where supported | Player |
| `/seen <player>` | Last recorded login | Player |
| `/playtime` | Stored playtime, updated on logout | Player |
| `/kick <player> [reason]` | Kick a player | OP |
| `/ban <player> [reason]` | Ban a player | OP |
| `/tempban <player> <seconds> [reason]` | Temporary ban | OP |
| `/unban <player>` | Remove a ban | OP |
| `/mute <player> [reason]` | Mute a player | OP |
| `/tempmute <player> <seconds> [reason]` | Temporary mute | OP |
| `/unmute <player>` | Remove a mute | OP |
| `/filter list` | Show filtered words | OP |
| `/filter add <word>` | Add a filtered word | OP |
| `/filter remove <word>` | Remove a filtered word | OP |
| `/clan ...` | Clan systems | Player |
| `/marry ...` | Marriage systems | Player |
| `/ah ...` | Auction house | Player |
| `/ahelp` | Auction help | Player |
| `/kastom [3/4]` | Legacy Strength potion | OP |
| `/kostoms [1/2/3]` | Legacy custom furnace | OP |

</details>

### Common aliases

```text
/menu        → /vemenu
/balance     → /bits, /bal, /money
/eco         → /economy
/baltop      → /topmoney
/homes       → /homelist
/warps       → /warplist
/tpahere     → /tph
/tpaccept    → /tpaaccept, /tpyes
/tpdeny      → /tpadeny, /tpno
/gamemode    → /gm, /gmc, /gms, /gma, /gmsp
/craft       → /workbench, /wb
/enderchest  → /ec, /echest
/sejf        → /safe
/repair      → /fix
/clearinventory → /ci
/spawnmob    → /sp
/msg         → /tell, /w
/reply       → /r
/clearchat   → /cc
/clan        → /cl, /c
/ah          → /auction, /market, /auc, /ac
/ahelp       → /ahhelp, /auctionhelp
```

---

## 🔑 Permissions Explained

VaultEconomy uses its own permission namespace:

```text
vaulteconomy.*
```

It does not use `essentials.*` permissions.

### Extension command permissions

An extension command needs its root permission and its command-path permission.

For example:

```text
/customitem action add ...
```

requires:

```text
vaulteconomy.customitem
vaulteconomy.customitem.action.add
```

Another example:

```text
/playerctl effect glowing 30 1 Alex
```

uses:

```text
vaulteconomy.playerctl
vaulteconomy.playerctl.effect.glowing
vaulteconomy.playerctl.effect.glowing.others
```

Literal command-path words become permission segments. Argument values such as an item ID or player name are not automatically appended to the command permission.

Definition permissions are checked separately.

### Extension roots

| Root | Purpose | Default |
|---|---|---|
| `customitem` | Custom-item definitions and giving | OP |
| `recipe` | Crafting and smelting definitions | OP |
| `brew` | Brewing definitions and command activation | OP |
| `fish` | Fishing commands | Player; administrative paths remain OP |
| `fishspecies` | Species editor | OP |
| `achievements` | Achievement browsing and claiming | Player |
| `achievementedit` | Achievement administration | OP |
| `playerctl` | Player controls | OP |
| `itemedit` | Held-item editor | OP |
| `fx` | Visual-effect tools | OP |
| `spear` | Spear controls | Player; giving remains OP |
| `jail` | Jail administration | OP |
| `language` | Personal language selection | Player; reload remains OP |
| `vex` | Extension menu/help | Player; administration remains OP |

### Content permissions

| Permission | Controls |
|---|---|
| `vaulteconomy.item.use.<id>` | Supported use of a custom item |
| `vaulteconomy.item.give.<id>` | Administrative giving of that item |
| `vaulteconomy.item.place.<id>` | Placing the custom item as a block |
| `vaulteconomy.item.target.<id>` | Activating its player-target action |
| `vaulteconomy.recipe.craft.<id>` | Crafting that recipe |
| `vaulteconomy.recipe.brew.<id>` | Starting that custom brewing recipe |
| `vaulteconomy.achievement.earn.<id>` | Earning progress |
| `vaulteconomy.achievement.claim.<id>` | Claiming the reward |
| `vaulteconomy.kit.<name>` | Receiving that kit |

`public: true` changes the applicable content permission defaults.

It does not grant administrative editing, item giving, or target-action rights.

### Activity permissions

```text
vaulteconomy.fish.play
vaulteconomy.spear.use
vaulteconomy.effects.view
vaulteconomy.achievements.earn
```

### Additional core permissions

| Permission | Purpose |
|---|---|
| `vaulteconomy.balance.others` | View another player’s balance |
| `vaulteconomy.point.balance.others` | View another player’s custom-currency balance |
| `vaulteconomy.<command>.others` | Target others for supported administrative commands |
| `vaulteconomy.gamemode.<mode>` | Access a specific game mode |
| `vaulteconomy.repair.all` | Repair multiple items |
| `vaulteconomy.enchant.unsafe` | Bypass normal enchantment restrictions in the core enchant command |
| `vaulteconomy.vanish.see` | See players hidden by this plugin |
| `vaulteconomy.chat.colors` | Use supported chat color codes |
| `vaulteconomy.color.<code>` | Select a specific color |
| `vaulteconomy.filter.bypass` | Bypass the chat filter |
| `vaulteconomy.combat.bypass` | Bypass combat restrictions |
| `vaulteconomy.cooldown.bypass` | Bypass applicable core command cooldowns |
| `vaulteconomy.teleport.instant` | Bypass teleport delay |
| `vaulteconomy.kit.cooldown.bypass` | Bypass kit cooldowns |

Core `.others` permissions apply to:

```text
heal
feed
fly
god
gamemode
speed
enderchest
clearinventory
exp
ext
```

Sensitive subcommands have separate nodes, including:

```text
vaulteconomy.ve.<action>
vaulteconomy.eco.<action>
vaulteconomy.point.<action>
vaulteconomy.ah.<action>
vaulteconomy.clan.<action>
vaulteconomy.marry.<action>
vaulteconomy.mail.<action>
vaulteconomy.filter.<action>
```

### Numeric limit permissions

```text
vaulteconomy.limit.homes.10
vaulteconomy.limit.auction.15
vaulteconomy.limit.rtp.5000
vaulteconomy.limit.near.200
```

The plugin takes the highest applicable positive permission or configuration baseline, within its maximum:

| Limit | Maximum |
|---|---:|
| Homes | 100 |
| Auction listings | 100 |
| Nearby-player radius | 1,000 |
| RTP radius | 50,000 |

A numeric permission increases the baseline; it does not lower it. Reduce the configuration value to lower the default limit.

### LuckPerms examples

Allow a VIP group to fly and have more homes:

```text
/lp group vip permission set vaulteconomy.fly true
/lp group vip permission set vaulteconomy.limit.homes.10 true
```

Allow a moderator to freeze other players:

```text
/lp group moderator permission set vaulteconomy.playerctl true
/lp group moderator permission set vaulteconomy.playerctl.freeze true
/lp group moderator permission set vaulteconomy.playerctl.freeze.others true
/lp group moderator permission set vaulteconomy.playerctl.unfreeze true
/lp group moderator permission set vaulteconomy.playerctl.unfreeze.others true
```

Allow ordinary players to use and craft a private custom potion:

```text
/lp group default permission set vaulteconomy.item.use.frost_potion true
/lp group default permission set vaulteconomy.recipe.craft.frost_potion_craft true
```

Allow command-based brewing:

```text
/lp group default permission set vaulteconomy.brew true
/lp group default permission set vaulteconomy.brew.start true
/lp group default permission set vaulteconomy.recipe.brew.frost_brew true
```

### Inspect permissions

```text
/vex permissions
/vex permissions fish
/vex help
/vex help 2
```

The source distribution also includes:

```text
permissions.json
docs/COMMAND_CATALOG.md
docs/COMMANDS_RU.md
```

The command catalog lists the exact extension paths, permission nodes, and defaults.

Help and extension command-path suggestions are filtered by permissions. This does not imply that every possible argument has dynamic completion.

---

## 🌍 Languages

Included Bukkit language files:

```text
lang/en.yml
lang/ru.yml
```

Player commands:

```text
/language list
/language set en
/language set ru
```

### Add another language

1. Copy `lang/en.yml`.
2. Rename the copy, for example, to `lang/de.yml`.
3. Translate values while preserving keys.
4. Preserve substitution markers such as `{0}` and `{1}`.
5. Run:

```text
/language reload
/language set de
```

Missing translations fall back to English.

Technical identifiers, material names, effect names, and configuration inspection values remain technical identifiers.

Some older event-driven notifications use the server language. Command responses and the newer primary interfaces use the selected player language.

### Content translation

Edit names and lore in their content files:

```text
items.yml
fishing.yml
achievements.yml
```

A stored item name is not automatically translated for each viewer.

### Proxy translations

The proxy module uses:

```text
lang/en.properties
lang/ru.properties
```

These are UTF-8 properties files. Bukkit YAML language files are not loaded by the proxy.

---

## 💾 Eleven Storage Backends

Select one backend through `storage.type` in `config.yml`.

| Value | Backend | Storage location |
|---|---|---|
| `YAML` | YAML file | `data-v2/records.yaml` |
| `JSON` | JSON file | `data-v2/records.json` |
| `PROPERTIES` | Java Properties | `data-v2/records.properties` |
| `XML` | XML Properties | `data-v2/records.xml` |
| `CSV` | Key/value CSV | `data-v2/records.csv` |
| `SQLITE` | Embedded SQLite | `data-v2/records.db` |
| `H2` | Embedded H2 | `data-v2/records.mv.db` |
| `MYSQL` | MySQL | Remote database |
| `MARIADB` | MariaDB | Remote database |
| `POSTGRESQL` | PostgreSQL | Remote database |
| `SQLSERVER` | Microsoft SQL Server | Remote database |

These are **11 selectable backends**, not 11 simultaneous replicas.

SQLite is the default. SQLite and H2 do not require an external database server.

### What is stored?

The selected backend stores plugin-owned records, including:

- Balances and custom currencies.
- Player identity mappings.
- Clan and social records.
- Auction records and queued deliveries.
- Locations and other server-specific settings.
- Language choices.
- Fishing records.
- Achievement progress and reward state.
- Custom-item signing key.

Minecraft continues to manage its own worlds and player inventories.

### SQLite

```yaml
storage:
  type: SQLITE
```

### MySQL

```yaml
storage:
  type: MYSQL
  url: "jdbc:mysql://127.0.0.1:3306/minecraft"
  username: minecraft
  password: "CHANGE_ME"
```

### MariaDB

```yaml
storage:
  type: MARIADB
  url: "jdbc:mariadb://127.0.0.1:3306/minecraft"
  username: minecraft
  password: "CHANGE_ME"
```

### PostgreSQL

```yaml
storage:
  type: POSTGRESQL
  url: "jdbc:postgresql://127.0.0.1:5432/minecraft"
  username: minecraft
  password: "CHANGE_ME"
```

### Microsoft SQL Server

```yaml
storage:
  type: SQLSERVER
  url: "jdbc:sqlserver://db.example:1433;databaseName=minecraft;encrypt=true"
  username: minecraft
  password: "CHANGE_ME"
```

The database must already exist. The configured account needs permission to create and use the plugin’s table.

The build includes the corresponding JDBC drivers.

### Hosted databases

There is no separate `SUPABASE`, `ORACLE`, or `CUSTOM_JDBC` storage mode in this release.

A hosted PostgreSQL service would use the `POSTGRESQL` backend and its provider-specific connection settings. The included tests do not establish compatibility with a particular hosted service, pooler, or free plan.

### Storage design and performance

SQL stores the plugin state in a shared, locked record in:

```text
ve_records_v2
```

File backends also save a complete state snapshot.

Consequences of this design:

- Operations are serialized around shared state.
- Remote database latency can affect synchronous economy calls.
- Frequent achievement updates add storage work.
- File backends should not be shared by multiple processes.
- Large-network scalability is not established by the supplied tests.

There is no configurable table prefix. Independent networks should use separate databases or schemas.

---

## 🗄️ Backups and Storage Migration

Create an export:

```text
/ve backup
```

Exports are written under:

```text
plugins/VaultEconomy/backups/
```

### Offline verification

Run from a terminal with the built JAR:

```bash
java -cp VaultEconomy-3.0.0-rc1.jar me.kodysimpson.vaulteconomy.core.StorageTool verify export.json
```

### Offline export

Create a `storage.properties` file matching the source backend:

```properties
type=SQLITE
url=
username=
password=
```

Then run:

```bash
java -cp VaultEconomy-3.0.0-rc1.jar me.kodysimpson.vaulteconomy.core.StorageTool export plugins/VaultEconomy/data-v2 storage.properties export.json
```

### Change backends

1. Export and verify the existing data.
2. Stop every process using the source storage.
3. Back up the complete plugin directory.
4. Prepare an empty target backend.
5. Create target connection settings in `storage.properties`.
6. Import into the final target location.
7. Update `config.yml` to match.
8. Start a test server and compare balances and records.

Example import:

```bash
java -cp VaultEconomy-3.0.0-rc1.jar me.kodysimpson.vaulteconomy.core.StorageTool import plugins/VaultEconomy/data-v2 storage.properties export.json
```

The importer refuses a non-empty target.

Do not delete `storage.binding` to force a configuration change. It protects against accidentally switching to an unrelated or empty backend.

For SQLite file backups, stop the server and copy the whole `data-v2` directory. Copying only `records.db` during operation can omit WAL-related data.

---

## 🌐 Velocity and BungeeCord

The same project includes separate proxy entry points.

### Proxy configuration

Generated file:

```text
proxy.properties
```

Example:

```properties
hub=lobby
language=en
economy.enabled=false
storage.type=MYSQL
storage.url=jdbc:mysql://127.0.0.1:3306/minecraft
storage.username=minecraft
storage.password=CHANGE_ME
```

Shared economy is disabled by default.

### Proxy commands and permissions

| Command | Function | Permission |
|---|---|---|
| `/hub` | Connect to the configured hub | `vaulteconomy.proxy.hub` |
| `/veserver [name]` | List servers or connect | `vaulteconomy.proxy.veserver` |
| `/veglist` | Network player list | `vaulteconomy.proxy.veglist` |
| `/gmsg <player> <message>` | Network private message | `vaulteconomy.proxy.gmsg` |
| `/gstaff <message>` | Network staff chat | `vaulteconomy.proxy.gstaff` |
| `/gbalance [player]` | Read shared money balance | `vaulteconomy.proxy.gbalance` |
| `/gpay <player> <amount>` | Shared-economy payment | `vaulteconomy.proxy.gpay` |
| `/geco give/take/set <player> <amount>` | Manage shared balances | `vaulteconomy.proxy.geco` |
| `/veproxy` | Proxy-module status | `vaulteconomy.proxy.veproxy` |

Additional checks:

```text
vaulteconomy.proxy.server.<server-name>
vaulteconomy.proxy.gbalance.others
vaulteconomy.proxy.geco.give
vaulteconomy.proxy.geco.take
vaulteconomy.proxy.geco.set
```

Grant these in the proxy’s permission system. Bukkit operator status does not automatically grant proxy permissions.

### Shared economy setup

- Configure the participating proxy and backends to use the same remote SQL database.
- Enable `economy.enabled=true` on the proxy.
- Keep player UUID forwarding consistent.
- Give each gameplay server a different `server-id`.

Example backend identities:

```yaml
server-id: survival
```

```yaml
server-id: skyblock
```

Money and identity records can be shared. Server-specific records, such as local locations and auctions, are separated using `server-id`.

Backend chat mutes and ignores do not automatically control `/gmsg`. It is a separate network messaging channel.

---

## 🔌 PlaceholderAPI

The plugin registers its own expansion when PlaceholderAPI is installed.

No separate expansion download is required.

| Placeholder | Output |
|---|---|
| `%vaulteconomy_balance%` | Main balance as a decimal |
| `%vaulteconomy_balance_formatted%` | Main balance with configured currency symbol |
| `%vaulteconomy_points_<id>%` | Custom-currency balance |
| `%vaulteconomy_clan%` | Clan name |
| `%vaulteconomy_prefix%` | Stored prefix |
| `%vaulteconomy_kills%` | Recorded kills |
| `%vaulteconomy_deaths%` | Recorded deaths |
| `%vaulteconomy_storage%` | Active backend type |

Example:

```text
%vaulteconomy_points_tokens%
```

Use these with compatible scoreboard, TAB, hologram, or menu plugins. VaultEconomy does not require a specific scoreboard layout.

---

## ⚙️ Configuration Files

| File | Purpose |
|---|---|
| `config.yml` | Core storage, currency, teleport, combat, auction, clan, chat, and language settings |
| `config-v2-example.yml` | Reference configuration retained for upgrades |
| `items.yml` | Custom-item definitions |
| `recipes.yml` | Crafting, smelting, and blocked recipes |
| `brewing.yml` | Custom brewing |
| `fishing.yml` | Species, minigame, buyer, and prices |
| `achievements.yml` | Progression and rewards |
| `effects.yml` | Particles, sounds, and spear settings |
| `extensions.yml` | Jail settings and extension command throttle |
| `lang/en.yml` | English Bukkit messages |
| `lang/ru.yml` | Russian Bukkit messages |
| `proxy.properties` | Proxy settings |
| `lang/*.properties` | Proxy translations |

### Core settings at a glance

| Setting | Default | Purpose |
|---|---:|---|
| `server-id` | `survival` | Namespace for server-local records |
| `storage.type` | `SQLITE` | Active storage backend |
| `teleport.delay` | `3` | Teleport delay in seconds |
| `limits.homes` | `2` | Base home limit |
| `limits.near` | `100` | Base nearby-player search radius |
| `limits.auction` | `5` | Base auction limit |
| `rtp.radius` | `2000` | Base random-teleport radius |
| `combat.seconds` | `15` | Combat-tag duration |
| `combat.kill-on-logout` | `true` | Combat logout penalty |
| `auction.tax-percent` | `3.0` | Auction tax |
| `auction.lifetime-seconds` | `172800` | Auction lifetime |
| `clans.create-price` | `1000.00` | Clan creation price |
| `clans.storage-upgrade-price` | `2000.00` | Storage upgrade price |
| `clans.max-members` | `20` | Clan member limit |
| `chat.enabled` | `false` | Plugin chat formatting |
| `language` | `ru` | Default interface language |

`server-id` accepts letters, numbers, underscores, and hyphens, with a maximum length of 32.

### Disable selected content

Examples:

- Set `fishing.yml → enabled: false` to disable normal custom-fishing activation.
- Set `fishing.yml → buyer.enabled: false` to disable fish purchases.
- Set `achievements.yml → enabled: false` to stop normal achievement progress tracking.
- Set an individual definition’s `enabled: false` to disable its supported processing.
- Set `effects.yml → particles: false` or `sounds: false`.
- Restrict command access through permissions.

These switches do not form a universal module manager. For example, stopping achievement progress does not automatically erase completed progress or disable every administrative command.

Menu text can be translated, but this release does not provide a general YAML editor for every GUI slot, material, and layout.

---

## 🔄 Reload Commands

| Command | Actual scope |
|---|---|
| `/ve reload` | Reloads core `config.yml` settings |
| `/vex reload` | Reloads extension definitions, languages, recipe registration, and content permission defaults |
| `/language reload` | Reloads language files |
| `/customitem reload` | Uses the shared extension reload |
| `/recipe reload` | Uses the extension reload and recipe registration path |
| `/fish reload` | Uses the shared extension reload |
| `/achievementedit reload` | Uses the shared extension reload |

The content-specific reload names should not be interpreted as completely isolated reloads.

**Storage connections and `server-id` are not switched live by `/ve reload`.** Change them through the appropriate stopped-server migration process.

### YAML editing tips

- Use spaces for indentation.
- Preserve the top-level section names.
- Merge examples into existing sections instead of adding duplicate top-level keys.
- Quote names containing color codes.
- Quote hexadecimal colors.
- Keep IDs stable when existing items or rewards reference them.
- Check the console after a reload.
- Test access with a non-operator account.

Command-based definition changes are saved to YAML with a backup and atomic file replacement.

---

## 🧪 Verification Included in the Source Archive

The supplied archive contains reports for:

| Check | Reported result |
|---|---|
| Core tests | 156 checks |
| Extension-core tests | 18 checks |
| MockBukkit 1.16.5 tests | 42 checks |
| Extension command routes | 391 loaded |
| Root commands | 108 registered |
| Static permissions | 819 definitions |
| API reference checks | 519 direct Bukkit references checked against each supplied target API |

Reported checks cover areas such as:

- Local-backend persistence and reopening.
- Transaction rollback.
- Concurrent money transfers.
- H2 shared-row locking.
- Export and migration behavior.
- Item signatures.
- Fishing minigame rules.
- Repeated fish-sale prevention.
- Achievement claim tracking.
- Item reward queues.
- Recipe permissions and ingredient matching.
- English and Russian resource loading.

These are the reports included with the release. They are not a claim that every gameplay system was exercised on a live Minecraft server.

### Remaining verification limits

The supplied results do not establish:

- Live compatibility with every listed server platform.
- Compatibility with every intermediate Minecraft release.
- Multi-region Folia behavior under real gameplay.
- Real MySQL, MariaDB, PostgreSQL, or SQL Server integration results.
- Full physical brewing and furnace behavior across versions.
- Client-side appearance of every animation.
- LibsDisguises integration behavior on every version.
- Performance at a particular player count.

Storage transactions also do not create a single atomic transaction with Minecraft world files, player inventories, or unrelated plugins.

---

## 📚 Legacy Content and Upgrades

The release retains older content commands:

```text
/kastom [3|4]
/kostoms [1|2|3]
```

These provide the legacy Strength potions and furnace tiers.

Relevant permissions include:

```text
vaulteconomy.potion.craft
vaulteconomy.potion.upgrade
vaulteconomy.potion.give.3
vaulteconomy.potion.give.4

vaulteconomy.furnace.use
vaulteconomy.furnace.give.1
vaulteconomy.furnace.give.2
vaulteconomy.furnace.give.3
```

The legacy content includes a Strength III crafting recipe and a Strength IV anvil upgrade using two Strength III potions and experience levels.

### Existing installations

Keep the existing plugin data directory.

The migration code supports known older records, including balances, additional currencies, locations, ignores, marriages, prefixes, and recognized clan data.

Name-based legacy balances are associated with UUIDs through the identity process.

Do not change authentication mode or UUID forwarding casually during migration. A player’s identity must remain consistent.

A JAR alone cannot recover previously lost server data.

---

## 🧱 Building from Source

The source distribution includes:

- Java source.
- Pinned API dependencies.
- JDBC drivers.
- ECJ compiler.
- Dependency checksums.
- Build scripts.
- Test source and reports.

Build with Python 3 and the Java version required by the bundled compiler:

```bash
python3 tools/build.py
```

The build script writes:

```text
dist/VaultEconomy-3.0.0-rc1.jar
```

Run the core tests:

```bash
python3 tools/test_core.py
```

Additional test setup is documented in:

```text
docs/TESTS_RU.md
```

The packaged build uses the included dependencies rather than downloading them at runtime.

---

## ❓ Frequently Asked Questions

### Is an external database required?

No. SQLite is configured automatically by default.

### Do players need a client mod?

No client mod is required for the implemented server-side gameplay.

CustomModelData visuals require a compatible resource pack if you want a custom appearance.

### Is Vault required?

Only for connecting compatible external plugins through the Vault economy API. Built-in money commands work without it.

### Can I create items without Java?

Yes. Use YAML definitions and the administrative editors.

### Can I create new client materials?

No. Custom items use materials available in the running Minecraft version.

### Can I restrict an item to a rank?

Yes. Set `public: false` and grant the appropriate `vaulteconomy.item.use.<id>` permission. Recipe and giving rights remain separate.

### Can I remove a vanilla enchantment?

Yes, for an applied enchantment on the held item:

```text
/itemedit unenchant sharpness
```

### Can I make a player glow?

Yes:

```text
/playerctl glowing true Alex
```

Or apply a timed glowing effect:

```text
/playerctl effect glowing 30 1 Alex
```

### Can I create a custom fish buyer NPC?

The plugin supplies a buyer menu and location restriction. Use a separate NPC plugin to present that menu through an NPC.

### Can I share balances across servers?

The implementation supports a common remote SQL backend. Use matching UUIDs and distinct backend `server-id` values, and test the setup before deployment.

### Does it support every future Minecraft version?

Future-version compatibility is not guaranteed. Use the release’s actual verification information and test the specific server build.

### Is there a paid activation requirement?

No. This source release contains no license activation, trial expiration, payment requirement, or nickname-based premium access.

---

**VaultEconomy — configurable economy, server tools, and custom gameplay for your Minecraft community.**
