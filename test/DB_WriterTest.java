import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;


/**
 * Created by Hugo Lucas on 11/23/2016.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(DB_Writer.class)
public class DB_WriterTest {

    @Mock
    private DriverManager mockDriver;

    @InjectMocks
    private DB_Writer testObject;

    @Before
    public void startUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void clockInUser() throws Exception {
        PreparedStatement statement = mock(PreparedStatement.class);
        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(any())).thenReturn(statement);

        mockStatic(DriverManager.class);
        when(DriverManager.getConnection(any())).thenReturn(connection);

        DB_Writer objectUnderTest = new DB_Writer();

        objectUnderTest.clockInUser(1,1);
    }

    @Test
    public void clockOutUser() throws Exception {

    }

    @Test
    public void addEmployee() throws Exception {

    }

    @Test
    public void addTask() throws Exception {

    }

    @Test
    public void addProject() throws Exception {

    }

    @Test
    public void createLogin() throws Exception {

    }

    @Test
    public void setWorkStatus() throws Exception {

    }

    @Test
    public void assignTaskToEmployee() throws Exception {

    }

}