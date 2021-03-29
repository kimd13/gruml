package input.reader;

import util.exception.InvalidInputException;

public interface InputReader {
    void populateReader(String[] inputs) throws InvalidInputException;

    Boolean isFlagPresent(String flag);

    String getFlagValue(String flag);
}
