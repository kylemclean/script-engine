package io.github.kylemclean.scriptengine.interpreter.values

import io.github.kylemclean.scriptengine.interpreter.Arguments
import io.github.kylemclean.scriptengine.interpreter.Interpreter.Companion.interpreter

abstract class Value(`class`: ClassValue?) {
    private val _class: ClassValue? = `class`
    open val `class`: ClassValue
        get() = _class!!

    private val members: MutableMap<String, Member> = mutableMapOf()

    init {
        _class?.allMembers?.forEach {
            setMember(it.key, it.value)
        }
    }

    abstract fun str(): String
    abstract fun repr(): String

    interface Member {
        fun get(): Value
        fun set(newValue: Value)
    }

    class Field(private var value: Value) : Member {
        override fun get(): Value = value

        override fun set(newValue: Value) {
            this.value = newValue
        }
    }

    class Property(private val getter: FunctionValue, private val setter: FunctionValue) : Member {
        override fun get(): Value = interpreter.call(getter, Arguments(emptyList()))

        override fun set(newValue: Value) {
            interpreter.call(setter, Arguments(listOf(newValue)))
        }
    }

    fun getMemberValue(identifier: String): Value =
        members[identifier]?.get() ?: throw NoSuchElementException("no member named \"$identifier\"")

    fun setMemberValue(identifier: String, value: Value) {
        members[identifier]?.set(value) ?: throw NoSuchElementException("no member named \"$identifier\"")
    }

    fun setMember(identifier: String, member: Member) {
        members[identifier] = member
    }

    open val callFunction: FunctionValue? =
        members["__call"]?.get() as FunctionValue?
}
