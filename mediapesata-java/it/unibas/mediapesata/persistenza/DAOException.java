package it.unibas.mediapesata.persistenza;

public class DAOException extends Exception {
    
    public DAOException() {
        super();
    }
    
    public DAOException(String s) {
        super(s);
    }
    
    public DAOException(Exception e) {
        super(e);
    }

}
