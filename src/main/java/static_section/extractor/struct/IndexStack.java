package static_section.extractor.struct;

import java.util.Stack;

public class IndexStack {

    private final Stack<MarkedIndex> stack = new Stack();

    public void push(Integer index, Boolean isBeginningOfObject) {
        stack.push(new MarkedIndex(index, isBeginningOfObject));
    }

    public MarkedIndex pop() {
        return stack.pop();
    }

    public Boolean isEmpty() {
        return stack.isEmpty();
    }

    public class MarkedIndex {
        public final Integer index;
        public final Boolean isBeginningOfObject;

        public MarkedIndex(Integer index, Boolean isBeginningOfObject) {
            this.index = index;
            this.isBeginningOfObject = isBeginningOfObject;
        }
    }
}
