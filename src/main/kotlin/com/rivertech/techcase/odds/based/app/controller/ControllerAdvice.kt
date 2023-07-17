package com.rivertech.techcase.odds.based.app.controller

import com.rivertech.techcase.odds.based.app.common.FieldValidationReason
import com.rivertech.techcase.odds.based.app.exceptions.NotEnoughCreditException
import com.rivertech.techcase.odds.based.app.exceptions.PlayerNotFoundException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.ResponseEntity
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebInputException
import java.util.function.Consumer

@ControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(
            e: MethodArgumentNotValidException): ResponseEntity<List<String?>> {
        val errors = e.bindingResult
                .fieldErrors
                .map { it.defaultMessage }
        return ResponseEntity.badRequest().body(errors)
    }

    @ExceptionHandler(ServerWebInputException::class)
    fun handleHttpMessageNotReadableException(
            e: ServerWebInputException): ResponseEntity<String> {
        val error = "Invalid request body."
        return ResponseEntity.badRequest().body(error)
    }

    @ExceptionHandler(WebExchangeBindException::class)
    fun constraintViolationExceptionHandler(
            e: WebExchangeBindException): ResponseEntity<MutableList<FieldValidationReason>>? {
        val bindingResult = e.bindingResult
        val reasons: MutableList<FieldValidationReason> = ArrayList<FieldValidationReason>()
        bindingResult.allErrors.forEach(Consumer<ObjectError> { allError: ObjectError -> extractReasons(reasons, allError) })
        return ResponseEntity.badRequest().body(reasons)
    }

    @ExceptionHandler(PlayerNotFoundException::class)
    fun userNotFoundExceptionHandler(
            e: PlayerNotFoundException): ResponseEntity<String>? {
        return ResponseEntity.badRequest().body(e.message)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun userAlreadyExistsExceptionHandler(
            e: DataIntegrityViolationException): ResponseEntity<String>? {
        return ResponseEntity.badRequest().body("The player is already registered in the system. Please try again with a different username.")
    }

    @ExceptionHandler(NotEnoughCreditException::class)
    fun notEnoughCreditExceptionHandler(
            e: NotEnoughCreditException): ResponseEntity<String>? {
        return ResponseEntity.badRequest().body("Insufficient funds in the player's wallet.")
    }

    private fun extractReasons(reasons: MutableList<FieldValidationReason>, error: ObjectError) {
        val codes = error.codes
        var name: String = ""
        if (codes != null) {
            name = codes[0].substring(codes[0].lastIndexOf('.') + 1)
        }
        val defaultMessage = error.defaultMessage
        reasons.add(FieldValidationReason(field = name, message = defaultMessage.orEmpty()))
    }
}