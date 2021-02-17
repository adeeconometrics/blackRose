package prototype.Java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static prototype.Java.TokenType.*;

/**
 * scans the source file
 */
class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    //offsets
    private int start = 0;
    private int current = 0;
    private int line = 1;

    Scanner(String source){
        this.source = source;
    }

    List<Token> scanTokens(){
        while(!isAtEnd()){
            // we are at the beginning of the next lexeme
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    /**
     * helper function that signals if we consumed all the lenght
     * @return boolean
     */
    private boolean isAtEnd(){
        return current>=source.length();
    }

    /**
     * recognizing lexemes
     */
    private void scanToken(){
        char c = advance();

        switch(c){
            case '(': 
                addToken(LEFT_PAREN); 
                break;
            case ')': 
                addToken(RIGHT_PAREN); 
                break;
            case '{': 
                addToken(LEFT_BRACE); 
                break;
            case '}': 
                addToken(RIGHT_BRACE); 
                break;
            case ',': 
                addToken(COMMA); 
                break;
            case '.': 
                addToken(DOT); 
                break;
            case '-': 
                addToken(MINUS); 
                break;
            case '+': 
                addToken(PLUS); 
                break;
            case ';': 
                addToken(SEMICOLON); 
                break;
            case '*': 
                addToken(STAR); 
                break; 
            // set of operators
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '/':
                if(match('/'))
                    while (peek() != '\n' && !isAtEnd()) advance();
                else addToken(SLASH);

                break;

            // white space and line 
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;

            case '\n':
                line++;
                break;

            // literals
            case '"': 
                string();
                break;

            default:
                if(isDigit(c)){number();}
                else if(isAlpha(c)) {identifier();}
                else {Lox.error(line, "Unexpected character.");}

                break;
        }
    }
    
    /**
     * scans identifiers
     */
    private void identifier(){
        while(isAlphaNeumeric(peek())) advance();
        
        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if(type == null) type = IDENTIFIER;
        addToken(type);
    }

    /**
     * helper function for determining if lexemes belongs to aphabet
     * @param c - scanned character
     * @return true if alphabet [a-z,A-Z, '_']
     */
    private boolean isAlpha(char c){
        return (c>='a' && c<='z') || (c>='A' && c<='Z')|| c == '_';
    }

    /**
     * helper function for determining if lexemes are digit
     * @param c - scanned character
     * @return true if digit [0-9]
     */
    private boolean isDigit(char c){
        return c>='0' && c<='9';
    }
    private boolean isAlphaNeumeric(char c){
        return isAlpha(c) || isDigit(c);
    }

    /**
     * helper function adds tokens if they are numerical literal
     */
    private void number(){
        while(isDigit(peek())) advance();

        //look for a fractional part
        if(peek() == '.' && isDigit(peekNext())){
            // consume the "."
            advance();
            while(isDigit(peek())) advance();
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    /**
     * helper function that consumes next character 
     * in the source file and returns it
     */
    private char advance() {
        current++;
        return source.charAt(current - 1);
    }

    /**
     * responsible for output. Grabs the text of current lexeme
     * and creates a new token for it.
     * @param type: TokenType
     */
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    /**
     * helper function responsible for output. Grabs the text of current 
     * lexeme and creates a new token for it.
     * @params type: TokenType, literal: Object
     */
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    /**
     * helper function fo matching characters
     * @return true if character matchs with current
     */
    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    /**
     * helper function that only looks at the current unconsumed character
     * @return character
     */
    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    /**
     * helper function that looks out for next two unconsumed characters
     * @return next character
     */
    private char peekNext(){
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current+1);
    }

    private void string(){
        while(peek() != '"' && !isAtEnd()){
            if(peek() == '\n') line++;
            advance();
        }

        if(isAtEnd()){
            Lox.error(line, "Unterminated string.");
            return;
        }

        //closing
        advance();

        //trim surrounding quotes
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }
  
    private static final Map<String, TokenType> keywords;
    // associative array for reserved words
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
}
