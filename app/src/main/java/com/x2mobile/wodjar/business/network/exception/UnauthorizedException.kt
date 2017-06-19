package com.x2mobile.wodjar.business.network.exception

class UnauthorizedException(code: Int, errors: List<String>?) : ServerException(code, errors)