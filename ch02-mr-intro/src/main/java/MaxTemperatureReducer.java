import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class MaxTemperatureReducer extends Reducer<Text,IntWritable, Text,IntWritable> {
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int maxvalue = Integer.MIN_VALUE;
        for(IntWritable value : values) {
            maxvalue = Math.max(value.get(),maxvalue);
        }
        context.write(key,new IntWritable(maxvalue));
    }
}
