package input.cloner;

import util.exception.CloneException;

public interface SrcControlCloner {
    void clone(String srcUri, String dst) throws CloneException;
}
