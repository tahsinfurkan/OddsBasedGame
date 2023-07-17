package com.rivertech.techcase.odds.based.app.repository

import com.rivertech.techcase.odds.based.app.entity.Player
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PlayerRepository : JpaRepository<Player, UUID> {
}