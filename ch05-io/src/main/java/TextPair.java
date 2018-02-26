import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.io.WritableUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TextPair implements WritableComparable<TextPair>{
	private Text first;
	private Text second;

	public TextPair() {
		set(new Text(), new Text());
	}

	public TextPair(Text first, Text second) {
		set(first, second);
	}

	public TextPair(String first, String second) {
		set(new Text(first), new Text(second));
	}

	private void set(Text first, Text second) {
		this.first = first;
		this.second = second;
	}

	public Text getFirst() {
		return first;
	}

	public Text getSecond() {
		return second;
	}


	@Override
	public int compareTo(TextPair o) {
		int cmp = first.compareTo(o.first);
		if(cmp != 0) {
			return cmp;
		}
		return second.compareTo(o.second);
	}

	@Override
	public void write(DataOutput dataOutput) throws IOException {
		first.write(dataOutput);
		second.write(dataOutput);
	}

	@Override
	public void readFields(DataInput dataInput) throws IOException {
		first.readFields(dataInput);
		second.readFields(dataInput);
	}

	@Override
	public int hashCode() {
		return first.hashCode()*163 + second.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof TextPair) {
			TextPair tp = (TextPair) obj;
			return first.equals(tp.first) && second.equals(tp.second);
		}
		return false;
	}

	@Override
	public String toString() {
		return first + "\t" + second;
	}

	public static class Comparator extends WritableComparator {
		private static  final Text.Comparator TEXT_COMPARATOR = new Text.Comparator();

		public Comparator() {
			super(TextPair.class);
		}

		@Override
		public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
			try{
					/**
					 * first是Text类型，Text是标准的UTF-8字节流，
					 * 由一个变长整形开头表示Text中文本所需要的长度，接下来就是文本本身的字节数组
					 * decodeVIntSize返回变长整形的长度，readVInt表示文本字节数组的长度，加起来就是第一个成员name的长度
					 */
					int firstL1 = WritableUtils.decodeVIntSize(b1[s1]) + readVInt(b1,s1);
				int firstL2 = WritableUtils.decodeVIntSize(b2[s2]) + readVInt(b2,s2);
				int cmp = TEXT_COMPARATOR.compare(b1,s1,firstL1,b2,s2,firstL2);
				if(cmp != 0) {
					return cmp;
				}
				return TEXT_COMPARATOR.compare(b1,s1+firstL1,l1-firstL1, b2,s2+firstL2,l2-firstL2);

			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}
	static {
		WritableComparator.define(TextPair.class, new Comparator());
	}
	public static class FirstComparator extends WritableComparator {

		private static final Text.Comparator TEXT_COMPARATOR = new Text.Comparator();

		public FirstComparator() {
			super(TextPair.class);
		}

		@Override
		public int compare(byte[] b1, int s1, int l1,
		                   byte[] b2, int s2, int l2) {

			try {
				int firstL1 = WritableUtils.decodeVIntSize(b1[s1]) + readVInt(b1, s1);
				int firstL2 = WritableUtils.decodeVIntSize(b2[s2]) + readVInt(b2, s2);
				return TEXT_COMPARATOR.compare(b1, s1, firstL1, b2, s2, firstL2);
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
		}

		@Override
		public int compare(WritableComparable a, WritableComparable b) {
			if (a instanceof TextPair && b instanceof TextPair) {
				return ((TextPair) a).first.compareTo(((TextPair) b).first);
			}
			return super.compare(a, b);
		}
	}


}
