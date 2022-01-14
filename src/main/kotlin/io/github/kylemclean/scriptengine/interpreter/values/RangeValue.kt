package io.github.kylemclean.scriptengine.interpreter.values

import io.github.kylemclean.scriptengine.ast.ParamList
import io.github.kylemclean.scriptengine.interpreter.Arguments

class RangeValue(private val start: Int, private val end: Int, private val step: Int) : Value(rangeClass),
    IterableValue, SizedValue,
    IndexableValue, SliceableValue, ContainerValue {
    companion object {
        val rangeClass: ClassValue

        init {
            val constructor = object : NativeFunctionValue(ParamList("minInclusive", "maxExclusive", "step")) {
                override fun executeNativeFunction(arguments: Arguments): Value {
                    arguments.requireSize(1, 3)
                    val minInclusive = if (arguments.values.size == 1) {
                        0
                    } else {
                        val arg = arguments[0]
                        require(arg is IntegerValue) { "minInclusive is not an IntegerValue" }
                        arg.value
                    }
                    val maxExclusive = run {
                        val arg = if (arguments.values.size == 1)
                            arguments[0]
                        else
                            arguments[1]
                        require(arg is IntegerValue) { "maxExclusive is not an IntegerValue" }
                        arg.value
                    }
                    val step = if (arguments.values.size < 3) {
                        1
                    } else {
                        val arg = arguments[2]
                        require(arg is IntegerValue) { "step is not an IntegerValue" }
                        arg.value
                    }
                    return RangeValue(minInclusive, maxExclusive, step)
                }
            }
            rangeClass = ClassValue("range", null, mapOf("__ctor" to Field(constructor)))
        }
    }

    init {
        if (step == 0) {
            throw IllegalArgumentException("range step cannot be 0")
        }
    }

    private fun at(index: Int): Int? {
        if (index < 0 || index >= length())
            return null
        return start + index * step
    }

    override fun iterator(): Iterator<Value> {
        return object : Iterator<Value> {
            private var next = start

            override fun hasNext(): Boolean {
                return next < end
            }

            override fun next(): Value {
                val value = IntegerValue(next)
                next += step
                return value
            }
        }
    }

    override fun length(): Int = ((end - start) / step).coerceAtLeast(0)

    override fun getAtIndex(index: IntegerValue): IntegerValue {
        val value = at(index.value)
        if (value == null)
            throw IndexOutOfBoundsException("index out of bounds of range")
        else
            return IntegerValue(value)
    }

    override fun setAtIndex(index: IntegerValue, newValue: Value) {
        throw UnsupportedOperationException("cannot assign to subscript of range")
    }

    override fun getSlice(slice: SliceValue): RangeValue {
        val (sliceStart, sliceEnd, sliceStep) = resolveSliceValues(slice)

        if (length() == 0)
            return RangeValue(start, end, step)

        return RangeValue(
            at(sliceStart.coerceIn(0, length() - 1))!!,
            at(sliceEnd.coerceIn(0, length() - 1))!!,
            step * sliceStep)
    }

    override fun setSlice(slice: SliceValue, newValues: Value) {
        throw UnsupportedOperationException("cannot assign to slice of range")
    }

    override fun contains(needle: Value) = BooleanValue.of(
        needle is IntegerValue
                && (needle.value - start) % step == 0
                && ((step > 0 && needle.value >= start && needle.value < end)
                || (step < 0 && needle.value <= start && needle.value > end)))

    override fun str(): String = repr()

    override fun repr(): String =
        if (step == 1)
            "range($start, $end)"
        else
            "range($start, $end, $step)"
}
