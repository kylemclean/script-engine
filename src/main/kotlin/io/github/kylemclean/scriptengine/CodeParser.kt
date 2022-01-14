package io.github.kylemclean.scriptengine

import io.github.kylemclean.scriptengine.ast.ASTBuilder
import io.github.kylemclean.scriptengine.ast.expressions.FunctionExpression
import io.github.kylemclean.scriptengine.ast.statements.BlockStatement
import io.github.kylemclean.scriptengine.ast.statements.Statement
import io.github.kylemclean.scriptengine.interpreter.Script
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree

class CodeParser {
    private val astBuilder = ASTBuilder()

    fun parseFile(fileName: String): Script {
        val input = CharStreams.fromFileName(fileName)
        val lexer = ScriptEngineLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = ScriptEngineParser(tokens)
        val parseTree: ParseTree = parser.file()
        val rootNode = astBuilder.visit(parseTree)!!
        return Script(rootNode as BlockStatement)
    }

    fun parseStatement(statementCode: String): Statement? {
        val lexer = ScriptEngineLexer(CharStreams.fromString(statementCode))
        val tokens = CommonTokenStream(lexer)
        val parser = ScriptEngineParser(tokens)
        val parseTree: ParseTree = parser.stmt()
        return astBuilder.visit(parseTree) as Statement?
    }

    fun parseFunctionExpression(functionExpressionCode: String): FunctionExpression? {
        val lexer = ScriptEngineLexer(CharStreams.fromString(functionExpressionCode))
        val tokens = CommonTokenStream(lexer)
        val parser = ScriptEngineParser(tokens)
        val parseTree: ParseTree = parser.expr()
        return astBuilder.visit(parseTree) as FunctionExpression?
    }
}
