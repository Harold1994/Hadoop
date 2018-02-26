package v2;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Test;
import v1.MaxTemperatureMapper;

import java.io.IOException;

import static org.junit.Assert.*;

public class MaxTemperatureMapperTest {
	@Test
	public void processvalidRecord() throws IOException {
		Text value = new Text("0043011990999991950051518004+68750+023550FM-12+038299999V0203201N00261220001CN9999999N9-00111+99999999999");
		new MapDriver<LongWritable, Text, Text, IntWritable>()
				.withMapper(new v2.MaxTemperatureMapper())
				.withInput(new LongWritable(0), value)
				.withOutput(new Text("1950"), new IntWritable(-11))
				.runTest();
	}

	@Test
	public void ignoreMissingTemperatureRecord() throws IOException {
		Text value = new Text("0067011990999991950051507004+68750+023550FM-12+038299999V0203301N00671220001CN9999999N9+99991+99999999999");
		new MapDriver<LongWritable, Text, Text, IntWritable>()
				.withMapper(new v2.MaxTemperatureMapper())
				.withInput(new LongWritable(0),value)
				.runTest();
	}
}