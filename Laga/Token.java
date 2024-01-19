package Laga;

class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;
    final int line;

    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;                   // type of token
        this.lexeme = lexeme;               // actual text
        this.literal = literal;            // value
        this.line = line;                  // line number
    }

    public String toString() {                  // pretty printing
        return type + " " + lexeme + " " + literal;
    }
}
