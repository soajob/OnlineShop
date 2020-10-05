package com.sostrovsky.onlineshop.datasource

interface DataSource<T> {
    suspend fun getData() : T?
}