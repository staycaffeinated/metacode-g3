plugins {
    id "java-platform"
}

group = "buildlogic.platform"

// Allow dependencies to other platforms (e.g., BOMs)
javaPlatform.allowDependencies()