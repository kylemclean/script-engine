package io.github.kylemclean.scriptengine.interpreter.values

interface SliceableValue : SubscriptableValue {
    fun getSlice(slice: SliceValue): Value {
        // Default behavior that assumes this is an IndexableValue
        check(this is IndexableValue) { "this is not an IndexableValue" }
        val (startIndex, endIndex, step) = resolveSliceValues(slice)
        return ArrayValue((startIndex until endIndex step step).map { getAtIndex(IntegerValue(it)) })
    }

    fun setSlice(slice: SliceValue, newValues: Value) {
        // Default behavior that assumes this is an IndexableValue
        check(this is IndexableValue) { "this is not an IndexableValue" }
        check(newValues is IterableValue) { "newValues is not an IterableValue" }
        val (startIndex, endIndex, step) = resolveSliceValues(slice)
        (startIndex until endIndex step step).zip(newValues).forEach { setAtIndex(IntegerValue(it.first), it.second) }
    }
}

fun SliceableValue.resolveSliceValues(slice: SliceValue): Triple<Int, Int, Int> {
    val startIndex = when (slice.start) {
        is IntegerValue -> slice.start.value
        is NullValue -> 0
        else -> throw IllegalArgumentException("slice startIndex is not IntegerValue or NullValue")
    }
    val endIndex = when (slice.end) {
        is IntegerValue -> slice.end.value
        is NullValue -> {
            if (this is SizedValue)
                length()
            else
                throw IllegalStateException("cannot resolve slice endIndex because this is not a SizedValue")
        }
        else -> throw IllegalArgumentException("slice endIndex is not IntegerValue or NullValue")
    }
    val step = when (slice.step) {
        is IntegerValue -> slice.step.value
        is NullValue -> 1
        else -> throw IllegalArgumentException("slice step is not IntegerValue or NullValue")
    }
    return Triple(startIndex, endIndex, step)
}

