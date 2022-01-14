package io.github.kylemclean.scriptengine.ast

import io.github.kylemclean.scriptengine.ScriptEngineParser.*
import io.github.kylemclean.scriptengine.ast.expressions.*
import io.github.kylemclean.scriptengine.ast.statements.*

class ASTBuilder : io.github.kylemclean.scriptengine.ScriptEngineBaseVisitor<Any>() {
    override fun visitFile(ctx: FileContext) = BlockStatement(ctx.stmt().map { visitStmt(it)!! })

    override fun visitBlockStmt(ctx: BlockStmtContext) = BlockStatement(ctx.stmt().map { visitStmt(it)!! })

    override fun visitStmt(ctx: StmtContext): Statement? {
        return visitChildren(ctx) as Statement?
    }

    override fun visitEmptyStmt(ctx: EmptyStmtContext): EmptyStatement {
        return EmptyStatement
    }

    override fun visitDeclStmt(ctx: DeclStmtContext): DeclarationStatement {
        return DeclarationStatement(ctx.ID().text, visit(ctx.expr()) as Expression)
    }

    override fun visitAssignStmt(ctx: AssignStmtContext): AssignmentStatement {
        val dstExpr = visit(ctx.dst) as Expression
        require(dstExpr is AssignableExpression) { "destination is not an l-value" }
        return AssignmentStatement(visit(ctx.src) as Expression, dstExpr as AssignableExpression)
    }

    override fun visitCallExpr(ctx: CallExprContext): CallExpression =
        CallExpression(visit(ctx.expr()) as Expression, visitArgList(ctx.argList()))

    override fun visitFunctionDefStmt(ctx: FunctionDefStmtContext): FunctionDefinitionStatement {
        val blockStmt = if (ctx.blockStmt() != null) {
            visitBlockStmt(ctx.blockStmt())
        } else {
            BlockStatement(listOf(ReturnStatement(visit(ctx.expr()!!) as Expression)))
        }
        return FunctionDefinitionStatement(ctx.ID().text, blockStmt, visitParamList(ctx.paramList()))
    }

    override fun visitParamList(ctx: ParamListContext): ParamList =
        ParamList(ctx.ID().map { it.text }, ctx.VARARGS() != null)

    override fun visitArgList(ctx: ArgListContext): List<Expression> = ctx.expr().map { visit(it) as Expression }

    override fun visitParenExpr(ctx: ParenExprContext): Expression {
        return visit(ctx.expr()) as Expression
    }

    override fun visitBooleanLiteralExpr(ctx: BooleanLiteralExprContext): BooleanLiteralExpression {
        return BooleanLiteralExpression(ctx.KEYWORD_TRUE() != null)
    }

    override fun visitExpExpr(ctx: ExpExprContext): ArithmeticBinaryOperatorExpression.ExponentiationExpression =
        ArithmeticBinaryOperatorExpression.ExponentiationExpression(
            visit(ctx.expr(0)) as Expression,
            visit(ctx.expr(1)) as Expression)

    override fun visitIntegerLiteralExpr(ctx: IntegerLiteralExprContext): IntegerLiteralExpression {
        return IntegerLiteralExpression(ctx.INT().text.toInt())
    }

    override fun visitFloatLiteralExpr(ctx: FloatLiteralExprContext): FloatLiteralExpression {
        return FloatLiteralExpression(ctx.FLOAT().text.toFloat())
    }

    override fun visitUnaryPlusMinusExpr(ctx: UnaryPlusMinusExprContext): UnaryPlusMinusExpression =
        UnaryPlusMinusExpression(visit(ctx.expr()) as Expression, ctx.SUB() != null)

    override fun visitMulDivRemExpr(ctx: MulDivRemExprContext): Expression {
        val lhsExpression = visit(ctx.expr(0)) as Expression
        val rhsExpression = visit(ctx.expr(1)) as Expression
        return when {
            ctx.MUL() != null -> ArithmeticBinaryOperatorExpression.MultiplicationExpression(lhsExpression, rhsExpression)
            ctx.DIV() != null -> ArithmeticBinaryOperatorExpression.FloatDivisionExpression(lhsExpression, rhsExpression)
            ctx.INT_DIV() != null -> ArithmeticBinaryOperatorExpression.IntegerDivisionExpression(lhsExpression, rhsExpression)
            ctx.REM() != null -> ArithmeticBinaryOperatorExpression.RemainderExpression(lhsExpression, rhsExpression)
            else -> throw IllegalArgumentException("unknown operator")
        }
    }

    override fun visitNullExpr(ctx: NullExprContext): NullLiteralExpression = NullLiteralExpression

    override fun visitAddSubExpr(ctx: AddSubExprContext): Expression {
        val lhsExpression = visit(ctx.expr(0)) as Expression
        val rhsExpression = visit(ctx.expr(1)) as Expression
        return if (ctx.ADD() != null)
            ArithmeticBinaryOperatorExpression.AdditionExpression(lhsExpression, rhsExpression)
        else
            ArithmeticBinaryOperatorExpression.SubtractionExpression(lhsExpression, rhsExpression)
    }

    override fun visitVariableExpr(ctx: VariableExprContext): VariableExpression {
        return VariableExpression(ctx.text)
    }

    override fun visitAccessExpr(ctx: AccessExprContext): AccessExpression {
        return AccessExpression(visit(ctx.expr()) as Expression, ctx.ID().text)
    }

