package dev.bingley

import dev.bingley.commands.ForceBattleCmd
import net.fabricmc.api.ModInitializer

class CardClash : ModInitializer {
    companion object {
        // Expose BattleManager instance for command access
        val battleManager = BattleManager()
    }

    override fun onInitialize() {
        // Register commands
        ForceBattleCmd.register()
    }
}
