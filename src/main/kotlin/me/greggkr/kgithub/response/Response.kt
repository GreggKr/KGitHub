package me.greggkr.kgithub.response

class Response<out T>(val type: ResponseType, val data: T?)