package Laga;

enum TokenType {
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,    // single-character tokens.
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,
  
    BANG, BANG_EQUAL,          // one or two character tokens.
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,
  
    IDENTIFIER, STRING, NUMBER,         // literals
  
    AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR,      // keywords
    PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE,
  
    EOF
  }