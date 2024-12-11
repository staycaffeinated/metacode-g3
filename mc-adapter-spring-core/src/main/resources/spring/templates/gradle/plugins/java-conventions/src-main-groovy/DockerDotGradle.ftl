plugins {
    id "com.google.cloud.tools.jib"
}

jib {
    from {
        image = 'amazoncorretto:17-al2023-jdk'
    }
}