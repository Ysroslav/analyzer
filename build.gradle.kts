plugins {
	kotlin("jvm") version "2.3.21"
	kotlin("plugin.spring") version "2.3.21"
	id("org.springframework.boot") version "4.1.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("jacoco")
}

group = "ru.bodrov"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("com.jsoizo:kotlin-csv:1.10.0")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

jacoco {
	toolVersion = "0.8.13"
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport)
}

val jacocoExclusions = listOf(
	"**/AnalyzerApplication.*"
)

tasks.jacocoTestReport {
	dependsOn(tasks.test)

	classDirectories.setFrom(
		files(
			classDirectories.files.map {
				fileTree(it) {
					exclude(jacocoExclusions)
				}
			}
		)
	)

	reports {
		xml.required.set(true)
		html.required.set(true)
	}
}

tasks.jacocoTestCoverageVerification {
	dependsOn(tasks.test)

	classDirectories.setFrom(
		files(
			classDirectories.files.map {
				fileTree(it) {
					exclude(jacocoExclusions)
				}
			}
		)
	)

	violationRules {
		rule {
			limit {
				minimum = "0.80".toBigDecimal()
			}
		}
	}
}

tasks.check {
	dependsOn(tasks.jacocoTestCoverageVerification)
}
