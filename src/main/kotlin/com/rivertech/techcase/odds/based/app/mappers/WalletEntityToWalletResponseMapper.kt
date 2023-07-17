package com.rivertech.techcase.odds.based.app.mappers

import com.rivertech.techcase.odds.based.app.entity.WalletTransaction
import com.rivertech.techcase.odds.based.app.model.WalletTransactionsResponse
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers

@Mapper
interface WalletEntityToWalletResponseMapper {
    @Mappings(
            Mapping(source = "createdDate", target = "transactionTime")
    )
    fun toResponse(walletTransaction: WalletTransaction): WalletTransactionsResponse
    fun toResponse(walletTransactions : List<WalletTransaction>): List<WalletTransactionsResponse>

    companion object {
        val INSTANCE: WalletEntityToWalletResponseMapper = Mappers.getMapper(WalletEntityToWalletResponseMapper::class.java)
    }
}