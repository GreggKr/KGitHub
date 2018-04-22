import me.greggkr.kgithub.KGitHub
import java.util.*

fun main(args: Array<String>) {
    val gists = KGitHub.getGists("GreggKr")

    if (gists.data == null) {
        println("invalid return. reason: ${gists.type.name}")
        return
    }

    println(Arrays.toString(gists.data))
}
