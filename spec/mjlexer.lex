package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
		return new_symbol(sym.EOF);
%eofval}

%%


" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

"program"   { return new_symbol(sym.PROG, yytext()); }
"break"		{ return new_symbol(sym.BREAK, yytext()); }
"return"   	{ return new_symbol(sym.RETURN, yytext()); }
"print"   	{ return new_symbol(sym.PRINT, yytext()); }
"void"   	{ return new_symbol(sym.VOID, yytext()); }
"const"   	{ return new_symbol(sym.CONST, yytext()); }
"new"   	{ return new_symbol(sym.NEW, yytext()); }
"read"   	{ return new_symbol(sym.READ, yytext()); }
"if"		{ return new_symbol(sym.IF, yytext()); }
"else"		{ return new_symbol(sym.ELSE, yytext()); }
"continue"	{ return new_symbol(sym.CONTINUE, yytext()); }
"for"		{ return new_symbol(sym.FOR, yytext()); }
"length"	{ return new_symbol(sym.LENGTH, yytext()); }
"enum"		{ return new_symbol(sym.ENUM, yytext()); }
"findAny"	{ return new_symbol(sym.FINDANY, yytext()); }
"map"		{ return new_symbol(sym.MAP, yytext()); }

"+" 		{ return new_symbol(sym.PLUS, yytext()); }
"-"			{ return new_symbol(sym.MINUS, yytext()); }
"*"			{ return new_symbol(sym.MUL, yytext()); }
"/"			{ return new_symbol(sym.DIV, yytext()); }
"%"			{ return new_symbol(sym.MOD, yytext()); }
"=" 		{ return new_symbol(sym.ASSIGN, yytext()); }
";" 		{ return new_symbol(sym.SEMI, yytext()); }
":"			{ return new_symbol(sym.COLON, yytext()); }
"," 		{ return new_symbol(sym.COMMA, yytext()); }
"."			{ return new_symbol(sym.DOT, yytext()); }
"(" 		{ return new_symbol(sym.LPARENT, yytext()); }
")" 		{ return new_symbol(sym.RPARENT, yytext()); }
"["			{ return new_symbol(sym.LBRACKET, yytext()); }
"]"			{ return new_symbol(sym.RBRACKET, yytext()); }
"{" 		{ return new_symbol(sym.LBRACE, yytext()); }
"}"			{ return new_symbol(sym.RBRACE, yytext()); }
"++"		{ return new_symbol(sym.INC, yytext()); }
"--"		{ return new_symbol(sym.DEC, yytext()); }
"=="		{ return new_symbol(sym.EQUAL, yytext()); }
"!="		{ return new_symbol(sym.NOTEQUAL, yytext()); }
">"			{ return new_symbol(sym.GREATER, yytext()); }
">="		{ return new_symbol(sym.GREATEREQUAL, yytext()); }
"<"			{ return new_symbol(sym.LESS, yytext()); }
"<="		{ return new_symbol(sym.LESSEQUAL, yytext()); }
"&&"		{ return new_symbol(sym.AND, yytext()); }
"||"		{ return new_symbol(sym.OR, yytext()); }
"?"			{ return new_symbol(sym.QUESTION, yytext()); }
"=>"		{ return new_symbol(sym.ARROW, yytext()); }
"@"			{ return new_symbol(sym.AT, yytext()); }

"//" 				{ yybegin(COMMENT); }
<COMMENT> .			{ yybegin(COMMENT); }
<COMMENT> "\r\n"	{ yybegin(YYINITIAL); }

[0-9]+ 							{ return new_symbol(sym.NUMBER, new Integer (yytext())); }
"'"."'"							{ return new_symbol(sym.CHARACTER, new Character (yytext().charAt(1))); }
("true"|"false")				{ return new_symbol(sym.BOOL, yytext().equals("true")? 1 : 0); }
([a-z]|[A-Z])[a-z|A-Z|0-9|_]* 	{ return new_symbol (sym.IDENT, yytext()); }

.			{ System.err.println("Leksicka greska ("+yytext()+") na liniji "+(yyline+1) + " i u koloni " + (yycolumn+1) + "\n"); }

/* ============================================================================
   NACRT (komentarisano, neaktivno): leksicko pravilo za "count" operaciju nad nizom.
   Testirano i potvrdjeno da radi (privremeno aktivirano, zatim vraceno kao komentar).
   Isti obrazac kao za "findAny"/"map" iznad - rezervisana rec pre generickog
   IDENT pravila, tako da se ne meša sa identifikatorima.
   ============================================================================
   "count"		{ return new_symbol(sym.COUNT, yytext()); }
*/
