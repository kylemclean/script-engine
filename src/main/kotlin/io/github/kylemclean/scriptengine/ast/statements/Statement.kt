package io.github.kylemclean.scriptengine.ast.statements

import io.github.kylemclean.scriptengine.ast.ASTNode

abstract class Statement : ASTNode() {
    abstract fun execute()
}
