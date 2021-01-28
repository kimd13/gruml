package dependency.inheritance;

import util.file.FileUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InheritanceDependencyDiagramImpl implements InheritanceDependencyDiagram {

    private HashMap<String, List> inheritanceMap = new HashMap<>();
    private RegexMatcher regexMatcher = new RegexMatcher();

    @Override
    public void populateDiagram(String srcPath) {
        try {
            String fileContent = FileUtil.getInstance().readFile(srcPath);

            // Must get rid of all comments
            // Check for extend and implements <- inheritance

            //List<String> comments = regexMatcher.getCommentedSectionSubstrings(fileContent);
            List<String> classes = regexMatcher.getClassSubstrings(fileContent);
            List<String> interfaces = regexMatcher.getInterfaceSubstrings(fileContent);
            System.out.println(classes);
            System.out.println(interfaces);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List getInheritanceList(String name) {
        return null;
    }

    private class RegexMatcher{
        public List<String> getCommentedSectionSubstrings(String target) {
            return getMatchedSubstrings( "^/\\*[^]*\\*/", target);
        }

        public List<String> getClassSubstrings(String target) {
            return getMatchedSubstrings("class [^ ]*", target);
        }

        public List<String> getInterfaceSubstrings(String target) {
            return getMatchedSubstrings("interface [^ ]*", target);
        }

        private List<String> getMatchedSubstrings(String regex, String target){
            List matches = new ArrayList();
            Pattern pattern =  Pattern.compile(regex);
            Matcher matcher = pattern.matcher(target);
            while (matcher.find()){
                matches.add(matcher.group());
            }
            return matches;
        }
    }
}
