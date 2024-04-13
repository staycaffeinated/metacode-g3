version = layout.projectDirectory.parentOrRoot().file("version.txt").asFile.let {
    // Note: it is important to handle the file-not-exists case as this code is executed during script compilation (on an artificial project)
    if (it.exists()) it.readLines().first().trim() else "unknown"
}

/*
 * Ascend the directory tree until the directory containing the 'version.txt' file is found; 
 * return that directory.
 */
fun Directory.parentOrRoot(): Directory = if (this.file("version.txt").asFile.exists()) {
    this
} else {
    val parent = dir("..")
    when {
        parent.file("version.txt").asFile.exists() -> parent
        this == parent -> parent
        else -> parent.parentOrRoot()
    }
}

