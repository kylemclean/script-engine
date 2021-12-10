package io.github.kylemclean.scriptengine.ast.statements

import io.github.kylemclean.scriptengine.ast.ASTNode
import io.github.kylemclean.scriptengine.interpreter.Interpreter

abstract class Statement : ASTNode() {
    abstract fun execute()
}