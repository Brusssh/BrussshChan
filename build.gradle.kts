import org.gradle.api.tasks.testing.logging.TestExceptionFormat

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val jda_version: String by project
val lavaplayer_version: String by project
val junit_version: String by project
val gson_version: String by project

plugins {
    application
    kotlin("jvm") version "1.4.10"
    jacoco
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    id("org.jlleitschuh.gradle.ktlint-idea") version "10.0.0"
}

group = "jp.brusssh.discord.bot"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("net.dv8tion:JDA:$jda_version")
    implementation("com.sedmelluq:lavaplayer:$lavaplayer_version")
    implementation("com.google.code.gson:gson:$gson_version")
    testImplementation("org.junit.jupiter:junit-jupiter:$junit_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit_version")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit_version")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    jar {
        manifest {
            attributes["Main-Class"] = "jp.brusssh.discord.bot.ApplicationKt"
        }
        from(configurations.compileClasspath.get().filter { !it.name.endsWith("pom") }.map { if (it.isDirectory) it else zipTree(it) })

        exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA")
    }
    test {
        finalizedBy(jacocoTestReport)
        useJUnitPlatform {
            includeEngines("junit-jupiter")
        }

        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events("passed", "failed", "skipped")
        }
    }
    jacocoTestReport {
        reports {
            xml.isEnabled = true
            csv.isEnabled = true
            html.isEnabled = true
        }
    }
}
