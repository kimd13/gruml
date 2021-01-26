import dependency.DependencyDiagram;
import input.reader.InputReaderImpl;
import input.cloner.SrcControlCloner;
import input.cloner.SrcControlClonerImpl;
import sequence.SequenceDiagram;
import util.exception.CloneException;
import util.exception.InvalidInputException;

public class GRUML {

    // This is at root level
    private static final String CLONED_SRC_DESTINATION = "cloned";

    private static final InputReaderImpl inputReader = new InputReaderImpl();
    private static final SrcControlCloner srcControlCloner = new SrcControlClonerImpl();

    public static void main(String[] args) {
        //populateInputReader(args);
        String srcUri = "https://github.com/kimd13/java-class-inheritance-dependency-example";//inputReader.getFlagValue("testing");
        cloneInputSrc(srcUri);
    }

    private static void populateInputReader(String [] args){
        try{
            inputReader.populateReader(args);
        }catch (InvalidInputException e){
            handleException(e);
        }
    }

    private static void cloneInputSrc(String srcUri){
        try{
            srcControlCloner.clone(srcUri, CLONED_SRC_DESTINATION);
        }catch (CloneException e){
            handleException(e);
        }
    }

    private static DependencyDiagram createDependencyDiagram() {
        return null;
    }

    private static SequenceDiagram createSequenceDiagram() {
        return null;
    }

    private static void handleException(Exception e){
        System.out.println(e.getMessage());
        System.exit(0);
    }
}
