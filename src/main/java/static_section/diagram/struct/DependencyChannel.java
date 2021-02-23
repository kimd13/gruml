package static_section.diagram.struct;

public class DependencyChannel {

    private final int len;
    private final int selfMarkerRow;

    public DependencyChannel(int len, int selfMarkerRow){
        this.len = len;
        this.selfMarkerRow = selfMarkerRow;
    }

    public int getLen() {
        return len;
    }

    public int getSelfMarkerRow() {
        return selfMarkerRow;
    }
}
