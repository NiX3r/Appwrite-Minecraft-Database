[![](https://jitpack.io/v/nCodesDotEU/Appwrite-Minecraft-Database.svg)](https://jitpack.io/#nCodesDotEU/Appwrite-Minecraft-Database)

# 🎉 Appwrite Minecraft SKD
Utility Minecraft plugin that creates persistent data storage while using Appwrite.

## 📜 Features

- insert

## 💿 How to install

1. Download lastest plugin from [Spigot](insert)
2. Copy it into the server plugins folder
3. Start / Restart server for first load plugin
4. Setup config settings
5. Restart server
6. **Enjoy Appwrite Database plugin**!

## ⌨️ Commands

Aliases: `appwrite` | `aw`

- **Help**
  - Command for show help menu in game
  - Usage: `appwrite help`
- **Version**
  - Command for show current version
  - Usage: `appwrite version`
  - Aliases: `version` | `ver` | `v`
  - Permisson: `appwrite.version`
- **Database**
  - Command for work with database
  - Aliases: `database` | `db`
  - Sub commands:
    - **Player**
      - Command for work with players database
      - Aliases `player` | `db`
      - Sub commands:
        - **Save**
          - Command for save cache into database
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
      - Command for work with global database
      - Aliases `global` | `g`
      - Sub commands:
        - **Save**
          - Command for global save cache into database
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
          - Command to get a value of remote variable
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

### ⚙️ Config

There you can set up plugin utilities

* **appwrite.api_endpoint:**  setup your appwrite endpoint
* **appwrite.project_id:** setup your appwrite project ID
* **appwrite.api_key**: setup your appwrite api key

## 🈴 Languages

* 🇺🇸 [English](insert)
* 🇨🇿 [Česky](insert)
* 🇸🇰 [Slovensky](insert)

## 🧲 API

Firstable you have to import project via `maven`

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
3. Defaults - default values of variables
4. Inspects - inspect players keys, global keys and their values
5. TOP 10 - shows top 10 
6. Merch?

## 📆 Versions

| Plugin version | Description             | 1.17 | 1.16 | 1.14 | 1.12 | 1.8  | 1.7  | 1.5  |
| -------------- | ----------------------- | ---- | ---- | ---- | ---- | ---- | ---- | ---- |
| 0.0.1          | First release of plugin | ✔️    | ❌    | ❌    | ❌    | ❌    | ❌    | ❌    |



## ❓ FAQ

### 📣 Where to use local and where remote variables?

Local variables are those that don't save in the database. And on the other way, remote variables are those that save in the database. So you can use local variables as much as you want and they'll be never shown in the database. Fun fact: You can create a local variable and then just set it as remote and vice versa.

### 📣 Types of variables

You can play with several variables types. If you once choose them you cannot change them (must delete and then create again).

Types:

- **INT** - number without a decimal point
- **FLOAT** - number with a decimal point
- **STRING** - everything else (typically text)

For what is that good? For example, if you have to store money you have to store it as INT or FLOAT because of later adding/taking.

