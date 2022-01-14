package io.github.kylemclean.scriptengine.interpreter

import io.github.kylemclean.scriptengine.CodeParser
import io.github.kylemclean.scriptengine.ast.ParamList
import io.github.kylemclean.scriptengine.ast.statements.Statement
import io.github.kylemclean.scriptengine.interpreter.values.*
import java.util.*
import kotlin.system.exitProcess

class Interpreter private constructor() {
    companion object {
        @JvmStatic
        val interpreter: Interpreter = Interpreter()
    }

    private val scopes: Stack<Scope> = Stack()
    private val callStack: CallStack = CallStack()
    private var nextScope = Scope()

    internal var lastExpressionStatementValue: Value? = null
    internal var shouldBreak = false
    internal var shouldContinue = false

    internal val printFunction: FunctionValue
    internal val lengthFunction: FunctionValue
    internal val mapFunction: FunctionValue
    internal val filterFunction: FunctionValue
    internal val reduceFunction: FunctionValue
    internal val sumFunction: FunctionValue
    internal val minFunction: FunctionValue
    internal val maxFunction: FunctionValue
    internal val containsFunction: FunctionValue
    internal val exitFunction: FunctionValue
    internal val typeFunction: FunctionValue

    init {
        val globalScope = Scope()

        printFunction = object : NativeFunctionValue(ParamList("value")) {
            override fun executeNativeFunction(arguments: Arguments): Value {
                arguments.requireSize(1)
                println(arguments[0].str())
                return NullValue
            }
        }
        globalScope.putSymbol(Symbol("print", printFunction))

        lengthFunction = object : NativeFunctionValue(ParamList("value")) {
            override fun executeNativeFunction(arguments: Arguments): Value {
                arguments.requireSize(1)
                val value = arguments[0]
                require(value is SizedValue) { "value is not a SizedValue" }
                return IntegerValue(value.length())
            }
        }
        globalScope.putSymbol(Symbol("length", lengthFunction))

        mapFunction = object : NativeFunctionValue(ParamList("func", "iterable")) {
            override fun executeNativeFunction(arguments: Arguments): Value {
                arguments.requireSize(2)
                val (func, iterable) = arguments.values
                require(func is FunctionValue) { "func is not a FunctionValue" }
                require(iterable is IterableValue) { "iterable is not an IterableValue" }
                val mappedValues = iterable.map { call(func, Arguments(listOf(it))) }
                return ArrayValue(mappedValues)
            }
        }
        globalScope.putSymbol(Symbol("map", mapFunction))

        filterFunction = object : NativeFunctionValue(ParamList("predicate", "iterable")) {
            override fun executeNativeFunction(arguments: Arguments): Value {
                arguments.requireSize(2)
                val (predicate, iterable) = arguments.values
                require(predicate is FunctionValue) { "predicate is not a FunctionValue" }
                require(iterable is IterableValue) { "iterable is not an IterableValue" }
                val filteredValues = iterable.filter { (call(predicate, Arguments(listOf(it))) as BooleanValue).value }
                return ArrayValue(filteredValues)
            }
        }
        globalScope.putSymbol(Symbol("filter", filterFunction))

        reduceFunction = object : NativeFunctionValue(ParamList("func", "iterable", "initializer")) {
            override fun executeNativeFunction(arguments: Arguments): Value {
                arguments.requireSize(2, 3)
                val (func, iterable) = arguments.values
                val initializer: Value? = if (arguments.values.size == 3) arguments[2] else null
                require(func is FunctionValue) { "func is not a FunctionValue" }
                require(iterable is IterableValue) { "iterable is not an IterableValue" }

                val items = if (initializer == null)
                    iterable.toList()
                else
                    listOf(initializer) + iterable.toList()
                return items.reduce { acc, value -> call(func, Arguments(listOf(acc, value))) }
            }
        }
        globalScope.putSymbol(Symbol("reduce", reduceFunction))

        val addFunction = CodeParser().parseFunctionExpression(
            "(x, y) -> x + y"
        )!!.evaluate()
        sumFunction = object : NativeFunctionValue(ParamList("iterable")) {
            override fun executeNativeFunction(arguments: Arguments): Value {
                arguments.requireSize(1)
                val iterable = arguments[0]
                require(iterable is IterableValue) { "iterable is not an IterableValue" }
                return call(reduceFunction, Arguments(listOf(addFunction, iterable)))
            }
        }
        globalScope.putSymbol(Symbol("sum", sumFunction))

        val minTwoFunction = CodeParser().parseFunctionExpression(
            "(x, y) -> if (x < y) x else y }"
        )!!.evaluate()
        val maxTwoFunction = CodeParser().parseFunctionExpression(
            "(x, y) -> if (x > y) x else y }"
        )!!.evaluate()
        val makeMinMaxFunctionValue: (FunctionValue) -> FunctionValue = { reducer: FunctionValue ->
            object : NativeFunctionValue(ParamList("value", "y")) {
                override fun executeNativeFunction(arguments: Arguments): Value {
                    return when (arguments.values.size) {
                        1 -> {
                            val value = arguments[0]
                            require(value is IterableValue) { "value is not an IterableValue" }
                            require(value is SizedValue) { " value is not a SizedValue" }
                            val length = call(lengthFunction, Arguments(listOf(value))) as IntegerValue
                            return when (length.value) {
                                0 -> throw IllegalArgumentException("value is empty")
                                1 -> value.first()
                                else -> call(reduceFunction, Arguments(listOf(reducer, value)))
                            }
                        }
                        2 -> call(reducer, arguments)
                        else -> call(this, Arguments(listOf(ArrayValue(arguments.values))))
                    }
                }
            }
        }
        minFunction = makeMinMaxFunctionValue(minTwoFunction)
        globalScope.putSymbol(Symbol("min", minFunction))
        maxFunction = makeMinMaxFunctionValue(maxTwoFunction)
        globalScope.putSymbol(Symbol("max", maxFunction))

        containsFunction = object : NativeFunctionValue(ParamList("needle", "haystack")) {
            override fun executeNativeFunction(arguments: Arguments): Value {
                arguments.requireSize(2)
                val (needle, haystack) = arguments.values
                return when (haystack) {
                    is ContainerValue -> return haystack.contains(needle)
                    is IterableValue -> BooleanValue.of(haystack.any { it == needle })
                    else -> throw IllegalArgumentException("haystack is not a ContainerValue or IterableValue")
                }
            }
        }
        globalScope.putSymbol(Symbol("contains", containsFunction))

        exitFunction = object : NativeFunctionValue(ParamList("exitCode")) {
            override fun executeNativeFunction(arguments: Arguments): Value {
                arguments.requireSize(0, 1)
                val exitCode = if (arguments.values.size == 1) arguments[0] else IntegerValue(0)
                require(exitCode is IntegerValue) { "exitCode is not an IntegerValue" }
                exitProcess(exitCode.value)
            }

            override fun repr(): String {
                return "Type exit() to exit"
            }
        }
        globalScope.putSymbol(Symbol("exit", exitFunction))

        typeFunction = object : NativeFunctionValue(ParamList("value")) {
            override fun executeNativeFunction(arguments: Arguments): Value {
                arguments.requireSize(1)
                val value = arguments[0]
                return value.`class`
            }
        }
        globalScope.putSymbol(Symbol("type", typeFunction))

        globalScope.putSymbol(Symbol("array", ArrayValue.arrayClass))
        globalScope.putSymbol(Symbol("bool", BooleanValue.booleanClass))
        globalScope.putSymbol(Symbol("dict", DictionaryValue.dictionaryClass))
        globalScope.putSymbol(Symbol("float", FloatValue.floatClass))
        globalScope.putSymbol(Symbol("function", FunctionValue.functionClass))
        globalScope.putSymbol(Symbol("int", IntegerValue.integerClass))
        globalScope.putSymbol(Symbol("range", RangeValue.rangeClass))
        globalScope.putSymbol(Symbol("slice", SliceValue.sliceClass))
        globalScope.putSymbol(Symbol("str", StringValue.stringClass))

        scopes.push(globalScope)
    }

