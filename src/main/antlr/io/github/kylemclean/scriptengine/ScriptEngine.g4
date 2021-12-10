grammar ScriptEngine;

@header {
package io.github.kylemclean.scriptengine;
}

file
  : stmt* EOF
  ;

blockStmt
  : '{' stmt* '}'
  ;

stmt
  : (declStmt | assignStmt | blockStmt | exprStmt | functionDefStmt | returnStmt | condStmt | whileStmt | forStmt | continueStmt | breakStmt | emptyStmt)
  ;

emptyStmt
  : ';'
  ;

continueStmt
  : KEYWORD_CONTINUE
  ;

breakStmt
  : KEYWORD_BREAK
  ;

condStmt
  : KEYWORD_IF expr consequent=stmt (KEYWORD_ELSE alternate=stmt)?
  ;

whileStmt
  : KEYWORD_WHILE expr stmt
  ;

forStmt
  : KEYWORD_FOR ID KEYWORD_IN domain=expr stmt
  ;

declStmt
  : KEYWORD_VAR ID (ASSIGN expr)?
  ;

assignStmt
  : dst=expr ASSIGN src=expr
  ;

exprStmt
  : expr
  ; 

functionDefStmt
  : KEYWORD_ASYNC? KEYWORD_FUNCTION ID '(' paramList ')' blockStmt
  | KEYWORD_ASYNC? KEYWORD_FUNCTION ID '(' paramList ')' ASSIGN expr
  ;

returnStmt
  : KEYWORD_RETURN expr
  ;

paramList
  : (ID (',' ID)* VARARGS?)?
  ;

argList
  : (expr (',' expr)*)?
  ;

expr
  : '(' expr ')' #parenExpr
  | expr '(' argList ')' #callExpr
  | expr '[' (exprSubscript=expr | sliceSubscript) ']' #subscriptExpr
  | expr op=MEMBER_ACCESS ID #accessExpr
  | op=KEYWORD_AWAIT expr #awaitExpr
  | <assoc=right> expr op=EXP expr #expExpr
  | op=(ADD | SUB) expr #unaryPlusMinusExpr
  | expr op=EXP expr #expExpr
  | expr op=(MUL | DIV | INT_DIV | REM) expr #mulDivRemExpr
  | expr op=(ADD | SUB) expr #addSubExpr
  | expr op=KEYWORD_IN expr #containsExpr
  | expr op=(LT | LTE | GT | GTE) expr #inequalityExpr
  | expr op=(EQ | NEQ) expr #equalityExpr
  | expr op=AND expr #andExpr
  | expr op=OR expr #orExpr
  | KEYWORD_IF '(' condition=expr ')' consequent=expr KEYWORD_ELSE alternate=expr #condExpr
  | INT #integerLiteralExpr
  | FLOAT #floatLiteralExpr
  | ID #variableExpr
  | (KEYWORD_TRUE | KEYWORD_FALSE) #booleanLiteralExpr
  | KEYWORD_NULL #nullExpr
  | KEYWORD_ASYNC? '(' paramList ')' '->' (expr | blockStmt) #functionExpr
  | STRING_LITERAL #stringLiteralExpr
  | '{' (keyValuePair (',' keyValuePair)*)? ','? '}' #dictionaryLiteralExpr
  | '[' (expr (',' expr)*)? ','? ']' #arrayLiteralExpr
  ;

sliceSubscript
  : min=expr? ':' max=expr? (':' step=expr?)?
  ;

keyValuePair
  : (STRING_LITERAL | ID) ':' expr
  ;

STRING_LITERAL
  : '\'' CHARACTER_ATOM*? '\''
  | '"' CHARACTER_ATOM*? '"'
  ;

fragment CHARACTER_ATOM
  : '\\a'
  | '\\b'
  | '\\n'
  | '\\r'
  | '\\t'
  | '\\\\'
  | '\\\''
  | '\\"'
  | ~[\\]
  ;

INT
  : [0-9]+
  ;

FLOAT
  : [0-9]+ '.' [0-9]* ([eE] ('+' | '-')? [0-9]+)?
  ;


KEYWORD_FUNCTION : 'fun' ;
KEYWORD_NULL : 'null' ;
KEYWORD_VAR : 'var' ;
KEYWORD_TRUE : 'true' ;
KEYWORD_FALSE : 'false' ;
KEYWORD_RETURN : 'return' ;
KEYWORD_ASYNC : 'async' ;
KEYWORD_AWAIT : 'await' ;
KEYWORD_IF : 'if' ;
KEYWORD_ELSE : 'else' ;
KEYWORD_WHILE : 'while' ;
KEYWORD_FOR : 'for' ;
KEYWORD_IN : 'in' ;
KEYWORD_AS : 'as' ;
KEYWORD_CONTINUE: 'continue' ;
KEYWORD_BREAK : 'break' ;

ID
  : [_a-zA-Z][_a-zA-Z0-9]*
  ;

MEMBER_ACCESS : '.' ;
EXP : '**' ;
MUL : '*' ;
DIV : '/' ;
INT_DIV : '//' ;
REM : '%' ;
ADD : '+' ;
SUB : '-' ;
LT : '<' ;
LTE : '<=' ;
GT : '>' ;
GTE : '>=' ;
EQ : '==' ;
NEQ : '!=' ;
NOT : '!' ;
AND : '&&' ;
OR : '||' ;

ASSIGN : '=' ;
VARARGS : '...' ;

COMMENT : '#' .*? '\n' -> channel(HIDDEN) ;

WS
  : [ \t\r\n] -> channel(HIDDEN)
  ;
