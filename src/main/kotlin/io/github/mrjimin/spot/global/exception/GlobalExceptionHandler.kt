package io.github.mrjimin.spot.global.exception

import io.github.mrjimin.spot.global.common.response.ResponseForm
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(SpotException::class)
    fun handleSpotException(e: SpotException): ResponseEntity<ResponseForm<Nothing>> {
        val errorCode = e.errorCode
        return ResponseEntity
            .status(errorCode.status)
            .body(ResponseForm.fail(errorCode.status.value(), errorCode.message))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ResponseForm<Nothing>> {
        return ResponseEntity
            .badRequest()
            .body(ResponseForm.fail(400, e.message ?: "잘못된 요청입니다."))
    }

    @ExceptionHandler(Exception::class)
    fun handleAllException(e: Exception): ResponseEntity<ResponseForm<Nothing>> {
        // e.printStackTrace()
        return ResponseEntity
            .internalServerError()
            .body(ResponseForm.fail(500, "서버 내부 오류가 발생했습니다."))
    }
}