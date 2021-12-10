package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.interpreter.values.DictionaryValue
import io.github.kylemclean.scriptengine.interpreter.values.StringValue
import io.github.kylemclean.scriptengine.interpreter.values.Value

class DictionaryLiteralExpression(private val map: Map<String, Expression>) : Expression() {
    override fun evaluate(): Value {
        val dictionaryValue = DictionaryValue()
        map.forEach { (key, valExpr) ->
            run {
                dictionaryValue.put(StringValue(key), valExpr.evaluate())
            }
        }
        return dictionaryValue
    }
}