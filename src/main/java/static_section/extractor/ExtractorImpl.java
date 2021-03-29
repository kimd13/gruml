package static_section.extractor;

import com.google.googlejavaformat.java.Formatter;
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
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ExtractorImpl implements Extractor {

    private final Formatter formatter = new Formatter();

    private final InheritanceExtractor inheritanceExtractor = new InheritanceExtractorImpl();
    private final ObjectInfoExtractor objectInfoExtractor = new ObjectInfoExtractorImpl();
    private final UseRelationshipExtractor useRelationshipExtractor = new UseRelationshipExtractorImpl(objectInfoExtractor);

    /**
     * Calls all extractors to begin extracting info about all objects found at srcPath
     * Does the mapping from srcPath -> list of objects as strings
     */
    @Override
    public void extractAllInfo(String srcPath) {
        List<String> objectsAsStrings = getAllObjectsAsPrettyStrings(srcPath);
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

    @Override
    public int getNumberOfMethods() {
        return objectInfoExtractor.getNumberOfMethods();
    }

    @Override
    public int getNumberOfObjects() {
        return objectInfoExtractor.getNumberOfObjects();
    }

    /**
     * Handles the possibility of an exception being thrown if InheritanceExtractor tries to access null content
     */
    @Override
    public List<String> getChildren(String objectName) {
        try {
            return inheritanceExtractor.getChildren(objectName);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Handles the possibility of an exception being thrown if UseRelationshipExtractor tries to access null content
     */
    @Override
    public Set<String> getUsedByObjects(String objectName) {
        try {
            return useRelationshipExtractor.getUsedByObjects(objectName);
        } catch (Exception e) {
            return Collections.emptySet();
        }
    }

    /**
     * Given srcPath, expected list of all objects found at srcPath as pretty strings
     * Pretty: unnecessary substrings removed + formatted code
     */
    private List<String> getAllObjectsAsPrettyStrings(String srcPath) {
        List<String> objectsAsStrings = new ArrayList<>();
        try {
            List<String> paths = FileUtil.getAllFilePaths(srcPath);
            for (String path : paths) {
                String fileName = FileUtil.getLastSegmentOfPath(path);
                if (isJavaFile(fileName)) {
                    String fileContent = FileUtil.readFile(path);
                    String formattedFileContent = formatter.formatSource(fileContent);
                    String filteredFileContent = removeUnnecessarySubstrings(formattedFileContent);
                    objectsAsStrings.addAll(separateObjects(filteredFileContent));
                }
            }
        } catch (Exception e) {
            // formatter will throw when java file is incorrectly formatted
            System.out.println(e.getMessage());
        }
        return objectsAsStrings;
    }

    /**
     * Unnecessary strings include: single-line comments, multi-line comments, any strings
     */
    private String removeUnnecessarySubstrings(String target) {
        String withoutSingleLineComments = removeSingleLineComments(target);
        String withoutMultiLineComments = removeMultiLineComments(withoutSingleLineComments);
        return removeStrings(withoutMultiLineComments);
    }

    private String removeSingleLineComments(String target) {
        String singleLineCommentRegex = "//.[^\\n\\r]*";
        return RegexUtil.removeMatched(singleLineCommentRegex, target);
    }

    private String removeMultiLineComments(String target) {
        String multiLineCommentRegex = "/\\*.+?\\*/";
        return RegexUtil.removeMatched(multiLineCommentRegex, target);
    }

    private String removeStrings(String target) {
        String stringRegex = "\".+?\"";
        return RegexUtil.removeMatched(stringRegex, target);
    }

    private Boolean isJavaFile(String fileName) {
        return fileName.endsWith(".java");
    }

    /**
     * Given a file as a string, return all the objects it contains
     * Java files can contain multiple objects(interfaces, classes), which can be nested as well
     * This causes major complications when extracting info from them
     * Must first separate all objects as separate strings
     */
    private List<String> separateObjects(String filteredFileContent) {
        List<BeginningEndIndices> indices = new ArrayList<>();
        IndexStack indexStack = new IndexStack();
        boolean isBeginningOfObject = false;
        int beginningObjectIndex = 0;

        for (int index = 0; index < filteredFileContent.length(); index++) {

            char currentChar = filteredFileContent.charAt(index);

            if (beginsWithKeywordInitials(currentChar)) {
                if (spellsClass(lookaheadForKeywordSubstring(index, filteredFileContent, true))) {
                    isBeginningOfObject = true;
                    beginningObjectIndex = index;
                    index = index + Keyword.classKeyword.length();
                } else if (spellsInterface(lookaheadForKeywordSubstring(index, filteredFileContent, false))) {
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
                if (markedIndex.isBeginningOfObject) {
                    BeginningEndIndices objectIndex = new BeginningEndIndices(markedIndex.index, index);
                    indices.add(objectIndex);
                }
            }
        }

        return extractObjects(indices, filteredFileContent);
    }

    private boolean beginsWithKeywordInitials(char initial) {
        return initial == Keyword.classKeyword.charAt(0) || initial == Keyword.interfaceKeyword.charAt(0);
    }

    /**
     * Given the current index, filteredFileContent and isClass flag
     * Returns a substring from index to index + keyword (interface or class) + 1
     * Function is called when a keyword initial is found
     */
    private String lookaheadForKeywordSubstring(int index, String filteredFileContent, boolean isClass) {
        // Must +1 to account for " "
        if (isClass) {
            return filteredFileContent.substring(index, index + Keyword.classKeyword.length() + 1);
        } else {
            return filteredFileContent.substring(index, index + Keyword.interfaceKeyword.length() + 1);
        }
    }

    private Boolean spellsInterface(String str) {
        return str.equals(Keyword.interfaceKeyword + " ");
    }

    private Boolean spellsClass(String str) {
        return str.equals(Keyword.classKeyword + " ");
    }

    /**
     * Given the indices of all found objects and the filteredFileContent
     * Returns a list of separated objects
     */
    private List<String> extractObjects(List<BeginningEndIndices> indices, String filteredFileContent) {
        List<String> objects = new ArrayList<>();
        for (BeginningEndIndices objectIndex : indices) {
            objects.add(filteredFileContent.substring(objectIndex.beginning, objectIndex.end));
            filteredFileContent = clearStringSegment(filteredFileContent, objectIndex.beginning, objectIndex.end);
        }
        return objects;
    }

    /**
     * Given a string and the beginning and end indices of the wanted space to be cleared
     * Returns a string with cleared contents from those indices
     * Cleared string is of the same size as given string
     */
    private String clearStringSegment(String content, int beginning, int end) {
        int size = end - beginning;
        return content.substring(0, beginning)
                + fillerStringOfSize(size)
                + content.substring(end);
    }

    private String fillerStringOfSize(int size) {
        return new String(new char[size]).replace("\0", " ");
    }
}
