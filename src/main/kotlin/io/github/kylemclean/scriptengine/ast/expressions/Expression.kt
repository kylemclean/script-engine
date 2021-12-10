package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.ast.ASTNode
import io.github.kylemclean.scriptengine.interpreter.Interpreter
import io.github.kylemclean.scriptengine.interpreter.values.Value

abstract class Expression : ASTNode() {
    abstract fun evaluate(): Value
}