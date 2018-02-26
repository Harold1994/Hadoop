import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class ShowFIleStatusTest {
	private MiniDFSCluster cluster; // use an in-process HDFS cluster for testing
	private FileSystem fs;

	@Before
	public void setUp() throws IOException {
		Configuration conf = new Configuration();
		if (System.getProperty("test.build.data") == null) {
			System.setProperty("test.build.data", "/tmp");
		}
		cluster = new MiniDFSCluster.Builder(conf).build();
		fs = cluster.getFileSystem();
		OutputStream out = fs.create(new Path("/dir/file"));
		out.write("content".getBytes("UTF-8"));
		out.close();
	}

	@After
	public void tearDown() throws IOException {
		if (fs != null) { fs.close(); }
		if (cluster != null) { cluster.shutdown(); }
	}

	@Test(expected = FileNotFoundException.class)
	public void throwsFileNotFoundForNonExistentFile() throws IOException {
		fs.getFileStatus(new Path("no-such-file"));
	}

	@Test
	public void fileStatusForFile() throws IOException {
		Path file = new Path("/dir/file");
		FileStatus stat = fs.getFileStatus(file);
		MatcherAssert.assertThat(stat.getPath().toUri().getPath(), CoreMatchers.is("/dir/file"));
		MatcherAssert.assertThat(stat.isDirectory(), CoreMatchers.is(false));
		MatcherAssert.assertThat(stat.getLen(), CoreMatchers.is(7L));
		MatcherAssert.assertThat(stat.getReplication(), CoreMatchers.is((short) 1));
		MatcherAssert.assertThat(stat.getBlockSize(), CoreMatchers.is(128 * 1024 * 1024L));
		MatcherAssert.assertThat(stat.getOwner(), CoreMatchers.is(System.getProperty("user.name")));
		MatcherAssert.assertThat(stat.getGroup(), CoreMatchers.is("supergroup"));
		MatcherAssert.assertThat(stat.getPermission().toString(), CoreMatchers.is("rw-r--r--"));
	}

	@Test
	public void fileStatusForDirectory() throws IOException {
		Path dir = new Path("/dir");
		FileStatus stat = fs.getFileStatus(dir);
		MatcherAssert.assertThat(stat.getPath().toUri().getPath(), CoreMatchers.is("/dir"));
		MatcherAssert.assertThat(stat.isDirectory(), CoreMatchers.is(true));
		MatcherAssert.assertThat(stat.getLen(), CoreMatchers.is(0L));
		MatcherAssert.assertThat(stat.getReplication(), CoreMatchers.is((short) 0));
		MatcherAssert.assertThat(stat.getBlockSize(), CoreMatchers.is(0L));
		MatcherAssert.assertThat(stat.getOwner(), CoreMatchers.is(System.getProperty("user.name")));
		MatcherAssert.assertThat(stat.getGroup(), CoreMatchers.is("supergroup"));
		MatcherAssert.assertThat(stat.getPermission().toString(), CoreMatchers.is("rwxr-xr-x"));
	}

}
