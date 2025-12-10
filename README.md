# Stat API
_A spigot plugin to expose player stats and server information over a simple HTTP API_
Stat API lets external apps, dashboards or Discord bots access live minecraft server data through endpoints.
___
## Features
* Exposes an HTTP API on a configurable port
* Fetch online players & player count
* Fetch the player statistics seen in the in-game statistics tab
* Fetch player info with configurable exposure of location & armor
* Optional API key authentication (SHA-256 hashed)
* Pull requests & new feature ideas welcome!
---
## Configuration `(config.yml)`
| Option                    | Description                                                          |
|---------------------------|----------------------------------------------------------------------|
| `show-player-armor`       | Whether or not to show armor information in the player info response |
| `show-player-coordinates` | Whether or not to show player's location in player info response     |
| `api-keys`                | **Optional.** If list is empty → no authentication required          |
| `port`                    | Port the API listens on                                              |

### Authentication (optional)
If `api-keys` is not empty, all API requests must include:
```
Authorization: Bearer <password>
```
Your password is hashed using SHA-256 (UTF-8) and compared against values in `api-keys`.

To disable auth: set the list empty:
```
api-keys: []
```
___
## Endpoints
### GET /server
Returns a list of online players.

<details>
    <summary>Example response</summary>

```json
{
  "playerCount": 1,
  "players": [
    {
      "uuid": "29e391aa-e42f-475c-a232-e4dfe692fe33",
      "name": "Need_Not"
    }
  ]
}
```

</details>

### GET /player/<name | uuid\>
Returns live player data such as health, armor, and location.

<details>
    <summary>Example response</summary>

```json
{
  "uuid": "29e391aa-e42f-475c-a232-e4dfe692fe33",
  "name": "Need_Not",
  "health": 20.0,
  "foodLevel": 14,
  "location": {
    "x": 52.69999998807907,
    "y": 70.0,
    "z": -113.94438720191937,
    "world": "NORMAL"
  },
  "armor": {
    "helmet": {
      "type": "DIAMOND_HELMET",
      "amount": 1,
      "metaData": {
        "damage": 13,
        "enchantments": [
          {
            "key": "unbreaking",
            "level": 3
          },
          {
            "key": "aqua_affinity",
            "level": 1
          }
        ]
      }
    },
    "chestplate": {
      "type": "CHAINMAIL_CHESTPLATE",
      "amount": 1,
      "metaData": {
        "damage": 0
      }
    },
    "leggings": {
      "type": "CHAINMAIL_LEGGINGS",
      "amount": 1,
      "metaData": {
        "damage": 0
      }
    },
    "boots": {
      "type": "CHAINMAIL_BOOTS",
      "amount": 1,
      "metaData": {
        "damage": 0
      }
    }
  },
  "firstPlayed": 1758352568995,
  "lastPlayed": 1765338842139,
  "online": true
}
```
(Armor and location included only if enabled in config.)

</details>

### GET /player/<name | uuid\>/statistics/<general | mobs | items\>
Returns same player statistics that can be found in the statistics tab in-game.

<details>
    <summary>Example "general" response</summary>

```json
{
  "DAMAGE_DEALT": 57,
  "DAMAGE_TAKEN": 1117,
  "DEATHS": 4,
  "MOB_KILLS": 0,
  "PLAYER_KILLS": 0,
  ...
}
```
</details>

<details>
    <summary>Example "mobs" response</summary>

```json
{
  "ELDER_GUARDIAN": {
    "KILL_ENTITY": 0,
    "ENTITY_KILLED_BY": 0
  },
  "WITHER_SKELETON": {
    "KILL_ENTITY": 0,
    "ENTITY_KILLED_BY": 0
  },
  "STRAY": {
    "KILL_ENTITY": 0,
    "ENTITY_KILLED_BY": 0
  },
  ...
}
```
</details>

<details>
    <summary>Example "items" response</summary>

```json
{
  "SAND": {
    "DROP": 2,
    "PICKUP": 3,
    "MINE_BLOCK": 2,
    "USE_ITEM": 0,
    "BREAK_ITEM": 0,
    "CRAFT_ITEM": 0
  },
  ...
          
}
```
</details>

___
## Contributing
New features, bug fixes, and pull requests are welcome! <br>
I don't know MC anymore so I won't be finding bugs or adding new features unless someone brings it to my attention.