plugins {
	java
	war
	id("jacoco")
	id("org.springframework.boot") version "3.5.12"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.msu.cmc"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.apache.tomcat.embed:tomcat-embed-jasper")
	implementation("jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api")
	compileOnly("org.projectlombok:lombok")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	runtimeOnly("org.glassfish.web:jakarta.servlet.jsp.jstl")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("com.h2database:h2")
	testImplementation("org.seleniumhq.selenium:htmlunit3-driver:4.43.0")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("org.testng:testng:7.10.2")
}

tasks.withType<Test> {
	useTestNG()
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)

	reports {
		xml.required.set(true)
		csv.required.set(false)
		html.required.set(true)
	}
}
