package static_section.extractor.general;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ObjectInfoExtractorTest {

    String parent = "Parent";
    String child1 = "Child1";
    String child2 = "Child2";
    String method1 = "method1";
    String method2 = "method2";
    String param1 = "String param1";
    String object1 = String.format("class %s implements %s { void %s() {} String %s(%s) {} }", child1, parent, method1, method2, param1);
    String object2 = String.format("class %s implements %s { void %s() {} }", child2, parent, method2);

    ObjectInfoExtractor objectInfoExtractor = new ObjectInfoExtractorImpl();

    @Test
    public void getAllObjects_testPopulation(){
        // Given
        objectInfoExtractor.extractAllObjectInfo(Arrays.asList(object1, object2));

        // When
        List<String> objects = objectInfoExtractor.getAllObjects();

        // Then
        assertEquals(2, objects.size());
        assertTrue(objects.contains(child1));
        assertTrue(objects.contains(child2));
    }

    @Test
    public void getAllObjectMethods_testPopulation(){
        // Given
        objectInfoExtractor.extractAllObjectInfo(Arrays.asList(object1, object2));

        // When
        List<String> methods = objectInfoExtractor.getAllObjectMethods(child1);

        // Then
        assertEquals(2, methods.size());
        assertTrue(methods.contains(method1 + "()"));
        assertTrue(methods.contains(method2 + "()"));
    }

    @Test
    public void getNumberOfMethods_testCorrectAmount(){
        // Given
        objectInfoExtractor.extractAllObjectInfo(Arrays.asList(object1, object2));

        // When
        int amount = objectInfoExtractor.getNumberOfMethods();

        // Then
        assertEquals(3, amount);
    }

    @Test
    public void getNumberOfObjects_testCorrectAmount(){
        // Given
        objectInfoExtractor.extractAllObjectInfo(Arrays.asList(object1, object2));

        // When
        int amount = objectInfoExtractor.getNumberOfObjects();

        // Then
        assertEquals(2, amount);
    }
}
