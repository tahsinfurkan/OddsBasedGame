package com.rivertech.techcase.odds.based.app.mappers

import com.rivertech.techcase.odds.based.app.common.TransactionType
import com.rivertech.techcase.odds.based.app.entity.Bet
import com.rivertech.techcase.odds.based.app.entity.Player
import com.rivertech.techcase.odds.based.app.entity.WalletTransaction
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers
import java.math.BigDecimal

@Mapper
interface WalletRequestToWalletEntityMapper {
    @Mappings(
            Mapping(source = "bet", target = "bet")
    )
    fun toEntity(player: Player, transactionType: TransactionType, amount: BigDecimal, bet: Bet): WalletTransaction

    companion object {
        val INSTANCE: WalletRequestToWalletEntityMapper = Mappers.getMapper(WalletRequestToWalletEntityMapper::class.java)
    }
}