package static_section.extractor;

import static_section.extractor.dependency.inheritance.InheritanceExtractor;
import static_section.extractor.dependency.inheritance.InheritanceExtractorImpl;
import static_section.extractor.dependency.use_relationship.UseRelationshipExtractor;
import static_section.extractor.dependency.use_relationship.UseRelationshipExtractorImpl;
import static_section.extractor.general.ObjectInfoExtractor;
import static_section.extractor.general.ObjectInfoExtractorImpl;
import static_section.extractor.struct.BeginningEndIndices;
import static_section.extractor.struct.IndexStack;
import util.file.FileUtil;
import util.keyword.Keyword;
import util.regex.RegexUtil;
import java.util.ArrayList;
import java.util.List;

public class ExtractorImpl implements Extractor{

    private final InheritanceExtractor inheritanceExtractor = new InheritanceExtractorImpl();
    private final ObjectInfoExtractor objectInfoExtractor = new ObjectInfoExtractorImpl();
    private final UseRelationshipExtractor useRelationshipExtractor = new UseRelationshipExtractorImpl(objectInfoExtractor);

    private final RegexUtil regexUtil = RegexUtil.getInstance();
    private final FileUtil fileUtil = FileUtil.getInstance();

    @Override
    public void extractAllInfo(String srcPath) {
        List<String> objectsAsStrings = getAllObjectsAsStrings(srcPath);
        objectInfoExtractor.extractAllObjectInfo(objectsAsStrings);
        inheritanceExtractor.extractAllInheritanceInfo(objectsAsStrings);
        useRelationshipExtractor.extractAllUseRelationshipInfo(objectsAsStrings);
    }

    @Override
    public List<String> getAllObjects() {
        return objectInfoExtractor.getAllObjects();
    }

    @Override
    public List<String> getAllObjectMethods(String objectName) {
        return objectInfoExtractor.getAllObjectMethods(objectName);
    }

    @Override
    public boolean isObjectInheritedFrom(String objectName) {
        return inheritanceExtractor.isObjectInheritedFrom(objectName);
    }

    @Override
    public boolean isObjectUsedByAnother(String objectName) {
        return useRelationshipExtractor.isObjectUsedByAnother(objectName);
    }

    private List<String> getAllObjectsAsStrings(String srcPath){
        List<String> objectsAsStrings = new ArrayList<>();
        try {
            List<String> paths = fileUtil.getAllFilePaths(srcPath);
            for (String path : paths) {
                String fileName = fileUtil.getLastSegmentOfPath(path);
                if (isJavaFile(fileName)) {
                    String fileContent = fileUtil.readFile(path);
                    String filteredFileContent = removeUnnecessarySubstrings(fileContent);
                    objectsAsStrings.addAll(separateObjects(filteredFileContent));
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return objectsAsStrings;
    }

    private Boolean isJavaFile(String fileName){
        return fileName.endsWith(".java");
    }

    private List<String> separateObjects(String filteredFileContent){
        // Given a file as String, return all the objects it contains
        // Java files can contain multiple objects(interfaces, classes), which can be nested as well
        // This causes major complications when extracting info from them
        // Must first separate all objects as separate Strings

        List<BeginningEndIndices> indices = new ArrayList<>();
        IndexStack indexStack = new IndexStack();
        boolean isBeginningOfObject = false;
        int beginningObjectIndex = 0;

        for (int index = 0; index < filteredFileContent.length(); index++) {

            char currentChar = filteredFileContent.charAt(index);

            if (beginsWithKeywordInitials(currentChar)){
                if (spellsClass(getKeywordSubstring(index, filteredFileContent, true))){
                    isBeginningOfObject = true;
                    beginningObjectIndex = index;
                    index = index + Keyword.classKeyword.length();
                } else if (spellsInterface(getKeywordSubstring(index, filteredFileContent, false))){
                    isBeginningOfObject = true;
                    beginningObjectIndex = index;
                    index = index + Keyword.interfaceKeyword.length();
                }
            }

            if (currentChar == '{') {
                indexStack.push(beginningObjectIndex, isBeginningOfObject);
                isBeginningOfObject = false;
            } else if (currentChar == '}') {
                IndexStack.MarkedIndex markedIndex = indexStack.pop();
                if (markedIndex.isBeginningOfObject){
                    BeginningEndIndices objectIndex = new BeginningEndIndices(markedIndex.index, index);
                    indices.add(objectIndex);
                }
            }
        }

        return extractObjects(indices, filteredFileContent);
    }

    private String getKeywordSubstring(int index, String filteredFileContent, boolean isClass){
        // Must +1 to account for " "
        if (isClass) {
            return filteredFileContent.substring(index, index + Keyword.classKeyword.length() + 1);
        } else {
            return filteredFileContent.substring(index, index + Keyword.interfaceKeyword.length() + 1);
        }
    }

    private boolean beginsWithKeywordInitials(char initial){
        return initial == Keyword.classKeyword.charAt(0) || initial == Keyword.interfaceKeyword.charAt(0);
    }

    private List<String> extractObjects(List<BeginningEndIndices> indices, String filteredFileContent){
        List<String> objects = new ArrayList<>();
        for (BeginningEndIndices objectIndex: indices){
            objects.add(filteredFileContent.substring(objectIndex.beginning, objectIndex.end));
            filteredFileContent = clearStringSegment(filteredFileContent, objectIndex.beginning, objectIndex.end);
        }
        return objects;
    }

    private String clearStringSegment(String content, int beginning, int end){
        int size  = end - beginning;
        return content.substring(0, beginning)
                + fillerStringOfSize(size)
                + content.substring(end);
    }

    private String fillerStringOfSize(int size){
        return new String(new char[size]).replace("\0", " ");
    }

    private Boolean spellsClass(String str){
        return str.equals(Keyword.classKeyword + " ");
    }

    private Boolean spellsInterface(String str){
        return str.equals(Keyword.interfaceKeyword + " ");
    }

    private String removeUnnecessarySubstrings(String target){
        String withoutSingleLineComments = removeSingleLineComments(target);
        String withoutMultiLineComments = removeMultiLineComments(withoutSingleLineComments);
        return removeStrings(withoutMultiLineComments);
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
}
