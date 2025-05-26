package com.yyy.theme

enum class ThemeOption {
    LIGHT,
    DARK,
    SYSTEM
}

enum class LangOption(val code: String) {
    EN("en"),
    TR("tr"),
    EMPTY("");

    companion object {
        fun fromCode(code: String?): LangOption {
            return LangOption.entries.find { it.code.equals(code, ignoreCase = true) } ?: EMPTY
        }
    }
}