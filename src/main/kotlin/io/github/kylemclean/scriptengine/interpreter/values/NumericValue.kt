package io.github.kylemclean.scriptengine.interpreter.values

abstract class NumericValue(`class`: ClassValue) : Value(`class`) {
    abstract fun asInt(): Int

    abstract fun asFloat(): Float

    abstract fun negation(): NumericValue
}