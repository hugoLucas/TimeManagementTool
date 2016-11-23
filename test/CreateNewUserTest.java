import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Hugo Lucas on 11/22/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(CreateNewUser.class)
public class CreateNewUserTest {

    private DB_Writer testWriter;

    private String testFirstName;
    private String testLastName;
    private String testDateHired;
    private String testUserName;
    private String testPassWord;

    @Before
    public void setUp() throws Exception {
        testWriter = mock(DB_Writer.class);

        testFirstName = "Hugo";
        testLastName = "Lucas";
        testDateHired = "03/02/2016";
        testUserName = "hugof";
        testPassWord = "passw0rd";
    }

    @After
    public void tearDown() throws Exception {
        testWriter = null;

        testFirstName = null;
        testLastName = null;
        testDateHired = null;
        testUserName = null;
        testPassWord = null;
    }

    @Test
    public void normalInputReturnTrue() throws Exception {
        whenNew(DB_Writer.class).withNoArguments().thenReturn(testWriter);
        when(testWriter.createLogin(anyString(), anyString(), anyObject(),
                anyString(), anyString())).thenReturn(Boolean.TRUE);

        CreateNewUser testObj = new CreateNewUser(testFirstName, testLastName,
                testDateHired, testUserName, testPassWord);
        boolean result = testObj.createNewLogin();

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void invalidDateReturnFalse() throws Exception {
        this.testDateHired = "03/99/7360";
        CreateNewUser testObj = new CreateNewUser(testFirstName, testLastName,
                testDateHired, testUserName, testPassWord);
        boolean result = testObj.createNewLogin();
        assertEquals(Boolean.FALSE, result);

        this.testDateHired = "39/01/1999";
        testObj = new CreateNewUser(testFirstName, testLastName,
                testDateHired, testUserName, testPassWord);
        result = testObj.createNewLogin();
        assertEquals(Boolean.FALSE, result);

        this.testDateHired = "03021992";
        testObj = new CreateNewUser(testFirstName, testLastName,
                testDateHired, testUserName, testPassWord);
        result = testObj.createNewLogin();
        assertEquals(Boolean.FALSE, result);

        this.testDateHired = "03-02-1992";
        testObj = new CreateNewUser(testFirstName, testLastName,
                testDateHired, testUserName, testPassWord);
        result = testObj.createNewLogin();
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void databaseErrorReturnFalse() throws Exception {
        whenNew(DB_Writer.class).withNoArguments().thenReturn(testWriter);
        when(testWriter.createLogin(anyString(), anyString(), anyObject(),
                anyString(), anyString())).thenReturn(Boolean.FALSE);

        CreateNewUser testObj = new CreateNewUser(testFirstName, testLastName,
                testDateHired, testUserName, testPassWord);
        boolean result = testObj.createNewLogin();

        assertEquals(Boolean.FALSE, result);
    }
}