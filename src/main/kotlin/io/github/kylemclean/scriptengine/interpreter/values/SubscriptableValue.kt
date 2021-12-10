package io.github.kylemclean.scriptengine.interpreter.values

interface SubscriptableValue {
    fun getAtSubscript(subscript: Value): Value {
        return if (this is IndexableValue && subscript is IntegerValue)
            getAtIndex(subscript)
        else if (this is SliceableValue && subscript is SliceValue)
            getSlice(subscript)
        else throw NotImplementedError("SubscriptableValue.getAt not implemented for ${javaClass.name}")
    }

    fun setAtSubscript(subscript: Value, newValue: Value) {
        if (this is IndexableValue && subscript is IntegerValue)
            getAtIndex(subscript)
        else if (this is SliceableValue && subscript is SliceValue)
            getSlice(subscript)
        else throw NotImplementedError("SubscriptableValue.setAt not implemented for ${javaClass.name}")
    }
}