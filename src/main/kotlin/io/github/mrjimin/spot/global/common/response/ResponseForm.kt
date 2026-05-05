package io.github.mrjimin.spot.global.common.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseForm<T>(
    val code: Int,
    val message: String?,
    val data: T? = null
) {
    companion object {
        fun success(message: String? = "성공"): ResponseForm<Nothing> =
            ResponseForm(200, message, null)

        fun <T> success(data: T, message: String? = "성공"): ResponseForm<T> =
            ResponseForm(200, message, data)

        fun fail(code: Int, message: String?): ResponseForm<Nothing> =
            ResponseForm(code, message, null)
    }
}