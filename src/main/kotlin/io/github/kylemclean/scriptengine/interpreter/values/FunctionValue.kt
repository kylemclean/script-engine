package io.github.kylemclean.scriptengine.interpreter.values

import io.github.kylemclean.scriptengine.ast.ParamList
import io.github.kylemclean.scriptengine.interpreter.Arguments

abstract class FunctionValue(val paramList: ParamList) : Value(functionClass) {
    companion object {
        val functionClass: ClassValue

        init {
            val constructor = object : NativeFunctionValue(ParamList("code")) {
                override fun executeNativeFunction(arguments: Arguments): Value {
                    arguments.requireSize(1)
                    return when (val value = arguments[0]) {
                        is FunctionValue -> value
                        is StringValue ->
                            io.github.kylemclean.scriptengine.CodeParser().parseFunctionExpression(value.string)!!
                                .evaluate()
                        else -> throw IllegalArgumentException("code is not a StringValue")
                    }
                }
            }
            functionClass = ClassValue("function", null, mapOf("__ctor" to Field(constructor)))
        }
    }

    abstract fun execute()

    override val callFunction: FunctionValue?
        get() = this
}
