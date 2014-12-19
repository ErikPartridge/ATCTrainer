import com.erikpartridge.aircraft.Flightplan;
import com.sun.javaws.exceptions.InvalidArgumentException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.JUnit4;

/**
 * Created by erik on 12/15/14.
 */
public class FlightplanUnitTest {

    private Flightplan flightplan = null;

    @Before
    public void setUp() throws Exception{
        flightplan = new Flightplan("KALB", "KBOS", "ALB5 ALB GDM4", "/v/ new pilot", 22000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSquawkCodeGreaterThanSeven() throws Exception{
        flightplan.setSquawk(new int[]{7,8,8,8});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSquawk() throws Exception{
        flightplan.setSquawk(new int[]{-1,0,0,0});
    }
}
