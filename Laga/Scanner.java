package Laga;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Laga.Laga;

import static Laga.TokenType.*;

class Scanner {
    private final String source;
    private final List <Token> tokens = new ArrayList<>();

    private int start = 0;    // next 3 lines for the Scanner function below
    private int current = 0;
    private int line = 1;

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

    Scanner(String source) {
        this.source = source;
    }

    public List <Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private static final Map<String, TokenType> keywords;

    static {
      keywords = new HashMap<>();
      keywords.put("and",    AND);
      keywords.put("class",  CLASS);
      keywords.put("else",   ELSE);
      keywords.put("false",  FALSE);
      keywords.put("for",    FOR);
      keywords.put("fun",    FUN);
      keywords.put("if",     IF);
      keywords.put("nil",    NIL);
      keywords.put("or",     OR);
      keywords.put("print",  PRINT);
      keywords.put("return", RETURN);
      keywords.put("super",  SUPER);
      keywords.put("this",   THIS);
      keywords.put("true",   TRUE);
      keywords.put("var",    VAR);
      keywords.put("while",  WHILE);
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
          case '(': addToken(LEFT_PAREN); break;
          case ')': addToken(RIGHT_PAREN); break;
          case '{': addToken(LEFT_BRACE); break;
          case '}': addToken(RIGHT_BRACE); break;
          case ',': addToken(COMMA); break;
          case '.': addToken(DOT); break;
          case '-': addToken(MINUS); break;
          case '+': addToken(PLUS); break;
          case ';': addToken(SEMICOLON); break;
          case '*': addToken(STAR); break; 

          case ' ':
          case '\r':
          case '\t':
            // Ignore whitespace.
            break;
    
          case '\n':
            line++;
            break;
            
          case '!':
          addToken(match('=') ? BANG_EQUAL : BANG); // "If the current character is '=', add a token of type BANG_EQUAL; otherwise, add a token of type BANG."
          break;
        case '=':
          addToken(match('=') ? EQUAL_EQUAL : EQUAL); // same onwards
          break;
        case '<':
          addToken(match('=') ? LESS_EQUAL : LESS);
          break;
        case '>':
          addToken(match('=') ? GREATER_EQUAL : GREATER);
          break;

        case '/':
            if (match('/')) {
                while (peek() != '\n' && !isAtEnd()) advance();
            } else {
                addToken(SLASH);
            }
            break;

        case '"': string(); break;

        default:
        if (isDigit(c)) {
            number();

        } else if (isAlpha(c)) {
            identifier();
        
        } else {
            Laga.error(line, "Unexpected character.");  // handling all other types of unknown characters
        }
        break;
        }
    }
        private boolean isDigit(char c) {
            return c >= '0' && c <= '9';
        }

        private void identifier() {
            while(isAlphaNumeric(peek())) advance();

            String text = source.substring(start, current);
            TokenType type = keywords.get(text);             // whether to use keywords token type
            if (type == null) type = IDENTIFIER;             // otherwise it is a regular user identifier variable
            addToken(type);
        }

        private boolean isAlpha(char c) {
            return (c >= 'a' && c<= 'z') ||
                   (c >= 'a' && c<= 'z') ||
                   c == '_';
        }

        private boolean isAlphaNumeric(char c) {
            return isAlpha(c) || isDigit(c);
        }

        private void number() {
            while (isDigit(peek())) advance();

            if (peek() == '.' && isDigit(peekNext())) {
                advance();

                while (isDigit(peek())) advance();
            }

            addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
        }

        private boolean match(char expected) {
            if (isAtEnd()) return false;
            if (source.charAt(current) != expected) return false;

            current++;
            return true;
        }

        private char peek() {   // lookahead. The smaller it is the faster the scanner runs.
            if (isAtEnd()) return '\0';
            return source.charAt(current);
        }

        private char peekNext() {
            if (current + 1 >= source.length()) return '\0';
            return source.charAt(current + 1);
        }


        private void string() {
            while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
            }

            if (isAtEnd()) {
            Laga.error(line, "Unterminated string.");
            return;
            }

            // The closing ".
            advance();

            // Trim the surrounding quotes.
            String value = source.substring(start + 1, current - 1);
            addToken(STRING, value);
        }
};