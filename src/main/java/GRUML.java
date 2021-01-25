import input.InputReaderImpl;
import util.exception.InvalidInputException;

public class GRUML {

    private static final InputReaderImpl inputParser = new InputReaderImpl();

    public static void main(String[] args) {
        verifyUserInput(args);
    }

    private static void verifyUserInput(String [] args){
        try{
            inputParser.validateInput(args);
        }catch (InvalidInputException e){
            System.out.println(e.getMessage());
        }
    }
}
