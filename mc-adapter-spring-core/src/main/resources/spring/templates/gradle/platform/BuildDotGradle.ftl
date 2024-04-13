plugins {
id "java-platform"
}

group = "org.example.platform"

// Allow dependencies to other platforms (e.g., BOMs)
javaPlatform.allowDependencies()