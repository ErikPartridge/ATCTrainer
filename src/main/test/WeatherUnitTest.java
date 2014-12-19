import com.erikpartridge.general.Weather;
import org.junit.Test;

public class WeatherUnitTest {

    @Test
    public void testWeather() throws Exception{
        Weather one = new Weather(29, 265);
        Weather.generateWeather("265@29");
        Weather two = Weather.weather();
        assert  one.heading() == two.heading();
        assert  one.wind() == two.wind();
    }
}
