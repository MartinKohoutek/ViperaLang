package com.martinkohoutek.vipera;

public enum TokenType {
    // Single-character tokens
    LPAREN, RPAREN, LBRACKET, RBRACKET,
    COMMA, COLON, DOT, MINUS, PLUS, SLASH, STAR,

    // One or two character tokens
    // NOT_EQUAL,
    // EQUAL, EQUAl_EQUAL,
    // GREATER, GREATER_EQUAL,
    // LESS, LESS_EQUAL,

    // Special tokens
    INDENT, DEDENT,

    // Literals
    IDENTIFIER, STRING, INT,

    // Keywords
    // IF, ELIF, ELSE, PASS, PRINT, WHILE,

    EOF
}
