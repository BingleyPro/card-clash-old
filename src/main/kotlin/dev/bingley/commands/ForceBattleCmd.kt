package dev.bingley.commands

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.bingley.CardClash
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

object ForceBattleCmd {
    fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(
                CommandManager.literal("forcebattle")
                    .then(
                        CommandManager.argument("player1", EntityArgumentType.player())
                            .then(
                                CommandManager.argument("player2", EntityArgumentType.player())
                                    .then(
                                        CommandManager.argument("arena", IntegerArgumentType.integer())
                                    )
                            )
                    )
            )
        }
    }

    private fun execute(context: CommandContext<ServerCommandSource>): Int {
        val player1 = EntityArgumentType.getPlayer(context, "player1")
        val player2 = EntityArgumentType.getPlayer(context, "player2")
        val arena = IntegerArgumentType.getInteger(context, "arena")

        val battlemanager = CardClash.battleManager
        battlemanager.startBattle(player1.uuid, player2.uuid, arena)

        player1.sendMessage(Text.literal("You are now in a battle with ${player2.name.string} in arena $arena"))
        player2.sendMessage(Text.literal("You are now in a battle with ${player1.name.string} in arena $arena"))

        return 1 // Success
    }
}