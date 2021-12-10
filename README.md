# script-engine
![CI](https://github.com/kylemclean/script-engine/actions/workflows/ci.yml/badge.svg)

This is an interpreter for a new scripting language.

The interpreter is written in Kotlin, so it can be integrated in any application written in a JVM language.

The goal of this project is to make it easier for Java and Kotlin applications to support runtime user scripting.

The lexer and parser for the language are generated from a [grammar file](src/main/antlr/io/github/kylemclean/scriptengine/ScriptEngine.g4) using ANTLR4.

This project is a work in progress.

## Features
- Arithmetic and logic operators
- `for` and `while` loops
- Integer, float, boolean, string, and function types
- Array and dictionary container types
- Range and slice operators for sequence types
- Built-in functions for working with containers (`length`, `map`, `filter`, `reduce`, `sum`, `min`, `max`, `contains`)
- An interactive language shell (REPL)

