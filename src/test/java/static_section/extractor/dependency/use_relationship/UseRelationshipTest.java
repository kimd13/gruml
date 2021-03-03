package static_section.extractor.dependency.use_relationship;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static_section.extractor.general.ObjectInfoExtractor;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UseRelationshipTest {

    String parent = "Parent";
    String child1 = "Child1";
    String child2 = "Child2";
    String dependentAsVariable = String.format("class %s implements %s { %s variable = new %s(); }", child1, parent, child2, child2);
    String dependentInMethod = String.format("class %s implements %s { void method(){ return new %s(); }  }", child1, parent, child2);
    String dependentAsParam = String.format("class %s extends %s { void method(%s param){} }", child1, parent, child2);

    @Mock
    ObjectInfoExtractor objectInfoExtractor;
    UseRelationshipExtractor useRelationshipExtractor;

    @Before
    public void setUp(){
        useRelationshipExtractor = new UseRelationshipExtractorImpl(objectInfoExtractor);
        Mockito.when(objectInfoExtractor.getAllObjects()).thenReturn(Arrays.asList(child1, child2, parent));
    }

    @Test
    public void isObjectUsedByAnother_testItIsUsedAsVariable(){
        // Given
        useRelationshipExtractor.extractAllUseRelationshipInfo(Arrays.asList(dependentAsVariable));

        // When
        boolean isUsed = useRelationshipExtractor.isObjectUsedByAnother(child2);

        // Then
        assertTrue(isUsed);
    }

    @Test
    public void isObjectUsedByAnother_testItIsUsedInMethod(){
        // Given
        useRelationshipExtractor.extractAllUseRelationshipInfo(Arrays.asList(dependentInMethod));

        // When
        boolean isUsed = useRelationshipExtractor.isObjectUsedByAnother(child2);

        // Then
        assertTrue(isUsed);
    }

    @Test
    public void isObjectUsedByAnother_testItIsUsedAsMethodParam(){
        // Given
        useRelationshipExtractor.extractAllUseRelationshipInfo(Arrays.asList(dependentAsParam));

        // When
        boolean isUsed = useRelationshipExtractor.isObjectUsedByAnother(child2);

        // Then
        assertTrue(isUsed);
    }

    @Test
    public void isObjectUsedByAnother_testUsedAsInheritance(){
        // Given
        useRelationshipExtractor.extractAllUseRelationshipInfo(Arrays.asList(dependentAsVariable));

        // When
        boolean isUsed = useRelationshipExtractor.isObjectUsedByAnother(parent);

        // Then
        assertFalse(isUsed);
    }
}
