package io.github.kylemclean.scriptengine.interpreter.values

import io.github.kylemclean.scriptengine.ast.ParamList

class IntegerValue(val value: Int) : NumericValue(integerClass) {
    companion object {
        val integerClass: ClassValue

        init {
            val constructor = object : NativeFunctionValue(ParamList("value")) {
                override fun executeNativeFunction(arguments: List<Value>): Value {
                    if (arguments.size != 1)
                        throw IllegalArgumentException("wrong number of arguments")
                    return when (val value = arguments[0]) {
                        is IntegerValue -> value
                        is FloatValue -> IntegerValue(value.asInt())
                        is StringValue -> IntegerValue(value.string.toInt())
                        else -> throw IllegalArgumentException("value is not an IntegerValue, FloatValue, or StringValue")
                    }
                }
            }
            integerClass = ClassValue("int", null, mapOf("__ctor" to Field(constructor)))
        }
    }

    override fun asInt(): Int {
        return value
    }

    override fun asFloat(): Float {
        return value.toFloat()
    }

    override fun negation() = IntegerValue(-value)

    override fun str(): String = repr()

    override fun repr(): String = value.toString()

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true

        if (other === null)
            return false

        if (other is IntegerValue) {
            return value == other.value
        }

        if (other is FloatValue) {
            return asFloat() == other.value
        }

        return false
    }

    override fun hashCode(): Int {
        return value
    }
}