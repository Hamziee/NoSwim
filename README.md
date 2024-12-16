## NoSwim

**NoSwim** is a Minecraft plugin made for event hosts and server administrators. It prevents players from swimming freely, ensuring a more controlled experience during in-game events. This utility is especially useful in scenarios where swimming could give players an unfair advantage or disrupt the flow of an event, such as in obstacle courses, survival challenges, or an island where players are not allowed to go out of.

## Features

- **Control Player Behavior in Water (All configurable in the config)**:  
   - **Min Water Height**: Prevent swimming in deep water by setting a minimum water depth.
   - **Player Damage**: Option to apply poison or drowning damage when players enter deep water.
   - **Downforce**: Control the sinking speed when players are in deep water.
   - **Water Breathing Exemption**: Exempt players with the Water Breathing effect from sinking.
- **Building Restrictions Above Water**: Prevent players from building above water areas that are restricted. (Not fully developed yet)

## Commands

The plugin supports the following commands:

- `/noswim enable`: Enables the NoSwim plugin's behavior (prevents swimming in deep water).
- `/noswim disable`: Disables the NoSwim plugin's behavior.
- `/noswim reload`: Reloads the plugin configuration from the `config.yml`.

## Permissions

- `noswim.admin`: Allows players to use the `/noswim` command (for enabling, disabling, and reloading the plugin).
- `noswim.bypass`: Allows players to bypass the sinking mechanic and swim in deep water.

## Configuration Options

The `config.yml` file allows you to customize the plugin's behavior. Below are the available configuration settings:

```yaml
# NoSwim Configuration File - Version 0.1-BETA-B1
# It is highly recommend to regenerate the config file after downloading a new version.
# To regenerate the config file: delete this config file and restart the server.

# Minimum water height (depth) to activate the sinking effect
min-water-height: 2                # Number

# Exempt players with the Water Breathing effect
water-breath-exemption: true       # Options: true, false

# Damage options: poison, drowning, or none
player-damage:
  enabled: false                   # Options: true, false
  type: poison                     # Options: poison, drowning, none
  damage-interval: 40              # Ticks (20 ticks = 1 second)

# Sinking effect
downforce:
  enabled: true                    # Toggle sinking effect on/off | Options: true, false
  amount: 0.25                     # Default sinking speed (0.1 is slower, 0.5 is faster)

# [BETA] Allow building above water
# This feature is still in development and contains bugs,
# players can easily bypass this restriction.
# It is recommended to let players build above water until the bugs are fixed.
allow-building-above-water: true   # Options: true, false (Default: true)
```

### Configuration Variables:

- **`min-water-height`**: Defines the minimum height of water required for the sinking effect to be triggered. Players in water less than this height won’t experience the sinking effect.
  
- **`water-breath-exemption`**: If set to `true`, players with the Water Breathing effect won’t be affected by the sinking behavior.

- **`player-damage`**:
  - **`enabled`**: Set to `true` to enable player damage effects (e.g., poisoning or drowning). 
  - **`type`**: Determines the type of damage inflicted. Options are `poison`, `drowning`, or `none`.
  - **`damage-interval`**: Defines the interval (in ticks) between each damage application.

- **`downforce`**:
  - **`enabled`**: Controls whether the sinking effect is active. If set to `false`, the player will float.
  - **`amount`**: Determines the rate at which players sink. Values closer to 0 make players sink slower, and higher values make them sink faster.

- **`allow-building-above-water`**: Allows or blocks players from building above water. This feature is still in development, and players can bypass the restriction. Use with caution until future updates.

## Troubleshooting

- **Players are not sinking in deep water**: Make sure the `min-water-height` is set correctly and that the `downforce` value is configured correctly.
- **Damage isn't applied**: Verify that the `player-damage` setting in the config is set to `true` and that a valid damage type (`poison` or `drowning`) is specified.
- **Players can still build above water**: This is because the plugin only checks for water one block under the placed block. That's why I recommend to keep building above waters enabled for now. This will be fixed in later versions, when I figure out a good way to implement a check for this.

## Contribution

Feel free to fork this plugin and submit pull requests if you'd like to contribute new features or fixes. Contributions are always welcome!

## License

NoSwim is licensed under the MIT License. See the LICENSE file for more details.