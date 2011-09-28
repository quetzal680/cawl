package org.sspl.parser.xml.prolog;

public class XMLPrologToken {
    enum Type { EOF, WhiteSpace, Open, Version, Eq, Value, Encoding, Close };
    
    private final Type type;
    private final String text;
    
    public XMLPrologToken(Type type, String text) {
        this.type = type;
        this.text = text;
    }
    
    public Type getType() {
        return type;
    }
    
    public String getText() {
        return text;
    }
    
    public String toString() {
        return "XMLPrologToken[type=" + type + ", text=" + text + "]";
    }
}
