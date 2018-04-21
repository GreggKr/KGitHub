package me.greggkr.kgithub.auth

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/**
 * Includes authentication
 */
class GitHubAuthenticator(private val oauthToken: String) : Authenticator {
    override fun authenticate(route: Route, response: Response): Request {
        return response
                .request()
                .newBuilder()
                .addHeader("Authorization", "token $oauthToken")
                .build()
    }
}
