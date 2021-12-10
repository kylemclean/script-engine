package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.interpreter.values.Value

interface AssignableExpression {
    fun assign(newValue: Value)
}