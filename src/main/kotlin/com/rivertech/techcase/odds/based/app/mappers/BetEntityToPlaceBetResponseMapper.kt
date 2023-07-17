package com.rivertech.techcase.odds.based.app.mappers

import com.rivertech.techcase.odds.based.app.common.TransactionType
import com.rivertech.techcase.odds.based.app.entity.Bet
import com.rivertech.techcase.odds.based.app.model.PlaceBetResponse
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import java.math.BigDecimal

@Mapper
interface BetEntityToPlaceBetResponseMapper {

    fun toResponse(bet: Bet, generatedNumber: Int, transactionType: TransactionType, winningAmount: BigDecimal, walletBalance: BigDecimal): PlaceBetResponse

    companion object {
        val INSTANCE: BetEntityToPlaceBetResponseMapper = Mappers.getMapper(BetEntityToPlaceBetResponseMapper::class.java)
    }
}