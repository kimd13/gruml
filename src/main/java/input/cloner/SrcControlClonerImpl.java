package input.cloner;

import org.eclipse.jgit.api.Git;
import util.exception.CloneException;
import java.io.File;

public class SrcControlClonerImpl implements SrcControlCloner {

    @Override
    public void clone(String srcUri, String dst) throws CloneException {
        try {
            Git.cloneRepository()
                    .setCloneSubmodules(true)
                    .setURI(srcUri)
                    .setDirectory(new File(dst))
                    .call();
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new CloneException();
        }
    }
}
