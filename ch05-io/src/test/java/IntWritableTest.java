import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.util.StringUtils;
import org.junit.Test;
import org.omg.CORBA.INTERNAL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import java.io.IOException;

public class IntWritableTest extends WritableTestBase {
	@Test
	public void walkthroughWithNoArgsConstructor() throws IOException {
		IntWritable writable = new IntWritable();
		writable.set(163);
		checkWalkthrough(writable);
	}
	@Test
	public void walkthroughWithValueConstructor() throws IOException {
		IntWritable writable = new IntWritable(163);
		checkWalkthrough(writable);
	}
	private void checkWalkthrough(IntWritable writable) throws IOException {
		byte[] bytes = serialize(writable);
		assertThat(bytes.length, is(4));
		assertThat(StringUtils.byteToHexString(bytes), is("000000a3"));
		IntWritable newWritable = new IntWritable();
		deserialize(newWritable, bytes);
		assertThat(newWritable.get(), is(163));
	}

	@Test
	@SuppressWarnings("unchecks")
	public void comparator() throws IOException {
		RawComparator<IntWritable> comparator = WritableComparator.get(IntWritable.class);
		IntWritable w1 = new IntWritable(163);
		IntWritable w2 = new IntWritable(67);
		assertThat(comparator.compare(w1,w2), greaterThan(0));
		byte[] b1 = serialize(w1);
		byte[] b2 = serialize(w2);
		assertThat(comparator.compare(b1,0,b1.length, b2,0, b2.length), greaterThan(0));

	}
	@Test
	public void test() throws IOException {
		IntWritable src = new IntWritable(163);
		IntWritable dst = new IntWritable();
		assertThat(writeTo(src,dst), is("000000a3"));
		assertThat(dst.get(), is(src.get()));
	}
}
