package com.rivertech.techcase.odds.based.app.repository

import com.rivertech.techcase.odds.based.app.entity.Player
import com.rivertech.techcase.odds.based.app.entity.WalletTransaction
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WalletTransactionRepository : CrudRepository<WalletTransaction, UUID> {
    fun getWalletTransactionsByPlayer(player: Player): List<WalletTransaction>

    @Query(
            value = """
            select dbt.player_id,player.username, sum(crdt_amt)-sum(dbt_amt) as amt from
            (select player_id, sum(amount) as crdt_amt from wallet_transaction
            where transaction_type='CREDIT'
            group by player_id) crdt join
            (select player_id,sum(amount) as dbt_amt from wallet_transaction
            where transaction_type='DEBIT'
            group by player_id) dbt on crdt.player_id=dbt.player_id
            join player on crdt.player_id=player.id
            group by  dbt.player_id,player.username
            order by 3 desc;
        """,
            nativeQuery = true
    )
    fun getPlayerWinnings(): List<Array<Any>>
}