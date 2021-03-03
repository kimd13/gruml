package static_section.extractor.dependency.inheritance;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class InheritanceExtractorTest {

    String parent = "Parent";
    String child1 = "Child1";
    String child2 = "Child2";
    String implementsObject1 = String.format("class %s implements %s {}", child1, parent);
    String implementsObject2 = String.format("class %s implements %s {}", child2, parent);
    String extendsObject1 = String.format("class %s extends %s {}", child1, parent);
    String extendsObject2 = String.format("class %s extends %s {}", child2, parent);

    InheritanceExtractor inheritanceExtractor = new InheritanceExtractorImpl();

    @Test
    public void getChildren_testChildrenOfInterfaces() {
        // Given
        inheritanceExtractor.extractAllInheritanceInfo(Arrays.asList(implementsObject1, implementsObject2));

        // When
        List<String> children = inheritanceExtractor.getChildren(parent);

        // Then
        assertEquals(children.size(), 2);
        assertEquals(children.get(0), child1);
        assertEquals(children.get(1), child2);
    }

    @Test
    public void getChildren_testChildrenOfClasses() {
        // Given
        inheritanceExtractor.extractAllInheritanceInfo(Arrays.asList(extendsObject1, extendsObject2));

        // When
        List<String> children = inheritanceExtractor.getChildren(parent);

        // Then
        assertEquals(children.size(), 2);
        assertEquals(children.get(0), child1);
        assertEquals(children.get(1), child2);
    }


    @Test
    public void isObjectInheritedFrom_testInherited(){
        // Given
        inheritanceExtractor.extractAllInheritanceInfo(Arrays.asList(implementsObject1));

        // When
        boolean isInheritedFrom = inheritanceExtractor.isObjectInheritedFrom(parent);

        // Then
        assertTrue(isInheritedFrom);
    }

    @Test
    public void checkIfInterface_testIsInterface(){
        // Given
        inheritanceExtractor.extractAllInheritanceInfo(Arrays.asList(implementsObject1));

        // When
        boolean isInterface = inheritanceExtractor.checkIfInterface(parent);

        // Then
        assertTrue(isInterface);
    }

    @Test
    public void checkIfInterface_testIsNotInterface(){
        // Given
        inheritanceExtractor.extractAllInheritanceInfo(Arrays.asList(extendsObject1));

        // When
        boolean isInterface = inheritanceExtractor.checkIfInterface(parent);

        // Then
        assertFalse(isInterface);
    }
}
