import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.StringUtils;

import java.io.*;

public class WritableTestBase {
	public static byte[] serialize(Writable writable) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream dataOut = new DataOutputStream(out);
		writable.write(dataOut);
		dataOut.close();
		return out.toByteArray();
	}

	public static byte[] deserialize(Writable writable, byte[] bytes) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		DataInputStream dataIn = new DataInputStream(in);
		writable.readFields(dataIn);
		dataIn.close();
		return bytes;
	}

	public static String serializeToString(Writable writable) throws IOException {
		return StringUtils.byteToHexString(serialize(writable));
	}

	public static String writeTo(Writable src, Writable dst) throws IOException {
		byte [] data = deserialize(dst, serialize(src));
		return StringUtils.byteToHexString(data);
	}
}
