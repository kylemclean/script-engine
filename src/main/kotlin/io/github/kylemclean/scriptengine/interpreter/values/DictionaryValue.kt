package io.github.kylemclean.scriptengine.interpreter.values

import io.github.kylemclean.scriptengine.ast.ParamList
import io.github.kylemclean.scriptengine.interpreter.Arguments

class DictionaryValue : Value(dictionaryClass), IterableValue, SubscriptableValue, SizedValue, ContainerValue {
    companion object {
        val dictionaryClass: ClassValue

        init {
            val constructor = object : NativeFunctionValue(ParamList()) {
                override fun executeNativeFunction(arguments: Arguments): Value {
                    arguments.requireSize(0)
                    return DictionaryValue()
                }
            }
            dictionaryClass = ClassValue("dict", null, mapOf("__ctor" to Field(constructor)))
        }
    }

    private val map: MutableMap<StringValue, Value> = mutableMapOf()

    fun get(key: StringValue): Value = map.getOrDefault(key, NullValue)

    fun put(key: StringValue, newValue: Value) {
        map[key] = newValue
    }

    override fun iterator(): Iterator<Value> = map.keys.iterator()

    override fun getAtSubscript(subscript: Value): Value {
        require(subscript is StringValue) { "subscript is not a StringValue" }
        return get(subscript)
    }

    override fun setAtSubscript(subscript: Value, newValue: Value) {
        require(subscript is StringValue) { "subscript is not a StringValue" }
        put(subscript, newValue)
    }

    override fun length(): Int = map.size

    override fun contains(needle: Value) = BooleanValue.of(needle is StringValue && map.containsKey(needle))

    override fun str(): String = repr()

    override fun repr(): String {
        if (map.isEmpty())
            return "{}"

        val sb = StringBuilder()
        sb.append('{')
        map.forEach { (key, value) ->
            run {
                sb.append('"')
                sb.append(key.str())
                sb.append("\": ")
                sb.append(value.repr())
                sb.append(", ")
            }
        }
        sb.deleteRange(sb.length - 2, sb.length)
        sb.append('}')
        return sb.toString()
    }
}
