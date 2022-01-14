package io.github.kylemclean.scriptengine.interpreter.values

import io.github.kylemclean.scriptengine.ast.ParamList
import io.github.kylemclean.scriptengine.interpreter.Arguments

class SliceValue(
    val start: Value,
    val end: Value,
    val step: Value
) : Value(sliceClass) {
    companion object {
        val sliceClass: ClassValue

        init {
            val constructor = object : NativeFunctionValue(ParamList("min", "max", "step")) {
                override fun executeNativeFunction(arguments: Arguments): Value {
                    arguments.requireSize(2, 3)
                    val min = arguments[0]
                    val max = arguments[1]
                    val step = if (arguments.values.size == 3) arguments[2] else IntegerValue(1)
                    require(min is IntegerValue) { "min is not an IntegerValue" }
                    require(max is IntegerValue) { "max is not an IntegerValue" }
                    require(step is IntegerValue) { "step is not an IntegerValue" }
                    return SliceValue(min, max, step)
                }
            }
            sliceClass = ClassValue("slice", null, mapOf("__ctor" to Field(constructor)))
        }
    }

    override fun str(): String = repr()

    override fun repr(): String = "slice(${start.repr()}, ${end.repr()}, ${step.repr()})"
}
