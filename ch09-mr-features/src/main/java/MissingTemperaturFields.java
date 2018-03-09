import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class MissingTemperaturFields extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        if(args.length != 1) {
            JobBuilder.printUsage(this,"<job ID>");
            return -1;
        }

        String jobId = args[0];
        Cluster cluster = new Cluster(getConf());
        Job job = cluster.getJob(JobID.forName(jobId));
        if(job == null) {
            System.err.printf("No job with %s found\n", jobId);
            return -1;
        }
        if(!job.isComplete()) {
            System.err.printf("Job %s is not complete\n", jobId);
            return -1;
        }

        Counters counters = job.getCounters();
        long missing = counters.findCounter(MaxTemperatureWithCounters.Temperature.MISSING).getValue();
        long total = counters.findCounter(TaskCounter.MAP_INPUT_RECORDS).getValue();
        System.out.printf("Record with missing temperature fields : %.2f%%\n",100.0*missing/total);
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new MissingTemperaturFields(), args);
        System.exit(exitCode);
    }
}
