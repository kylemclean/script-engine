package io.github.kylemclean.scriptengine

import io.github.kylemclean.scriptengine.ast.statements.ExpressionStatement
import io.github.kylemclean.scriptengine.interpreter.Interpreter.Companion.interpreter
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import org.jline.reader.impl.DefaultParser
import org.jline.terminal.TerminalBuilder
import java.io.IOException
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val parser = io.github.kylemclean.scriptengine.CodeParser()

    if (args.isEmpty()) {
        val terminalBuilder = TerminalBuilder.builder()
        val terminal = terminalBuilder.build()
        val commandParser = DefaultParser()
        commandParser.setEofOnUnclosedBracket(DefaultParser.Bracket.CURLY, DefaultParser.Bracket.ROUND, DefaultParser.Bracket.SQUARE)
        commandParser.isEofOnUnclosedQuote = true
        commandParser.isEofOnEscapedNewLine = true
        val lineReader = LineReaderBuilder.builder()
            .terminal(terminal)
            .parser(commandParser)
            .variable(LineReader.SECONDARY_PROMPT_PATTERN, "  ")
            .variable(LineReader.INDENTATION, 2)
            .build()

        while (true) {
            val input: String
            try {
                input = lineReader.readLine("> ")!!
            } catch (e: Exception) {
                when (e) {
                    is EndOfFileException -> exitProcess(0)
                    is UserInterruptException -> continue
                    else -> throw e
                }
            }

            if (input.isBlank())
                continue

            val statement = parser.parseStatement(input)
            if (statement != null) {
                interpreter.executeStatement(statement)
                if (statement is ExpressionStatement) {
                    println(interpreter.lastExpressionStatementValue!!.repr())
                }
            }
        }
    } else {
        try {
            val script = parser.parseFile(args[0])
            interpreter.executeScript(script)
        } catch (e: IOException) {
            System.err.println("Failed to open input file")
            exitProcess(1)
        }
    }
}
