package org.sspl.parser;

/**
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InvalidTokenException extends Exception {
    private final int line, column;
    private final String message;
    
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3258415019233653815L;
	
    public InvalidTokenException(int line, int column, String message) {
        this.line = line;
        this.column = column;
        this.message = message;
    }
    
    public int getLine() {
        return line;
    }
    
    public int getColumn() {
        return column;
    }
    
    @Override
    public String getMessage() {
        return line + ":" + column + ", " + message;
    }
}
