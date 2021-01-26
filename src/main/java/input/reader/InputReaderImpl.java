package input.reader;

import util.exception.InvalidInputException;
import java.util.HashMap;

public class InputReaderImpl implements InputReader {

    private HashMap<String, String> arguments;

    @Override
    public void populateReader(String[] inputs) throws InvalidInputException {
        //String pathToSource = inputs[0];
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
    public Boolean isFlagPresent(String flag) {
        return arguments.get(flag) != null;
    }

    @Override
    public String getFlagValue(String flag) {
        return arguments.get(flag);
    }
}
