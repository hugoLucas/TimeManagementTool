import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Hugo Lucas on 11/23/2016.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(LoginUserData.class)
public class LoginUserDataTest {

    private DB_Reader mockReader;
    private LoginUserData testObj;

    @Test
    public void authenticateWithCorrectInput() throws Exception {
        int validResults [] = {1,1,1};

        this.mockReader = mock(DB_Reader.class);
        whenNew(DB_Reader.class).withAnyArguments().thenReturn(mockReader);
        when(mockReader.login_user(anyString(), anyString())).thenReturn(validResults);

        this.testObj = new LoginUserData();
        int results [] = testObj.authenticate("UN", "PW");

        assertEquals(validResults[0], results[0]);
        assertEquals(validResults[1], results[1]);
        assertEquals(validResults[2], results[2]);
    }

    @Test
    public void authenticateWithIncorrectCredentials() throws Exception {
        int validResults [] = {-1,-1,-1};

        this.mockReader = mock(DB_Reader.class);
        whenNew(DB_Reader.class).withAnyArguments().thenReturn(mockReader);
        when(mockReader.login_user(anyString(), anyString())).thenReturn(validResults);

        this.testObj = new LoginUserData();
        int results [] = testObj.authenticate("UN", "PW");

        assertEquals(validResults[0], results[0]);
        assertEquals(validResults[1], results[1]);
        assertEquals(validResults[2], results[2]);
    }

    @Test
    public void authenticateWithDatabaseError() throws Exception {
        int validResults [] = {-2,-1,-1};

        this.mockReader = mock(DB_Reader.class);
        whenNew(DB_Reader.class).withAnyArguments().thenReturn(mockReader);
        when(mockReader.login_user(anyString(), anyString())).thenReturn(validResults);

        this.testObj = new LoginUserData();
        int results [] = testObj.authenticate("UN", "PW");

        assertEquals(validResults[0], results[0]);
        assertEquals(validResults[1], results[1]);
        assertEquals(validResults[2], results[2]);
    }
}