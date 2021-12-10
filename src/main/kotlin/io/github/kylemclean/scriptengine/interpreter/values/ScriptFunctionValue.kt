package io.github.kylemclean.scriptengine.interpreter.values

import io.github.kylemclean.scriptengine.ast.ParamList
import io.github.kylemclean.scriptengine.ast.statements.BlockStatement

class ScriptFunctionValue(private val body: BlockStatement, paramList: ParamList) :
    FunctionValue(paramList) {

    override fun execute() {
        body.execute()
    }

    override fun str(): String = repr()

    override fun repr(): String = "<function>"
}
