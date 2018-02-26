import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.ToolRunner;

import javax.lang.model.SourceVersion;
import javax.tools.Tool;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

public class ConfigurationPrinter extends Configured implements org.apache.hadoop.util.Tool {
	static {
		Configuration.addDefaultResource("hdfs-default.xml");
		Configuration.addDefaultResource("hdfs-site.xml");
		Configuration.addDefaultResource("yarn-default.xml");
		Configuration.addDefaultResource("yarn-site.xml");
		Configuration.addDefaultResource("mapred-default.xml");
		Configuration.addDefaultResource("mapred-site.xml");
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new ConfigurationPrinter(),args);
		System.exit(exitCode);
	}

	@Override
	public int run(String[] strings) throws Exception {
		Configuration conf = getConf();
		for(Map.Entry<String,String> entry : conf) {
			System.out.printf("%s=%s", entry.getKey(), entry.getValue());
		}
		return 0;
	}
}