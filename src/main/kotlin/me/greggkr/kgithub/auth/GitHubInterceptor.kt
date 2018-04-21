package me.greggkr.kgithub.auth

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Does not include authentication
 */
class GitHubInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain
                .proceed(chain
                        .request()
                        .newBuilder()
                        .addHeader("Accept", "application/vnd.github.v3+json")
                        .build())
    }
}