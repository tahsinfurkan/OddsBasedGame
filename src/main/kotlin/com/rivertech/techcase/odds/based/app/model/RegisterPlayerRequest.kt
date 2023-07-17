package com.rivertech.techcase.odds.based.app.model

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class RegisterPlayerRequest(
        @field:NotNull
        @field:NotEmpty
        val username: String,
        @field:NotNull
        @field:NotEmpty
        val name: String,
        @field:NotNull
        @field:NotEmpty
        val surname: String
)