import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import javax.swing.*;
import java.sql.Date;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Hugo Lucas on 11/18/2016.
 */
@RunWith(PowerMockRunner.class)
public class AddEmployeeTest {

    @Mock
    private DB_Writer writer;
    private AddEmployee defaultObject;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField hireDateField = new JTextField();
        JComboBox projectSelectorBox = new JComboBox();

        firstNameField.setText("Hugo");
        lastNameField.setText("Lucas");
        hireDateField.setText("11/18/2016");
        projectSelectorBox.addItem("Developer");
        projectSelectorBox.setSelectedIndex(0);

        this.defaultObject = new AddEmployee(firstNameField, lastNameField,
                hireDateField, projectSelectorBox);
    }

    @Test
    @PowerMockIgnore("javax.management.*")
    public void actionPerformed() throws Exception {
        writer = mock(DB_Writer.class);
        when(writer.addEmployee(any(String.class), any(String.class),
                any(Date.class), any(Integer.class))).thenReturn(true);
        whenNew(DB_Writer.class).withNoArguments().thenReturn(writer);
        defaultObject.actionPerformed(null);
    }

    @After
    public void tearDown() throws Exception {
        defaultObject = null;
    }
}