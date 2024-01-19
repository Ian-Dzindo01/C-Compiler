package Laga;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Laga.Laga;

import static Laga.TokenType.*;   // bad practice, but works perfect for this example

class Scanner {

    Scanner(String source) {    // constructor
        this.source = source;
    }

    private final String source;
    private final List <Token> tokens = new ArrayList<>();

    private int start = 0;    // next 3 lines for the Scanner function below
    private int current = 0;
    private int line = 1;

    private boolean isAtEnd() {           // checks if we reached the end
        return current >= source.length();
    }

    private char advance() {              // advance to next character
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {   // adds the token to the tokens list
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));     // type of token, literal value, text stored and line number
    }

    public List <Token> scanTokens() {             // scanToken until you reach the end
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));   // at end of file token to the end
        return tokens;
    }

    private static final Map<String, TokenType> keywords;     // final - cannot be altered 

    static {                                                  // link keyword strings to corresponding token types
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
        char c = advance();                            // grab next char 
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
    
          case '\n':             // support for newlines
            line++;
            break;
            
          case '!':              // two char tokens
          addToken(match('=') ? BANG_EQUAL : BANG); // if the current character is '=', add a token of type BANG_EQUAL; otherwise, add a token of type BANG
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

        case '/':                             // commenting. NEED TO ADD SUPPORT FOR C style commenting /* */
            if (match('/')) {        // if char after / is also / disregards rest of line because it is a comment
                while (peek() != '\n' && !isAtEnd()) advance();
            } else {                          // if not followed by another slash just treat it as divide
                addToken(SLASH);
            }
            break;

        // case '/*':

        case '"': string(); break;        // string treating implemented in string function

        default:
        if (isDigit(c)) {                // handle digits
            number();

        } else if (isAlpha(c)) {    // handle characters
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

        private boolean isAlpha(char c) {                   // checks if its accepted char
            return (c >= 'a' && c<= 'z') ||
                   (c >= 'a' && c<= 'z') ||
                   c == '_';
        }

        private boolean isAlphaNumeric(char c) {     // check whether it is an accepted digit or char
            return isAlpha(c) || isDigit(c);
        }

        private void number() {                     // handle numbers - floats
            while (isDigit(peek())) advance();

            if (peek() == '.' && isDigit(peekNext())) {
                advance();

                while (isDigit(peek())) advance();
            }

            addToken(NUMBER, Double.parseDouble(source.substring(start, current)));        // add number to final list of tokens
        }

        private boolean match(char expected) {                 // check if char is one we expect
            if (isAtEnd()) return false;
            if (source.charAt(current) != expected) return false;

            current++;                                        // moves forward
            return true;
        }

        private char peek() {           // lookahead. The smaller it is the faster the scanner runs.
            if (isAtEnd()) return '\0';
            return source.charAt(current);
        }

        private char peekNext() {                               // get next char          
            if (current + 1 >= source.length()) return '\0';
            return source.charAt(current + 1);
        }


        private void string() {
            while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
            }

            if (isAtEnd()) {                          // if we encounter end of line, throw error
            Laga.error(line, "Unterminated string.");
            return;
            }

            advance();               // the closing "

            String value = source.substring(start + 1, current - 1);            // trim surrounding quotes.
            addToken(STRING, value);
        }
};