package io.github.kylemclean.scriptengine.interpreter.values

import io.github.kylemclean.scriptengine.ast.ParamList

abstract class BooleanValue private constructor(
    val value: Boolean
) : Value(booleanClass) {
    companion object {
        val booleanClass: ClassValue

        init {
            val constructor = object : NativeFunctionValue(ParamList("value")) {
                override fun executeNativeFunction(arguments: List<Value>): Value {
                    if (arguments.size != 1)
                        throw IllegalArgumentException("wrong number of arguments")
                    return when (val value = arguments[0]) {
                        is BooleanValue -> value
                        is SizedValue -> of(value.length() > 0)
                        is IntegerValue -> of(value.value != 0)
                        is FloatValue -> of(value.value != 0F)
                        else -> throw IllegalArgumentException("value is not a BooleanValue, SizedValue, IntegerValue, or FloatValue")
                    }
                }
            }
            booleanClass = ClassValue("bool", null, mapOf("__ctor" to Field(constructor)))
        }

        @JvmStatic
        fun of(value: Boolean): BooleanValue = if (value) True else False
    }

    override fun str(): String = repr()

    override fun repr(): String = value.toString()

    object True : BooleanValue(true)

    object False : BooleanValue(false)
}
