package com.adrien.kmp_multiplatform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform