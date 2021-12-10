package io.github.kylemclean.scriptengine.interpreter.values

import io.github.kylemclean.scriptengine.ast.ParamList

class StringValue(val string: String) : Value(stringClass), IterableValue, IndexableValue, SliceableValue, SizedValue,
    ContainerValue {
    companion object {
        val stringClass: ClassValue

        init {
            val constructor = object : NativeFunctionValue(ParamList("value")) {
                override fun executeNativeFunction(arguments: List<Value>): Value {
                    if (arguments.size != 1)
                        throw IllegalArgumentException("wrong number of arguments")
                    return when (val value = arguments[0]) {
                        is StringValue -> value
                        else -> StringValue(arguments[0].str())
                    }
                }
            }
            stringClass = ClassValue("string", null, mapOf("__ctor" to Field(constructor)))
        }
    }

    init {
        setMember("split", Field(object : NativeFunctionValue(ParamList("delimiter")) {
            override fun executeNativeFunction(arguments: List<Value>): Value {
                if (arguments.size > 1)
                    throw IllegalArgumentException("wrong number of arguments")
                val delimiter = if (arguments.size == 1) {
                    val arg = arguments[0]
                    require(arg is StringValue) { "delimiter is not a StringValue" }
                    arg.string
                } else {
                    " "
                }
                return ArrayValue(string.split(delimiter).map { StringValue(it) })
            }
        }))
    }

    override fun iterator(): Iterator<Value> {
        return object : Iterator<Value> {
            private var index = 0

            override fun hasNext(): Boolean = index < string.length

            override fun next(): Value = StringValue(string[index++].toString())
        }
    }

    override fun getAtIndex(index: IntegerValue) = StringValue(string[index.value].toString())

    override fun setAtIndex(index: IntegerValue, newValue: Value) {
        throw UnsupportedOperationException("cannot assign to index of string")
    }

    override fun getSlice(slice: SliceValue): StringValue {
        val (min, max, step) = resolveSliceValues(slice)
        return StringValue(string.slice(min until max step step))
    }

    override fun setSlice(slice: SliceValue, newValues: Value) {
        throw UnsupportedOperationException("cannot assign to slice of string")
    }

    override fun length(): Int = string.length

    override fun contains(needle: Value): BooleanValue {
        require(needle is StringValue) { "needle is not a StringValue" }
        return BooleanValue.of(string.indexOf(needle.string) != -1)
    }

    override fun str(): String = string

    override fun repr(): String = "\"$string\""

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StringValue

        return string == other.string
    }

    override fun hashCode(): Int {
        return string.hashCode()
    }
}