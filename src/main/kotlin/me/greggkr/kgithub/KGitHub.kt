package me.greggkr.kgithub

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import me.greggkr.kgithub.auth.GitHubAuthenticator
import me.greggkr.kgithub.auth.GitHubInterceptor
import me.greggkr.kgithub.exceptions.GitHubException
import me.greggkr.kgithub.response.Response
import me.greggkr.kgithub.response.ResponseType
import me.greggkr.kgithub.wrappers.Gist
import me.greggkr.kgithub.wrappers.Repository
import me.greggkr.kgithub.wrappers.User
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.*
import java.util.stream.Collectors

private const val BASE_URL = "https://api.github.com"
private const val BASE_USER_URL = "$BASE_URL/users"
private const val BASE_GIST_URL = "$BASE_URL/gists"

/**
 * All methods in the companion object do not require authentication.
 */
class KGitHub(oauthToken: String?) {
    companion object {
        private val client = OkHttpClient.Builder()
                .addInterceptor(GitHubInterceptor())
                .build()

        private val gson = GsonBuilder()
                .setPrettyPrinting()
                .create()

        /**
         * @param user the user to check repositories for
         * @return null if user does not exist, an Array of Repositories by the user
         */
        fun getRepositories(user: String): Response<Array<Repository>?> {
            val req = Request.Builder()
                    .url("$BASE_USER_URL/$user/repos?per_page=${getRepoCount(user) ?: 20}")
                    .get()
                    .build()

            val res = client.newCall(req).execute()

            val body = res.body() ?: return Response(ResponseType.NOT_FOUND, null)

            val str = body.string()

            val valRes = validate(str)

            if (valRes != ResponseType.OK) return Response(valRes, null)

            val repos = gson.fromJson(str, Array<Repository>::class.java)

            return Response(ResponseType.OK, repos)
        }

        fun getRepository(user: String, repo: String): Response<Repository?> {
            val repos = getRepositories(user)

            val r = Arrays.stream(repos.data)
                    .filter { it.name.equals(repo, true) }
                    .limit(1)
                    .collect(Collectors.toList())[0] ?: return Response(ResponseType.NOT_FOUND, null)

            return Response(ResponseType.OK, r)
        }

        fun getUser(user: String): Response<User?> {
            val req = Request.Builder()
                    .url("$BASE_USER_URL/$user")
                    .get()
                    .build()

            val res = client.newCall(req).execute()

            val body = res.body() ?: return Response(ResponseType.NOT_FOUND, null)

            val str = body.string()

            val valRes = validate(str)

            if (valRes != ResponseType.OK) return Response(valRes, null)

            val u = gson.fromJson(str, User::class.java)

            return Response(ResponseType.OK, u)
        }

        fun getGists(user: String): Response<Array<Gist>?> {
            val req = Request.Builder()
                    .url("$BASE_USER_URL/$user/gists")
                    .get()
                    .build()

            val res = client.newCall(req).execute()

            val body = res.body() ?: return Response(ResponseType.NOT_FOUND, null)

            val str = body.string()

            val valRes = validate(str)

            if (valRes != ResponseType.OK) return Response(valRes, null)

            val gists = gson.fromJson(str, Array<Gist>::class.java)

            return Response(ResponseType.OK, gists)
        }

        private fun getRepoCount(user: String): Int? {
            val response = getUser(user)

            if (response.type != ResponseType.OK) return null

            return response.data!!.publicRepos
        }

        /**
         * Checks the GitHub response to make sure it's valid
         *
         * @param json the json to check
         * @return true if valid response, false otherwise
         */
        private fun validate(json: String): ResponseType {
            val elem = gson.fromJson(json, JsonElement::class.java)

            if (elem.isJsonArray) return ResponseType.OK // If it's an array, it's always valid

            val obj = elem.asJsonObject

            if (obj.has("message")) {
                if (obj.get("message").asString != "Not found") return ResponseType.NOT_FOUND
            }

            return ResponseType.OK
        }
    }

    class File(val name: String, private val content: String) {
        fun toJsonObject(): JSONObject {
            return JSONObject()
                    .put("content", content)
        }
    }

    init {
        if (oauthToken.isNullOrEmpty()) throw GitHubException("oauthToken cannot be blank.")
    }

    private val client = OkHttpClient.Builder()
            .addInterceptor(GitHubInterceptor())
            .authenticator(GitHubAuthenticator(oauthToken!!))
            .build()

    fun createGist(files: List<File>, description: String = "", public: Boolean = false): Response<Gist?> {
        val filesObject = JSONObject()

        for (file in files) {
            filesObject.put(file.name, file.toJsonObject())
        }

        val req = Request.Builder()
                .url(BASE_GIST_URL)
                .post(RequestBody.create(null, JSONObject()
                        .put("description", description)
                        .put("public", public)
                        .put("files", filesObject)
                        .toString()))
                .build()

        val res = client.newCall(req).execute()

        val body = res.body() ?: return Response(ResponseType.NOT_FOUND, null)

        val str = body.string()

        val valRes = validate(str)

        if (valRes != ResponseType.OK) return Response(valRes, null)

        val gist = gson.fromJson(str, Gist::class.java)

        return Response(ResponseType.OK, gist)
    }

    private fun validate(json: String): ResponseType {
        val elem = gson.fromJson(json, JsonElement::class.java)

        if (elem.isJsonArray) return ResponseType.OK

        val obj = elem.asJsonObject

        if (obj.has("message")) {
            val msg = obj.get("message").asString

            if (msg == "Requires Authentication") return ResponseType.BAD_AUTHENTICATION
            if (msg == "Not found") return ResponseType.NOT_FOUND
        }

        return ResponseType.OK
    }
}
