package io.github.kylemclean.scriptengine.interpreter.values

import io.github.kylemclean.scriptengine.ast.ParamList

class FloatValue(val value: Float) : NumericValue(floatClass) {
    companion object {
        val floatClass: ClassValue

        init {
            val constructor = object : NativeFunctionValue(ParamList("value")) {
                override fun executeNativeFunction(arguments: List<Value>): Value {
                    if (arguments.size != 1)
                        throw IllegalArgumentException("wrong number of arguments")
                    return when (val value = arguments[0]) {
                        is FloatValue -> value
                        is IntegerValue -> FloatValue(value.asFloat())
                        is StringValue -> FloatValue(value.string.toFloat())
                        else -> throw IllegalArgumentException("value is not a FloatValue, IntegerValue, or StringValue")
                    }
                }
            }
            floatClass = ClassValue("float", null, mapOf("__ctor" to Field(constructor)))
        }
    }

    override fun asInt(): Int {
        return value.toInt()
    }

    override fun asFloat(): Float {
        return value
    }

    override fun negation() = FloatValue(-value)

    override fun str(): String = repr()

    override fun repr(): String = value.toString()

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true

        if (other === null)
            return false

        if (other is IntegerValue) {
            return asInt() == other.value
        }

        if (other is FloatValue) {
            return value == other.value
        }

        return false
    }

    override fun hashCode(): Int {
        return asInt()
    }
}