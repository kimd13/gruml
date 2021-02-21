package static_section.extractor.struct;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UseRelationshipContainer {

    private Set<String> used = new HashSet<>();
    private Set<String> usedBy = new HashSet<>();

    @Override
    public String toString() {
        return String.format("used: %s, usedBy: %s", used, usedBy);
    }

    public Set<String> getUsed() {
        return used;
    }

    public Set<String> getUsedBy() {
        return usedBy;
    }

    public void addToUsedBy(String user){
        this.usedBy.add(user);
    }

    public void addToUsed(String used){
        this.used.add(used);
    }

    public void addAllToUsed(List<String> used){
        this.used.addAll(used);
    }
}
