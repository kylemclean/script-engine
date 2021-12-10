package io.github.kylemclean.scriptengine.ast

data class ParamList(val parameterIdentifiers: List<String>, val lastIsVarargs: Boolean) {
    constructor(vararg parameterIdentifiers: String):
        this(parameterIdentifiers.toList(), false)
}