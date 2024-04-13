/**
* These are the plugins that need to be present in the root directory's
* build.gradle file to enable support for multimodule projects. With this
* file in place, it becomes easier to add additional modules to this project.
* For example, to add a module named, say, "core-library", the steps are:
* 1) Create a directory named "core-library"
* 2) Update settings.gradle and add the line: include "core-library"
* 3) Create the core-library/build.gradle file. That build file might look like:
* <code>
    * plugins {
    * id 'org.example.library-conventions'
    * alias(libs.plugins.versions)
    * alias(libs.plugins.lombok.plugin)
    * }
    * dependencies {
    * implementation libs.guava // for example
    * }
    * </code>
**/

plugins {
alias(libs.plugins.nebula.lint)
alias(libs.plugins.sonar)
alias(libs.plugins.lombok.plugin)
id "org.example.subproject-configurations"
}
