package com.kylecorry.trail_sense

class RustBridge {
    companion object {
        init {
            System.loadLibrary("rust_module")
        }
    }
    external fun hello(input: String): String
}