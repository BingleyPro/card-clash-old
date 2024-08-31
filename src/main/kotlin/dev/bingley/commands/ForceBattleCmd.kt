package dev.bingley.commands

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.bingley.BattleManager
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
        val result = battlemanager.startBattle(player1.uuid, player2.uuid, arena)

        when (result) {
            BattleManager.BattleStartResult.SUCCESS -> {
                player1.sendMessage(Text.literal("You are now in a battle with ${player2.name.string} in arena $arena"))
                player2.sendMessage(Text.literal("You are now in a battle with ${player1.name.string} in arena $arena"))
                return 1 // Success
            }

            BattleManager.BattleStartResult.SAME_PLAYER_ERROR -> {
                context.source.sendError(Text.literal("Error: The same player cannot battle themself!"))
                return 0 // Failed
            }

            BattleManager.BattleStartResult.PLAYER_IN_BATTLE_ERROR -> {
                context.source.sendError(Text.literal("Error: One or both of the players are already in a battle."))
                return 0 // Failed
            }
        }
    }
}