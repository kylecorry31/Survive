plugins {
    kotlin("jvm")
    id("com.android.lint")
}

dependencies {
    compileOnly(libs.lint.api)
    compileOnly(libs.lint.checks)
}
