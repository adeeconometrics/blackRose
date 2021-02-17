package prototype.Java;

/**
 * Token class is responsible for tracking where the erros has occured,
 * Token communicates with the scanner class
 */
public class Token {
  final TokenType type;
  final String lexeme;
  final Object literal; // root object
  final int line; 

  Token(TokenType type, String lexeme, Object literal, int line) {
    this.type = type;
    this.lexeme = lexeme;
    this.literal = literal;
    this.line = line;
  }

  public String toString() {
    return type + " " + lexeme + " " + literal;
  }
}
