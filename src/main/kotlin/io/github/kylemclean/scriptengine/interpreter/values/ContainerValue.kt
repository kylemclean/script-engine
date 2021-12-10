package io.github.kylemclean.scriptengine.interpreter.values

interface ContainerValue {
    fun contains(needle: Value): BooleanValue
}