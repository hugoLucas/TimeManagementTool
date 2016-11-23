import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Hugo on 11/22/2016.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(AddProject.class)
public class AddProjectTest {

    private DB_Writer testWriter;
    private String testProjectName;

    @Before
    public void setUp() throws Exception {
        this.testProjectName = "Test_Name";
        this.testWriter = mock(DB_Writer.class);
    }

    @After
    public void tearDown() throws Exception {
        this.testWriter = null;
        this.testProjectName = null;
    }

    @Test
    public void addNormalProjectReturnTrue() throws Exception {
        AddProject addProjectTestObject = new AddProject(this.testProjectName);

        PowerMockito.whenNew(DB_Writer.class).withNoArguments().thenReturn(this.testWriter);
        when(this.testWriter.addProject(anyString())).thenReturn(Boolean.TRUE);

        boolean result = addProjectTestObject.addProjectToDatabase();

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void nullStringProvidedReturnFalse() throws Exception {
        this.testProjectName = null;
        AddProject addProjectTestObject = new AddProject(this.testProjectName);

        PowerMockito.whenNew(DB_Writer.class).withNoArguments().thenReturn(this.testWriter);
        when(this.testWriter.addProject(anyString())).thenReturn(Boolean.TRUE);

        boolean result = addProjectTestObject.addProjectToDatabase();

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void databaseErrorReturnFalse() throws Exception {
        AddProject addProjectTestObject = new AddProject(this.testProjectName);

        PowerMockito.whenNew(DB_Writer.class).withNoArguments().thenReturn(this.testWriter);
        when(this.testWriter.addProject(anyString())).thenReturn(Boolean.FALSE);

        boolean result = addProjectTestObject.addProjectToDatabase();

        assertEquals(Boolean.FALSE, result);
    }
}