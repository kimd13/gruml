package dependency.inheritance;

import dependency.inheritance.model.ClassInfoContainer;
import dependency.inheritance.model.InheritanceRelationship;
import util.file.FileUtil;
import util.regex.RegexUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class InheritanceDependencyDiagramImpl implements InheritanceDependencyDiagram {

    private HashMap<String, ClassInfoContainer> inheritanceMap = new HashMap<>();
    private RegexUtil regexUtil = RegexUtil.getInstance();
    private FileUtil fileUtil = FileUtil.getInstance();

    @Override
    public void populateDiagram(String srcPath) {
        try {
            List<String> paths = fileUtil.getAllFilePaths(srcPath);
            for (int i = 0; i < paths.size(); i++){
                String path = paths.get(i);
                String fileContent = fileUtil.readFile(path);
                String filteredFileContent = removeUnnecessarySubstrings(fileContent);
                System.out.println(filteredFileContent);
                populateInheritanceMap(filteredFileContent);
                System.out.println(inheritanceMap);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<String> getAll() {
        return new ArrayList<String>(inheritanceMap.keySet());
    }

    @Override
    public List<String> getChildren(String name) {
        return inheritanceMap.get(name).getChildren();
    }

    @Override
    public Boolean checkIfAbstract(String name) {
        return inheritanceMap.get(name).isAbstract();
    }

    @Override
    public Boolean checkIfInterface(String name) {
        return inheritanceMap.get(name).isInterface();
    }

    private void populateInheritanceMap(String target){
        List<String> inheritanceSubstrings = getInheritanceSubstrings(target);
        List<InheritanceRelationship> inheritanceRelationships = convertInheritanceSubstringsToRelationships(inheritanceSubstrings);
        for (InheritanceRelationship relationship: inheritanceRelationships){
            inheritanceMap.computeIfAbsent(relationship.getChild(), info -> new ClassInfoContainer())
                    .setIsAbstract(relationship.isChildAbstract());
            inheritanceMap.computeIfAbsent(relationship.getParent(), info -> new ClassInfoContainer())
                    .addChild(relationship.getChild())
                    .setIsInterface(relationship.isParentInterface());
        }
    }

    public List<InheritanceRelationship> convertInheritanceSubstringsToRelationships(List<String> inheritanceSubstrings){
        List<InheritanceRelationship> relationships = new ArrayList<>();
        for (int i = 0; i < inheritanceSubstrings.size(); i++){
            String inheritanceSubstring = inheritanceSubstrings.get(i);
            List<String> childInfo = extractChildInfo(inheritanceSubstring);
            String childPrecedent = childInfo.get(0);
            String childName = childInfo.get(1);
            List<String> interfaceParents = extractParents(inheritanceSubstring, true);
            List<String> classParents = extractParents(inheritanceSubstring, false);
            interfaceParents.forEach(parent -> { relationships.add( new InheritanceRelationship(childPrecedent, childName, "implements", parent)); });
            classParents.forEach(parent -> { relationships.add( new InheritanceRelationship(childPrecedent, childName,"extends", parent)); });
        }
        return relationships;
    }

    private List<String> extractChildInfo(String inheritanceSubstring){
        String [] classKeywordRemoved = inheritanceSubstring.split("class");
        String childPrecedent = extractLastWordInString(classKeywordRemoved[0]);
        String childName = extractFirstWordInString(classKeywordRemoved[1]);
        return Arrays.asList(childPrecedent, childName);
    }

    private List<String> extractParents(String inheritanceSubstring, Boolean getInterfaceParents){
        String splitKeyword =  getInterfaceParents ? "implements" : "extends";
        List<String> parents = new ArrayList<>();
        String [] split = inheritanceSubstring.split(splitKeyword);
        for (int i = 1; i < split.length; i++){
            String parent = extractFirstWordInString(split[i]);
            parents.add(parent);
        }
        return parents;
    }

    private String extractLastWordInString(String target){
        String [] split = target.split(" ");
        return split[split.length - 1];
    }

    private String extractFirstWordInString(String target){
        return target.split(" ")[1];
    }

    public List<String> getInheritanceSubstrings(String target){
        String inheritanceRegex = "(abstract )?class(.[^\\t\\n\\r ]*)(( extends| implements) (.[^\\t\\n\\r ]*))*";
         return regexUtil.getMatched(inheritanceRegex, target);
    }

    private String removeSingleLineComments(String target){
        String singleLineCommentRegex = "//.[^\\n\\r]*";
        return regexUtil.removeMatched(singleLineCommentRegex, target);
    }

    private String removeMultiLineComments(String target){
        String multiLineCommentRegex = "/\\*.+?\\*/";
        return regexUtil.removeMatched(multiLineCommentRegex, target);
    }

    private String removeStrings(String target){
        String stringRegex = "\".+?\"";
        return regexUtil.removeMatched(stringRegex, target);
    }

    public String removeUnnecessarySubstrings(String target){
        String withoutSingleLineComments = removeSingleLineComments(target);
        String withoutMultiLineComments = removeMultiLineComments(withoutSingleLineComments);
        return removeStrings(withoutMultiLineComments);
    }
}
