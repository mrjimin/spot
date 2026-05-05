package io.github.mrjimin.spot.global.exception

class SpotException(val errorCode: ErrorCode) : RuntimeException(errorCode.message)