    override fun visitStringLiteralExpr(ctx: StringLiteralExprContext): StringLiteralExpression {
        val string = ctx.STRING_LITERAL().text.removeQuotes()
        return StringLiteralExpression(string)
    }

    override fun visitSubscriptExpr(ctx: SubscriptExprContext): SubscriptExpression {
        val subscriptExpression = if (ctx.exprSubscript != null)
            visit(ctx.exprSubscript) as Expression
        else
            visit(ctx.sliceSubscript()) as Expression
        return SubscriptExpression(visit(ctx.expr(0)) as Expression, subscriptExpression)
    }

    override fun visitSliceSubscript(ctx: SliceSubscriptContext): SliceExpression {
        val minExpression = if (ctx.min != null) visit(ctx.min) as Expression else null
        val maxExpression = if (ctx.max != null) visit(ctx.max) as Expression else null
        val stepExpression = if (ctx.step != null) visit(ctx.step) as Expression else null
        return SliceExpression(minExpression, maxExpression, stepExpression)
    }

    override fun visitDictionaryLiteralExpr(ctx: DictionaryLiteralExprContext) =
        DictionaryLiteralExpression(ctx.keyValuePair().associate { visitKeyValuePair(it) })

    override fun visitKeyValuePair(ctx: KeyValuePairContext): Pair<String, Expression> {
        val key = if (ctx.STRING_LITERAL() != null) {
            ctx.STRING_LITERAL().text.removeQuotes()
        } else {
            ctx.ID().text
        }
        return Pair(key, visit(ctx.expr()) as Expression)
    }

    override fun visitArrayLiteralExpr(ctx: ArrayLiteralExprContext): ArrayLiteralExpression =
        ArrayLiteralExpression(ctx.expr().map {
            visit(it) as Expression
        })

    override fun visitInequalityExpr(ctx: InequalityExprContext): InequalityOperatorExpression {
        val lhsExpression = visit(ctx.expr(0)) as Expression
        val rhsExpression = visit(ctx.expr(1)) as Expression
        return when {
            ctx.LT() != null -> InequalityOperatorExpression.LessThanExpression(lhsExpression, rhsExpression)
            ctx.LTE() != null -> InequalityOperatorExpression.LessThanOrEqualExpression(lhsExpression, rhsExpression)
            ctx.GT() != null -> InequalityOperatorExpression.GreaterThanExpression(lhsExpression, rhsExpression)
            ctx.GTE() != null -> InequalityOperatorExpression.GreaterThanOrEqualExpression(lhsExpression, rhsExpression)
            else -> throw IllegalArgumentException("unknown operator")
        }
    }

    override fun visitEqualityExpr(ctx: EqualityExprContext): EqualityOperatorExpression =
        EqualityOperatorExpression(
            visit(ctx.expr(0)) as Expression,
            visit(ctx.expr(1)) as Expression,
            ctx.EQ() == null)

    override fun visitFunctionExpr(ctx: FunctionExprContext): FunctionExpression {
        val body =
            if (ctx.blockStmt() != null)
                visitBlockStmt(ctx.blockStmt())
            else
                BlockStatement(listOf(ReturnStatement(visit(ctx.expr()) as Expression)))
        return FunctionExpression(body, visitParamList(ctx.paramList()))
    }

    override fun visitCondExpr(ctx: CondExprContext) = ConditionalExpression(
            visit(ctx.condition) as Expression, visit(ctx.consequent) as Expression, visit(ctx.alternate) as Expression)

    override fun visitContainsExpr(ctx: ContainsExprContext): ContainsExpression =
        ContainsExpression(visit(ctx.expr(0)) as Expression, visit(ctx.expr(1)) as Expression)

    override fun visitAndExpr(ctx: AndExprContext): AndExpression =
        AndExpression(visit(ctx.expr(0)) as Expression, visit(ctx.expr(1)) as Expression)

    override fun visitOrExpr(ctx: OrExprContext): OrExpression =
        OrExpression(visit(ctx.expr(0)) as Expression, visit(ctx.expr(1)) as Expression)

    override fun visitReturnStmt(ctx: ReturnStmtContext): ReturnStatement {
        return ReturnStatement(visit(ctx.expr()) as Expression)
    }

    override fun visitExprStmt(ctx: ExprStmtContext): ExpressionStatement {
        return ExpressionStatement(visit(ctx.expr()) as Expression)
    }

    override fun visitCondStmt(ctx: CondStmtContext): ConditionalStatement {
        return ConditionalStatement(
            visit(ctx.expr()) as Expression,
            visit(ctx.consequent) as Statement,
            if (ctx.alternate != null) visit(ctx.alternate) as Statement else null)
    }

    override fun visitForStmt(ctx: ForStmtContext): ForStatement {
        var body = visitStmt(ctx.stmt())!!
        if (body !is BlockStatement)
            body = BlockStatement(listOf(body))
        return ForStatement(ctx.ID().text, visit(ctx.domain) as Expression, body)
    }

    override fun visitWhileStmt(ctx: WhileStmtContext): WhileStatement {
        var body = visitStmt(ctx.stmt())!!
        if (body !is BlockStatement)
            body = BlockStatement(listOf(body))
        return WhileStatement(visit(ctx.expr()) as Expression, body)
    }

    override fun visitBreakStmt(ctx: BreakStmtContext): BreakStatement = BreakStatement

    override fun visitContinueStmt(ctx: ContinueStmtContext): ContinueStmt = ContinueStmt
}

fun String.removeQuotes(): String = substring(1, length - 1)
