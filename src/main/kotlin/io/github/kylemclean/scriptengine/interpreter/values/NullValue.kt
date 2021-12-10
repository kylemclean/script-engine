package io.github.kylemclean.scriptengine.interpreter.values

object NullValue : Value(ClassValue("null", null, emptyMap())) {
    override fun str(): String = repr()

    override fun repr(): String = "null"
}