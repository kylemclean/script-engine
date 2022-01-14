package io.github.kylemclean.scriptengine.ast.statements

import io.github.kylemclean.scriptengine.ast.ParamList
import io.github.kylemclean.scriptengine.interpreter.Interpreter.Companion.interpreter
import io.github.kylemclean.scriptengine.interpreter.values.ScriptFunctionValue

class FunctionDefinitionStatement(
    private val identifier: String,
    private val body: BlockStatement,
    private val paramList: ParamList
) : Statement() {
    override fun execute() {
        interpreter.declareSymbol(identifier, ScriptFunctionValue(body, paramList))
    }
}
