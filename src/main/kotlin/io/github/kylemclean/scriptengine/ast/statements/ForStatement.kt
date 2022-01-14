package io.github.kylemclean.scriptengine.ast.statements

import io.github.kylemclean.scriptengine.ast.expressions.Expression
import io.github.kylemclean.scriptengine.interpreter.Interpreter.Companion.interpreter
import io.github.kylemclean.scriptengine.interpreter.values.IterableValue

class ForStatement(
    private val variableIdentifier: String,
    private val domainExpression: Expression,
    private val bodyStatement: BlockStatement
) : Statement() {
    override fun execute() {
        val domain = domainExpression.evaluate()
        require(domain is IterableValue) { "domain is not iterable" }
        for (element in domain) {
            interpreter.declareSymbolInNextScope(variableIdentifier, element)
            bodyStatement.execute()
            if (interpreter.shouldContinue) {
                interpreter.shouldContinue = false
            } else if (interpreter.shouldBreak) {
                interpreter.shouldBreak = false
                break
            }
        }
    }
}
