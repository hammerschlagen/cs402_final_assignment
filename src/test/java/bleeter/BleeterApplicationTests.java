package bleeter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cs402.bleeter.BleeterApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BleeterApplication.class)
@WebAppConfiguration
public class BleeterApplicationTests {

	@Test
	public void contextLoads() {
	}

}
