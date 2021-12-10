package io.github.kylemclean.scriptengine.interpreter.values


class ClassValue(
    private val name: String,
    private val superClass: ClassValue?,
    private val members: Map<String, Member>,
) : Value(null) {
    companion object {
        val classClass = ClassValue("class", null, emptyMap())
    }

    override val `class`: ClassValue
        get() = classClass

    val allMembers: Map<String, Member>
        get() {
            return if (superClass == null) {
                members
            } else {
                superClass.allMembers + members
            }
        }

    override fun str(): String = repr()

    override fun repr(): String = "<class '$name'>"

    override val callFunction: FunctionValue? =
        members["__ctor"]?.get() as FunctionValue?
}