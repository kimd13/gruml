package util.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    private static int REGEX_FLAG = Pattern.DOTALL;

    private RegexUtil(){}

    public static  List<String> getMatched(String regex, String target){
        List matches = new ArrayList();
        Pattern pattern =  Pattern.compile(regex, REGEX_FLAG);
        Matcher matcher = pattern.matcher(target);
        while (matcher.find()){
            matches.add(matcher.group());
        }
        return matches;
    }

    public static String removeMatched(String regex, String target){
        return Pattern.compile(regex, REGEX_FLAG).matcher(target).replaceAll("");
    }
}
