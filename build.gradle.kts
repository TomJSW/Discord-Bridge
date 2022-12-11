plugins {
	java
	id("com.github.johnrengelman.shadow") version "7.0.0"
	id("io.freefair.lombok") version "6.5.0.3"
}

val projectName = "Discord-Bridge"

group = "net.cookietom"
version = "1.0-SNAPSHOT"
description = projectName
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
	maven("https://papermc.io/repo/repository/maven-public/")
	maven("https://oss.sonatype.org/content/groups/public/")
	maven("https://jitpack.io")
	maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
	maven("https://repo.totallyavir.us/maven-public")
	maven("https://repo.spring.io/libs-release/")
}

dependencies {
	compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
	implementation("club.minnced:discord-webhooks:0.8.2")
	implementation("net.dv8tion:JDA:5.0.0-beta.2") {
		exclude("opus-java")
	}
	compileOnly("org.apache.commons:commons-lang3:3.11")
}

tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
}

tasks {
	shadowJar {
		archiveFileName.set("$projectName.jar")
		mergeServiceFiles()
	}
}

tasks {
	build {
		dependsOn(shadowJar)
	}
}