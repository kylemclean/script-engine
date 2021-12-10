package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.ast.ParamList
import io.github.kylemclean.scriptengine.ast.statements.BlockStatement
import io.github.kylemclean.scriptengine.interpreter.values.FunctionValue
import io.github.kylemclean.scriptengine.interpreter.values.ScriptFunctionValue

class FunctionExpression(body: BlockStatement, paramList: ParamList) : Expression() {
    private val cachedValue = ScriptFunctionValue(body, paramList)

    override fun evaluate(): FunctionValue = cachedValue
}