package io.github.kylemclean.scriptengine.interpreter

import io.github.kylemclean.scriptengine.interpreter.values.FunctionValue
import io.github.kylemclean.scriptengine.interpreter.values.Value

class StackFrame(val function: FunctionValue, val arguments: List<Value>) {
    var returnValue: Value? = null
        set(value) {
            check(returnValue == null) { "returnValue is already set" }
            field = value
        }
}