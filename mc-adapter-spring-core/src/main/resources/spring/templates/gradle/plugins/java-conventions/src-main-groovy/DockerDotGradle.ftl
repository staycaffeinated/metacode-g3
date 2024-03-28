plugins {
    id "com.google.cloud.tools.jib"
}

jib {
    from {
        image = 'amazoncorretto:17-alpine3.17-jdk'
    }
}