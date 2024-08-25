package com.martinkohoutek.vipera;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.martinkohoutek.vipera.TokenType.*;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;
    private int currentIndent = 0;
    private final Stack<Integer> indentStack = new Stack<>();

    Scanner(String source) {
        this.source = source;
        indentStack.push(0);
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        while (indentStack.size() > 1) {
            indentStack.pop();
            addToken(DEDENT);
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
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
            case '\n': handleNewline(); break;
            case ' ':  handleIndent(); break;
            case '\r':
            case '\t': break;
            case '"': string(); break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Vipera.error(line, "Unexpected character: " + c);
                }
                break;
        }
    }

    private void handleIndent() {
        int indent = 0;
        while (peek() == ' ') {
            advance();
            indent++;
        }

        if (indent == 0) {
            return;
        }
        if (indent > currentIndent) {
            if (!indentStack.isEmpty() && indent != indentStack.peek() + 4) {
                Vipera.error(line, "Unexpected indentation level at line " + line);
            }
            addToken(INDENT);
            indentStack.push(indent);
            currentIndent = indent;
        } else if (indent < currentIndent) {
            checkDedent(indent);
        }
    }

    private void handleNewline() {
        line++;
        addToken(NEWLINE);
        if (!isAtEnd()) {
            char nextChar = peek();
            if (nextChar == ' ') {
                start = current;
                handleIndent();
            } else if (nextChar == '\n' || nextChar == '\r' || nextChar == '\t') {
                advance();
            }
        }
    }

    private void checkDedent(int indent) {
        while (!indentStack.isEmpty() && indent < indentStack.peek()) {
            if (indent % 4 != 0) {
                Vipera.error(line, "Unexpected dedentation at line " + line);
            }

            indentStack.pop();
            addToken(DEDENT);
        }

        if (indent < indentStack.peek()) {
            Vipera.error(line, "Unexpected dedentation at line " + line);
        }

        currentIndent = indent;
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
        return isAtEnd() ? '\0' : source.charAt(current);
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        if (isAtEnd()) {
            Vipera.error(line, "Unterminated string.");
            return;
        }

        advance();
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

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();
        addToken(IDENTIFIER);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }
}
