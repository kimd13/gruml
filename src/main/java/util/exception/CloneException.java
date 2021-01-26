package util.exception;

public class CloneException extends Exception{
    public CloneException() {
        super("Something went wrong while cloning src.");
    }
}
