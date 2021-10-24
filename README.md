[![](https://jitpack.io/v/nCodesDotEU/Appwrite-Minecraft-Database.svg)](https://jitpack.io/#nCodesDotEU/Appwrite-Minecraft-Database)

# 🎉 Appwrite Minecraft SDK

Utility Minecraft plugin that creates persistent data storage while using Appwrite as a database. This plugin was a missing piece on the scene for years! Finally, with Appwrite Minecraft SDK, storing data is as simple as `/appwrite database NiX3r set money 50`. A server makes no longer needs to pay over-priced developers just because there is no simple way to store data without writing Java code.

## 📜 Showcase

I am a server administrator, I have no coding experience, and I want to customize my server...

I have a dungeon minigame already created, but I don't want to let players play it infinitely; instead, I want to make this minigame a special reward. First, I start by running a command to define the variable `availableDungeonKeys` when a player first joins my server and set it to 0:

```
/appwrite database player set-remote %playername% availableDungeonKeys 3
```

Great, now when a new player joins, he will automatically have 3 keys.

When starting a minigame, I can use the placeholder `%aw_p_availableDungeonKeys%` to get the value and check if the amount is higher than 0. If that is the case, I can run a command to take 1 key from the player:

```
/appwrite database player take-remote %playername% availableDungeonKeys 1
```

Now that I have taken one key from the player, I can let him into a minigame. Tada 🎉 I have just created a system where players can join my minigame, but they can only do it 3 times.

Finally, I add an option to purchase more keys to my web-store and run the command once they purchase the keys:

```
/appwrite database player add-remote %playername% availableDungeonKeys 10
```

---

Let's take a look at another scenario... Let's say I need an energy system so the player can only join the minigame when he has at least 1 energy. Instead of buying it, he needs to collect 25 fragments in order to convert it to 1 energy. Fragments can be obtained by breaking a special block on the spawn. How do I achieve that?  It's extremely simple!

Let's start.. First, define variables when a player joins for the first time:

```
/appwrite database player set-remote %playername% energy 5
/appwrite database player set-local %playername% fragments 0
```

Notice how I marked fragments local because I want to lose the value when a player leaves the server - I don't want to store that.

Now, when a player breaks the specific block, I can run a command to increase his fragments count:

```
/appwrite database player add-local %playername% fragments 1
```

In this event, I also use placeholder `%aw_p_fragments%` to check if he has at least 25, and if that is the case, I reset it to 0 while increasing the energy count:

```
/appwrite database player add-remote %playername% energy 1
/appwrite database player set-local %playername% fragments 0
```

Wohoo, energy system finished 🥳 Here is a working example of such a logic:

<img src="https://github.com/nCodesDotEU/Appwrite-Minecraft-Database/blob/master/pictures/energy.gif">

## 💿 How to install

1. Download the latest plugin from [Spigot](insert) (TODO)
2. Copy the jar file into the server plugins folder
3. Start / Restart the server to trigger the first load  of the plugin
4. Insert Appwrite endpoint, project ID and API key into the generated config.yml file
5. Restart the server
6. **Enjoy Appwrite Database plugin!**

## ⌨️ Commands

Aliases: `appwrite` | `aw` <br>
Example of commands usage: <br>
<img src="https://github.com/nCodesDotEU/Appwrite-Minecraft-Database/blob/master/pictures/command.gif">

- **Help**
  - Command to show help menu in the game
  - Usage: `/appwrite help`
- **Version**
  - Command to show the currently installed version
  - Usage: `/appwrite version`
  - Aliases: `version` | `ver` | `v`
  - Permisson: `appwrite.version`
- **Database**
  - Command for work with the data storage
  - Aliases: `database` | `db`
  - Sub commands:
    - **Player**
      - Command to work with data of a specific player
      - Aliases `player` | `p`
      - Sub commands:
        - **Save**
          - Command for save cache into the database. This is triggered automatically when player leaves the server, but you can use this command to store it at a specific point.
          - Usage: `appwrite database player save <player>`
          - Aliases: `save` | `s`
          - Permission: `appwrite.player.save`
        - **Set Local**
          - Command to set a value of local variable
          - Usage: `appwrite database player set-local <player> <key> <value>`
          - Aliases: `set-local` | `sl`
          - Permission: `appwrite.player.set.local`
        - **Set Remote**
          - Command to set a value of remote variable
          - Usage: `appwrite database player set-remote <player> <key> <value>`
          - Aliases: `set-remote` | `sr`
          - Permission: `appwrite.player.set.remote`
        - **Add Local**
          - Command to increase a value of local variable
          - Usage: `appwrite database player add-remote <player> <key> <value>`
          - Aliases: `add-local` | `al`
          - Permission: `appwrite.player.add.local`
        - **Add Remote**
          - Command to increase a value of remote variable
          - Usage: `appwrite database player add-remote <player> <key> <value>`
          - Aliases: `add-remote` | `ar`
          - Permission: `appwrite.player.add.remote`
        - **Take Local**
          - Command to decrease a value of local variable
          - Usage: `appwrite database player take-local <player> <key> <value>`
          - Aliases: `takel-local` | `tl`
          - Permission: `appwrite.player.take.local`
        - **Take Remote**
          - Command to decrease a value of remote variable
          - Usage: `appwrite database player take-remote <player> <key> <value>`
          - Aliases: `take-remote` | `tr`
          - Permission: `appwrite.player.take.remote`
        - **Get**
          - Command to get a value of remote variable
          - Usage: `appwrite database player get <player> <key>`
          - Aliases: `get` | `g`
          - Permission: `appwrite.player.get`
    - **Global**
      - Command to work with global database. Here you can store variables that are shared for everyone one the server.
      - Aliases `global` | `g`
      - Sub commands:
        - **Save**
          - Command to save the global cache into database
          - Usage: `appwrite database global save`
          - Aliases: `save` | `s`
          - Permission: `appwrite.global.save`
        - **Set Local**
          - Command to set a value of local variable
          - Usage: `appwrite database global set-local <key> <value>`
          - Aliases: `set-local` | `sl`
          - Permission: `appwrite.global.set.local`
        - **Set Remote**
          - Command to set a value of remote variable
          - Usage: `appwrite database global set-remote <key> <value>`
          - Aliases: `set-remote` | `sr`
          - Permission: `appwrite.global.set.remote`
        - **Add Local**
          - Command to increase a value of local variable
          - Usage: `appwrite database global add-remote <key> <value>`
          - Aliases: `add-local` | `al`
          - Permission: `appwrite.global.add.local`
        - **Add Remote**
          - Command to increase a value of remote variable
          - Usage: `appwrite database global add-remote <key> <value>`
          - Aliases: `add-remote` | `ar`
          - Permission: `appwrite.global.add.remote`
        - **Take Local**
          - Command to decrease a value of local variable
          - Usage: `appwrite database global take-local <key> <value>`
          - Aliases: `takel-local` | `tl`
          - Permission: `appwrite.global.take.local`
        - **Take Remote**
          - Command to decrease a value of remote variable
          - Usage: `appwrite database global take-remote <key> <value>`
          - Aliases: `take-remote` | `tr`
          - Permission: `appwrite.global.take.remote`
        - **Get**
          - Command to get a value of global variable
          - Usage: `appwrite database global get <key>`
          - Aliases: `get` | `g`
          - Permission: `appwrite.global.get`
- **Reload**
  - Command to reload configs
  - Aliases `reload` | `r`
  - Sub commands:
    - **All**
      - Command to reload all configs
      - Usage: `appwrite reload all`
      - Aliases: `all` | `a`
      - Permission: `appwrite.reload.all`
    - **Messages**
      - Command to reload messages config
      - Usage: `appwrite reload messages`
      - Aliases: `messages` | `msg` | `m`
      - Permission: `appwrite.reload.messages`
    - **Config**
      - Command to reload config
      - Usage: `appwrite reload config`
      - Aliases: `config` | `c`
      - Permission: `appwrite.reload.config`

## 📐 Configs

### ⚙️ config.yml

In the `config.yml` file, you can configure the connection to the Appwrite server.

* **appwrite.api_endpoint:**  Your Appwrite endpoint
* **appwrite.project_id:** Your Appwrite project ID
* **appwrite.api_key**: Your Appwrite API key

### 🈴 languages.yml

* 🇺🇸 [English](https://github.com/nCodesDotEU/Appwrite-Minecraft-Database/blob/master/messages/english.yml)
* 🇨🇿 [Česky](https://github.com/nCodesDotEU/Appwrite-Minecraft-Database/blob/master/messages/cesky.yml)
* 🇸🇰 [Slovensky](insert) (TODO)

## 🧲 API

Before using the API, make sure your plugin is set up a valid Maven project.

First things first, you have to import the project from [JitPack](https://jitpack.io/#nCodesDotEU/Appwrite-Minecraft-Database). To do that, you enter `pom.xml` of your project and add `repository` and `dependency`.

```
	...
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
	...
	<dependency>
	    <groupId>com.github.nCodesDotEU</groupId>
	    <artifactId>Appwrite-Minecraft-Database</artifactId>
	    <version>Tag</version>
	</dependency>
	...
```

Then, you can start using the API from class `AppwriteDatabaseAPI`, for example:

```
String playerName = "NiX3r";
Player player = Bukkit.getPlayer(playerName);
String playerUUID = player.getUniqueId().toString();

AppwriteDatabaseAPI.addValueSync(playerUUID, "money", 12, true);
```

## 🔮 PlaceholdersAPI

- **Global**
  - Placeholder: `%aw_g_<key>%`
  - Stands for global variables
- **Player**
  - Placeholder: `%aw_p_<key>%`
  - Stands for source player variable
- **Other Player**
  - Placeholder: `%aw_po_<player>_<key>%`
  - Stands for different players variable

## 📋 TO-DO List

1. Don't remove local variables
2. Aliasses - mathematical formulas to count
3. Events - create database events (such as OnDataChange, OnDataLoad, OnDataSave, ...)
4. Defaults - default values of variables
5. Inspects - inspect players keys, global keys and their values
6. TOP 10 - shows top 10 
7. Merge data with external changes

## 📆 Versions

| Plugin version                                               | Description             | 1.17 | 1.16 | 1.14 | 1.12 | 1.8  | 1.7  | 1.5  |
| ------------------------------------------------------------ | ----------------------- | ---- | ---- | ---- | ---- | ---- | ---- | ---- |
| 0.0.1                                                        | First release of plugin | ✔️    | ❌    | ❌    | ❌    | ❌    | ❌    | ❌    |
| [0.0.2](https://github.com/nCodesDotEU/Appwrite-Minecraft-Database/releases/tag/v0.0.2) | Huge refactor           | ✔️    | ❌    | ❌    | ❌    | ❌    | ❌    | ❌    |
| [0.0.3](https://github.com/nCodesDotEU/Appwrite-Minecraft-Database/releases/tag/v0.0.3) | Fixed JitPack build     | ✔️    | ❌    | ❌    | ❌    | ❌    | ❌    | ❌    |



## ❓ FAQ

### 📣 Where to use local and where remote variables?

Local variables are those that don't save in the database. On the other hand, remote variables are those that are saved in the database. That means you can use local variables as much as you want, and they'll never be stored in the database. Fun fact: You can create a local variable and then just set it as remote and vice versa.

### 📣 Types of variables

You can use several variables types. Once you choose the type, you cannot change it - you must delete and then create in again to override the type.

Types:

- **INT** - number without a decimal point
- **FLOAT** - number with a decimal point
- **STRING** - everything else (typically text)

Why do I need different types? 🤔 For example, if you have to store money, you have to store it as INT or FLOAT, because later you will need to increase or decrease the value by a relative amount.

## Powered by [Appwrite](https://appwrite.io)!

<img src="insert" alt="Appwrite logo"> (TODO)