    fun executeScript(script: Script) {
        script.rootBlock.execute()
    }

    fun executeStatement(statement: Statement) {
        statement.execute()
    }

    private fun declareSymbolInScope(scope: Scope, identifier: String, value: Value) {
        check(!scope.hasSymbol(identifier)) { "symbol \"$identifier\" is already declared" }
        scope.putSymbol(Symbol(identifier, value))
    }

    fun declareSymbol(identifier: String, value: Value) {
        declareSymbolInScope(currentScope, identifier, value)
    }

    fun declareSymbolInNextScope(identifier: String, value: Value) {
        declareSymbolInScope(nextScope, identifier, value)
    }

    fun resolveSymbol(identifier: String): Symbol? {
        for (i in scopes.indices.reversed()) {
            val scope = scopes[i]
            val symbol = scope.getSymbolIfExists(identifier)
            if (symbol != null) {
                return symbol
            }
        }
        return null
    }

    private val currentScope: Scope
        get() = (scopes.peek())

    fun enterScope() {
        scopes.push(nextScope)
        nextScope = Scope()
    }

    fun exitScope() {
        scopes.pop()
    }

    fun call(function: FunctionValue, arguments: Arguments): Value {
        val stackFrame = StackFrame(function, arguments)
        callStack.push(stackFrame)

        function.paramList.parameterIdentifiers
            .dropLast(if (function.paramList.lastIsVarargs) 1 else 0)
            .zip(arguments)
            .forEach {
                declareSymbolInNextScope(it.first, it.second)
            }
        if (function.paramList.lastIsVarargs) {
            val varargs = arguments.drop(function.paramList.parameterIdentifiers.size - 1)
            declareSymbolInNextScope(function.paramList.parameterIdentifiers.last(), ArrayValue(varargs))
        }

        function.execute()

        callStack.pop()
        val returnValue = stackFrame.returnValue
        return returnValue ?: NullValue
    }

    val currentStackFrame: StackFrame?
        get() = callStack.peek()
}
