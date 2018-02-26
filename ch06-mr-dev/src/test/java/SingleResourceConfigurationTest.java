import org.apache.hadoop.conf.Configuration;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class SingleResourceConfigurationTest {
	@Test
	public void get() {
		Configuration conf = new Configuration();
		conf.addResource("configuration-1.xml");
		conf.addResource("configuration-2.xml");
		System.setProperty("size", "14");
		assertThat(conf.get("color"), is("yellow"));
		assertThat(conf.get("weight"), is("heavy"));
		assertThat(conf.getInt("size", 0), is(12));
		assertThat(System.getProperty("size"), is("14"));
		assertThat(conf.get("size-weight"), is("14, heavy"));
		assertThat(conf.get("breadth", "wide"), is("wide"));
	}
}
