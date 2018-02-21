package com.vatcore.graphdb.json

class Request[T](
    val code: Int,
    val user: String,
    val password: String,
    val database: String,
    val data: T
)