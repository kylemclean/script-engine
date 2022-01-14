package io.github.kylemclean.scriptengine.interpreter.values

import io.github.kylemclean.scriptengine.ast.ParamList
import io.github.kylemclean.scriptengine.interpreter.Arguments
import io.github.kylemclean.scriptengine.interpreter.Interpreter.Companion.interpreter

abstract class NativeFunctionValue(paramList: ParamList): FunctionValue(paramList) {
    final override fun execute() {
        interpreter.enterScope()
        interpreter.currentStackFrame!!.returnValue = executeNativeFunction(interpreter.currentStackFrame!!.arguments)
        interpreter.exitScope()
    }

    abstract fun executeNativeFunction(arguments: Arguments): Value

    override fun str(): String = repr()

    override fun repr(): String = "<native function>"
}
