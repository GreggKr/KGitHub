import me.greggkr.kgithub.KGitHub

fun main(args: Array<String>) {
    val repo = KGitHub.getRepository("GreggKr", "KGitHub")

    if (repo.data == null) {
        println("invalid return. reason: ${repo.type.name}")
        return
    }

    println(repo.data)
}
