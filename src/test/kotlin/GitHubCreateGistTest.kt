import me.greggkr.kgithub.KGitHub
import me.greggkr.kgithub.response.ResponseType
import java.io.File

/**
 * Note that token will not work
 */
fun main(args: Array<String>) {
    val token = File("token.txt").readText()
fun woah() {

}

    val files = listOf(
            KGitHub.File("testfile.txt", "hello, how are you?"),
            KGitHub.File("yeet.kt", "fun woah() {\n\n}")
    )

    val gist = KGitHub(token).createGist(files, "test desc")

    if (gist.type != ResponseType.OK) {
        println(gist.type)
        return
    }

    println(gist.data)
}