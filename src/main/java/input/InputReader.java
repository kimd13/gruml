package input;

import util.exception.InvalidInputException;

public interface InputReader {
    void validateInput(String[] inputs) throws InvalidInputException;
    String isFlagPresent(String flag);
    String getFlagValue(String flag);
    String getFlagValue(int index);
}
