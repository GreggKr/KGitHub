import me.greggkr.kgithub.KGitHub

fun main(args: Array<String>) {
    val repo = KGitHub.getRepository("GreggKr", "KGitHub")

    if (repo == null) {
        println("repo == null")
        return
    }

    println("${repo.createdAt} : ${repo.defaultBranch} : ${repo.language}")
}
