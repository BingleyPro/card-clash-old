package dev.bingley

import java.util.UUID

class BattleManager {
    // Tracks which player is in a battle and their opponent
    private val battles = mutableMapOf<UUID, BattleInfo>()

    // BattleInfo holds the opponent's UUID and the arena number
    data class BattleInfo(val opponent: UUID, val arenaNumber: Int)

    // Start a new battle between 2 players
    fun startBattle(player1: UUID, player2: UUID, arenaNumber: Int) {
        // Check that the players are not the same
        if (player1 == player2) {return}

        // Check that one of the players aren't already in a battle

        battles[player1] = BattleInfo(player2, arenaNumber)
        battles[player2] = BattleInfo(player1, arenaNumber)
    }

    // Check if a player is in a battle
    fun isInBattle(player: UUID): Boolean {
        return battles.containsKey(player)
    }

    // Get the opponent of a player
    fun getOpponent(player: UUID): UUID? {
        return battles[player]?.opponent
    }

    // Get the arena where the player is battling
    fun getArena(player: UUID): Int {
        return battles[player]?.arenaNumber ?: -1 // -1 means not in battle
    }

    // End a battle
    fun endBattle(player1: UUID, player2: UUID) {
        battles.remove(player1)
        battles.remove(player2)
    }
}