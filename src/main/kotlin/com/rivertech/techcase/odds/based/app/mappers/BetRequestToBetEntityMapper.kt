package com.rivertech.techcase.odds.based.app.mappers

import com.rivertech.techcase.odds.based.app.entity.Bet
import com.rivertech.techcase.odds.based.app.entity.Player
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers
import java.math.BigDecimal

@Mapper
interface BetRequestToBetEntityMapper {
    @Mappings(
            Mapping(source = "player", target = "player")
    )
    fun toEntity(player: Player, betAmount: BigDecimal, betNumber: Int): Bet

    companion object {
        val INSTANCE: BetRequestToBetEntityMapper = Mappers.getMapper(BetRequestToBetEntityMapper::class.java)
    }
}