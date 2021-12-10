package io.github.kylemclean.scriptengine.interpreter.values

interface IndexableValue : SubscriptableValue {
    fun getAtIndex(index: IntegerValue): Value
    fun setAtIndex(index: IntegerValue, newValue: Value)
}