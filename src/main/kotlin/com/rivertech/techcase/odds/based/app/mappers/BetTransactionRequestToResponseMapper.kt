package com.rivertech.techcase.odds.based.app.mappers

import com.rivertech.techcase.odds.based.app.entity.Bet
import com.rivertech.techcase.odds.based.app.model.BetResultsResponse
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface BetTransactionRequestToResponseMapper {

    fun toResponse(bets: List<Bet>?): List<BetResultsResponse>

    companion object {
        val INSTANCE: BetTransactionRequestToResponseMapper = Mappers.getMapper(BetTransactionRequestToResponseMapper::class.java)
    }
}