package com.x2mobile.wodjar.data.model.base

interface Filterable {
    fun matches(query: String): Boolean
}