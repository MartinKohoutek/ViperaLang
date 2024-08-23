package com.martinkohoutek.vipera;

import java.util.ArrayList;
import java.util.List;

import static com.martinkohoutek.vipera.TokenType.*;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));     // End of Stream
        return tokens;
    }

    private void scanToken() {
        char c = advance();         // next char
        switch (c) {
            case '(': addToken(LPAREN); break;
            case ')': addToken(RPAREN); break;
            case ']': addToken(LBRACKET); break;
            case '[': addToken(RBRACKET); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ':': addToken(COLON); break;
            case '*': addToken(STAR); break;
            case '/': addToken(SLASH); break;

            case '#': while (peek() != '\n' && !isAtEnd()) advance(); break;

            case ' ':
            case '\r':
            case '\t':
                break;      // Ignore white space
            case '\n': line++; break;

            case '"': string(); break;

            default:
                if (isDigit(c)) {
                    number();
                } else {
                    Vipera.error(line, "Unexpected character");
                }
                break;
        }
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        if (isAtEnd()) {
            Vipera.error(line, "Unterminated string.");
        }

        advance();         // The closing ".

        // Trim trailing ""
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void number() {
        while (isDigit(peek())) advance();

        addToken(INT, Integer.parseInt(source.substring(start, current)));
    }
}
