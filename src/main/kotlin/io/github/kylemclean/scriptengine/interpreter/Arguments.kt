package io.github.kylemclean.scriptengine.interpreter

import io.github.kylemclean.scriptengine.interpreter.values.Value

class Arguments(val values: List<Value>): Iterable<Value> {
    operator fun get(index: Int): Value = values[index]

    fun requireSize(min: Int, max: Int = min) {
        if (max < min)
            throw IllegalArgumentException("max < min ($max < $min)")

        if (values.size < min || values.size > max)
            throw IllegalArgumentException(
                "wrong number of arguments " +
                        "(expected ${if (max != min) "$min-$max" else min}, got ${values.size})"
            )
    }

    override fun iterator(): Iterator<Value> = values.iterator()
}
