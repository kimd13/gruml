package dependency.inheritance;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InheritanceDependencyDiagramImplTest {

    private InheritanceDependencyDiagramImpl inheritanceDependencyDiagram = new InheritanceDependencyDiagramImpl();

    @Test
    public void removeUnnecessarySubstrings_testRemovalOfSingleLineComments(){
        // Given
        String expected = "\nClass test";
        String target = String.format("//this is a comment%s", expected);

        // When
        String commentsRemoved = inheritanceDependencyDiagram.removeUnnecessarySubstrings(target);

        // Then
        assertEquals(expected, commentsRemoved);
    }

    @Test
    public void removeUnnecessarySubstrings_testRemovalOfMultiLineComments(){
        String expected = "Class test";
        String target = String.format("/*this\nis\na\ncomment*/%s", expected);

        // When
        String commentsRemoved = inheritanceDependencyDiagram.removeUnnecessarySubstrings(target);

        // Then
        assertEquals(expected, commentsRemoved);
    }

    @Test
    public void removeUnnecessarySubstrings_testRemovalOfStrings(){
        // Given
        String expected = "Class test";
        String target = String.format("\"this is a string\"%s", expected);

        // When
        String commentsRemoved = inheritanceDependencyDiagram.removeUnnecessarySubstrings(target);

        // Then
        assertEquals(expected, commentsRemoved);
    }

    @Test
    public void getInterfaceSubstrings_testReturnedValuesMatch(){
        // Given

        // When

        // Then
    }

    @Test
    public void getClassSubstrings_testReturnedValuesMatch(){
        // Given

        // When

        // Then
    }
}
