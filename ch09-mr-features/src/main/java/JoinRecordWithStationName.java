import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class JoinRecordWithStationName extends Configured implements Tool {

    public static class KeyPartitioner extends Partitioner<TextPair, Text> {

        @Override
        public int getPartition(TextPair textPair, Text text, int i) {
            return (textPair.getFirst().hashCode() & Integer.MAX_VALUE) % i;
        }
    }
    @Override
    public int run(String[] strings) throws Exception {
        if(strings.length != 3) {
            JobBuilder .printUsage(this, "<ncdc input> <station input> <output>");
            return -1;
        }

        Job job  = new Job(getConf(),"Join weather record with station names");

        job.setJarByClass(getClass());
        Path ncdcInputPath = new Path(strings[0]);
        Path stationINputPath = new Path(strings[1]);
        Path outputPath = new Path(strings[2]);

        MultipleInputs.addInputPath(job,ncdcInputPath, TextInputFormat.class,JoinRecordMapper.class);
        MultipleInputs.addInputPath(job, stationINputPath, TextInputFormat.class, JoinStationMapper.class);
        FileOutputFormat.setOutputPath(job,outputPath);

        job.setPartitionerClass(KeyPartitioner.class);
        job.setGroupingComparatorClass(TextPair.FirstComparator.class);
        job.setMapOutputKeyClass(TextPair.class);
        job.setReducerClass(JoinReducer.class);
        job.setOutputKeyClass(Text.class);
        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new JoinRecordWithStationName(), args);
        System.exit(exitCode);
    }
}
