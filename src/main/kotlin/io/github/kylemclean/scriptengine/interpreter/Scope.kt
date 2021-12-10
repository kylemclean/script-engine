package io.github.kylemclean.scriptengine.interpreter

class Scope {
    private val symbols: MutableMap<String, Symbol> = mutableMapOf()

    fun hasSymbol(identifier: String): Boolean {
        return symbols.containsKey(identifier)
    }

    fun putSymbol(symbol: Symbol) {
        require(!hasSymbol(symbol.identifier)) { "symbol \"" + symbol.identifier + "\" already exists" }
        symbols[symbol.identifier] = symbol
    }

    fun getSymbolIfExists(identifier: String): Symbol? {
        return symbols[identifier]
    }
}