package input;

import util.exception.InvalidInputException;
import java.util.HashMap;

public class InputReaderImpl implements InputReader {

    private HashMap<String, String> arguments;

    @Override
    public void validateInput(String[] inputs) throws InvalidInputException {

    }

    private void populateArguments(String[] inputs){
        for (int i = 0; i < inputs.length; i++){
            String currentInput = inputs[i];
            if (isFlag(currentInput)) {

            } else if (isArgument(currentInput)){

            }
        }
    }

    private Boolean isFlag(String input){
        return input.charAt(0) == '-';
    }

    private Boolean isArgument(String input){
        return !input.equals("") && !isFlag(input);
    }

    @Override
    public String isFlagPresent(String flag) {
        return null;
    }

    @Override
    public String getFlagValue(String flag) {
        return null;
    }

    @Override
    public String getFlagValue(int index) {
        return null;
    }
}
