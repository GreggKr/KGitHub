package me.greggkr.kgithub.auth

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class GitHubAuthenticator : Authenticator {
    override fun authenticate(route: Route, response: Response): Request {
        return response
                .request()
                .newBuilder()
                .addHeader("Accept", "application/vnd.github.v3+json")
                .build()
    }
}
