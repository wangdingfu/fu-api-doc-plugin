import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key).toString()


plugins {
    // Java support
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version "2.0.21"
    // Gradle IntelliJ Platform Plugin 2.x - using 2.0.1 to avoid runIde bug in 2.1.0
    id("org.jetbrains.intellij.platform") version "2.10.4"
    // Gradle Changelog Plugin
    id("org.jetbrains.changelog") version "2.2.1"
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

    // IntelliJ Platform repositories
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    implementation("com.github.jsonzou:jmockdata:4.3.0")
    implementation("org.freemarker:freemarker:2.3.31")
    implementation("cn.hutool:hutool-json:5.8.20")
    implementation("cn.hutool:hutool-http:5.8.20")
    implementation("cn.hutool:hutool-crypto:5.8.20")
    implementation("cn.hutool:hutool-system:5.8.20")
    implementation("cn.hutool:hutool-script:5.8.20")
    implementation("org.apache.commons:commons-lang3:3.10")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("com.atlassian.commonmark:commonmark:0.17.0")
    implementation("cn.fudoc:fu-api-commons:222.${properties["fudocVersion"]}")

    // IntelliJ Platform dependencies
    intellijPlatform {
        create(properties("platformType"), properties("platformVersion"))

        // Bundled plugins (plugins that come with IntelliJ)
        bundledPlugins(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))

        // IntelliJ Platform instrumentation
        instrumentationTools()

        // Test Framework
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
    }
}

// Configure IntelliJ Platform Gradle Plugin - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
intellijPlatform {
    pluginConfiguration {
        name.set(properties("pluginName"))
    }

    // 沙箱目录位置,用于保存 IDEA 的设置,默认在 build 文件下面,防止 clean,放在根目录下。
    sandboxContainer.set(file("${rootProject.rootDir}/idea-sandbox"))
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
            compilerOptions {
                jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(it))
            }
        }
    }

    wrapper {
        gradleVersion = properties("gradleVersion")
    }

    // Configure runIde task to avoid IndexOutOfBoundsException
    runIde {
        maxHeapSize = "2g"
        jvmArgs = listOf(
            "-Xms512m"
        )
    }

    patchPluginXml {
        sinceBuild.set("251")
        untilBuild.set("")

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
