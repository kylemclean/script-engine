package io.github.kylemclean.scriptengine.interpreter.values

import io.github.kylemclean.scriptengine.ast.ParamList
import io.github.kylemclean.scriptengine.interpreter.Arguments

class ArrayValue(elements: List<Value> = emptyList()) : Value(arrayClass),
    IterableValue, IndexableValue, SliceableValue, SizedValue {
    companion object {
        val arrayClass: ClassValue

        init {
            val constructor = object : NativeFunctionValue(ParamList("iterable")) {
                override fun executeNativeFunction(arguments: Arguments): ArrayValue {
                    arguments.requireSize(1)
                    val iterable = arguments[0]
                    require(iterable is IterableValue) { "iterable is not an IterableValue" }
                    return ArrayValue(iterable.toList())
                }
            }
            arrayClass = ClassValue("array", null, mapOf("__ctor" to Field(constructor)))
        }
    }

    private val elements = elements.toMutableList()

    override fun iterator(): Iterator<Value> {
        return elements.iterator()
    }

    override fun getAtIndex(index: IntegerValue): Value {
        return elements[index.value]
    }

    override fun setAtIndex(index: IntegerValue, newValue: Value) {
        elements[index.value] = newValue
    }

    override fun length(): Int = elements.size

    override fun str(): String = repr()

    override fun repr(): String {
        val sb = StringBuilder()
        sb.append('[')
        forEachIndexed { index, value ->
            run {
                sb.append(value.repr())
                if (index < elements.size - 1) {
                    sb.append(", ")
                }
            }
        }
        sb.append(']')
        return sb.toString()
    }
}
