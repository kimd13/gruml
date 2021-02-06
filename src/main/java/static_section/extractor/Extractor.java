package static_section.extractor;

import util.file.FileUtil;
import util.regex.RegexUtil;

public abstract class Extractor {

    protected RegexUtil regexUtil = RegexUtil.getInstance();
    protected FileUtil fileUtil = FileUtil.getInstance();

    protected String removeSingleLineComments(String target){
        String singleLineCommentRegex = "//.[^\\n\\r]*";
        return regexUtil.removeMatched(singleLineCommentRegex, target);
    }

    protected String removeMultiLineComments(String target){
        String multiLineCommentRegex = "/\\*.+?\\*/";
        return regexUtil.removeMatched(multiLineCommentRegex, target);
    }

    protected String removeStrings(String target){
        String stringRegex = "\".+?\"";
        return regexUtil.removeMatched(stringRegex, target);
    }

    protected String removeUnnecessarySubstrings(String target){
        String withoutSingleLineComments = removeSingleLineComments(target);
        String withoutMultiLineComments = removeMultiLineComments(withoutSingleLineComments);
        return removeStrings(withoutMultiLineComments);
    }
}
