package com.x2mobile.wodjar.business.network.exception

open class ServerException(val code: Int, val errors: List<String>?) : RuntimeException()