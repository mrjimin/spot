package io.github.mrjimin.spot.global.common.response

import com.fasterxml.jackson.annotation.JsonInclude
import io.github.mrjimin.spot.global.exception.ErrorCode

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseForm<T>(
    val success: Boolean,
    val code: Int,
    val message: String?,
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T, message: String? = "성공"): ResponseForm<T> =
            ResponseForm(
                success = true,
                code = 200,
                message = message,
                data = data
            )

        fun success(message: String? = "성공"): ResponseForm<Unit> =
            ResponseForm(
                success = true,
                code = 200,
                message = message
            )

        fun fail(errorCode: ErrorCode, message: String? = null): ResponseForm<Nothing> =
            ResponseForm(
                success = false,
                code = errorCode.status.value(),
                message = message ?: errorCode.message
            )

        fun fail(code: Int, message: String?): ResponseForm<Nothing> =
            ResponseForm(
                success = false,
                code = code,
                message = message
            )
    }
}