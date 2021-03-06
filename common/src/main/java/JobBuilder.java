import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;

import java.io.IOException;

public class JobBuilder {
    private final Class<?> driverClass;
    private final org.apache.hadoop.mapreduce.Job job;
    private final int extraArgCount;
    private final String extraArgsUsage;
    private String[] extraArgs;

    public JobBuilder(Class<?> driverClass) throws IOException {
        this(driverClass, 0, "");
    }

    public JobBuilder(Class<?> driverClass, int extraArgCount, String extraArgsUsage) throws IOException {
        this.driverClass = driverClass;
        this.extraArgCount = extraArgCount;
        this.job = new org.apache.hadoop.mapreduce.Job();
        this.job.setJarByClass(driverClass);
        this.extraArgsUsage = extraArgsUsage;
    }

    public static Job parseInputAndOutput(Tool tool, Configuration conf, String[] args) throws IOException {
        if(args.length != 2) {
            printUsage(tool, "<input> <output>");
            return null;
        }
        Job job = new Job(conf);
        job.setJarByClass(tool.getClass());
        FileInputFormat.addInputPath(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        return job;
    }

    public static void printUsage(Tool tool,String extraArgsUsage){
        System.err.printf("Usage: %s [genericOptions] %s\n\n",tool.getClass().getSimpleName(), extraArgsUsage);
        GenericOptionsParser.printGenericCommandUsage(System.err);
    }

    public JobBuilder withCommandLineArgs(String...args) throws IOException {
        Configuration conf = job.getConfiguration();
        GenericOptionsParser parser = new GenericOptionsParser(conf,args);
        String[] otherArgs = parser.getRemainingArgs();
        if(otherArgs.length < 2 && otherArgs.length > 3 + extraArgCount) {
            System.err.printf("Usage: %s [genericOptions] [-overwrite] <input path> <output path> %s\n\n",
                    driverClass.getSimpleName(), extraArgsUsage);
            GenericOptionsParser.printGenericCommandUsage(System.err);
            System.exit(-1);
        }
        int index = 0;
        boolean overwrite = false;
        if(otherArgs[index].equals("-overwrite")){
            overwrite = true;
            index++;
        }
        Path input = new Path(otherArgs[index++]);
        Path output = new Path(otherArgs[index++]);

        if(index < otherArgs.length){
            extraArgs = new String[otherArgs.length - index];
            System.arraycopy(otherArgs,index,extraArgs,0,otherArgs.length-index);
        }

        if(overwrite) {
            output.getFileSystem(conf).delete(output, true);
        }
        FileInputFormat.addInputPath(job,input);
        FileOutputFormat.setOutputPath(job, output);
        return this;
    }

    public Job build(){
        return job;
    }

    public String[] getExtraArgs() {
        return extraArgs;
    }
}
