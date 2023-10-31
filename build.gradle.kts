import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key).toString()


plugins {
    // Java support
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    // Gradle IntelliJ Plugin
    id("org.jetbrains.intellij") version "1.13.3"
    // Gradle Changelog Plugin
    id("org.jetbrains.changelog") version "1.3.1"
}



group = properties("pluginGroup")
version = properties("pluginVersion")

// Configure project's dependencies
repositories {
    mavenLocal()
    maven {
        setUrl("https://maven.aliyun.com/nexus/content/groups/public/")
    }
    mavenCentral()
}
dependencies {
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    implementation("com.github.jsonzou:jmockdata:4.3.0")
    implementation("org.freemarker:freemarker:2.3.31")
    implementation("cn.hutool:hutool-json:5.8.20")
    implementation("cn.hutool:hutool-http:5.8.20")
    implementation("cn.hutool:hutool-crypto:5.8.20")
    implementation("cn.hutool:hutool-system:5.8.20")
    implementation("cn.hutool:hutool-script:5.8.20")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("com.atlassian.commonmark:commonmark:0.17.0")
    implementation("com.wdf.api:fu-api-commons:${properties["ideaVersion"]}.${properties["fudocVersion"]}")
//    implementation("com.wdf.api:fu-api-start:${properties["ideaVersion"]}.${properties["fudocVersion"]}")
}


// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))
    updateSinceUntilBuild.set(false)

    //沙箱目录位置，用于保存IDEA的设置，默认在build文件下面，防止clean，放在根目录下。
    sandboxDir.set("${rootProject.rootDir}/idea-sandbox")

    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
}


// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    version.set(properties("pluginVersion"))
    groups.set(emptyList())
}


tasks {
    // Set the JVM compatibility versions
    properties("javaVersion").let {
        withType<JavaCompile> {
            sourceCompatibility = it
            targetCompatibility = it
            options.encoding = "UTF-8"
        }
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = it
        }
    }

    wrapper {
        gradleVersion = properties("gradleVersion")
    }

    patchPluginXml {

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription.set(
            projectDir.resolve("pluginDescription.md").readText().lines().run {
                val start = "<!-- Plugin description -->"
                val end = "<!-- Plugin description end -->"

                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end))
            }.joinToString("\n").run { markdownToHTML(this) }
        )

        changeNotes.set(
            projectDir.resolve("changeNotes.md").readText().lines().run {
                val start = "<!-- Plugin description -->"
                val end = "<!-- Plugin description end -->"

                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end))
            }.joinToString("\n").run { markdownToHTML(this) }
        )
    }
}
