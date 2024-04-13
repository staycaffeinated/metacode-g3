plugins {
id "org.sonarqube"
id "jacoco"
}

def ignoredClasses =
"**/Exception.java," +
"**/*Test*.java," +
"**/*IT.java," +
"**/ResourceIdTrait.java," +
"**/ResourceIdentity.java," +
"**/*Application.java," +
"**/*TablePopulator.java," +
"**/*Config.java," +
"**/*Configuration.java," +
"**/*Initializer.java," +
"**/*Exception.java"

sonarqube {
properties {
property("sonar.coverage.exclusions", ignoredClasses)
}
}
