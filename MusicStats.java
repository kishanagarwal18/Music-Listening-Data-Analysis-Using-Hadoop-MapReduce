import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.HashSet;

public class MusicStats {

    public static class MusicMapper extends Mapper<Object, Text, Text, Text> {
        private Text trackEvent = new Text();
        private Text userTrackEvent = new Text();

        @Override
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split(",");

            // Skip header line
            if (fields[0].equals("user")) {
                return;
            }

            if (fields.length == 3) {
                String user = fields[0];
                String track = fields[1];
                String event = fields[2];

                // Key: track, Value: user:event
                trackEvent.set(track);
                userTrackEvent.set(user + ":" + event);
                context.write(trackEvent, userTrackEvent);
            }
        }
    }

    public static class MusicReducer extends Reducer<Text, Text, Text, Text> {
        private Text result = new Text();

        @Override
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            HashSet<String> uniqueListeners = new HashSet<>();
            int sharedCount = 0;
            int listenedRadioCount = 0;
            int skippedRadioCount = 0;
            int totalListens = 0;

            for (Text val : values) {
                String[] userEvent = val.toString().split(":");
                String user = userEvent[0];
                String event = userEvent[1];

                // Track unique listeners
                uniqueListeners.add(user);

                // Count events
                switch (event) {
                    case "shared":
                        sharedCount++;
                        break;
                    case "listened_radio":
                        listenedRadioCount++;
                        break;
                    case "skipped_radio":
                        skippedRadioCount++;
                        break;
                }
                totalListens++;
            }

            // Prepare output
            String output = String.format("UniqueListeners=%d, Shared=%d, ListenedRadio=%d, SkippedRadio=%d, TotalListens=%d",
                    uniqueListeners.size(), sharedCount, listenedRadioCount, skippedRadioCount, totalListens);
            result.set(output);
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: MusicStats <in> <out>");
            System.exit(2);
        }

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Music Stats");

        job.setJarByClass(MusicStats.class);
        job.setMapperClass(MusicMapper.class);
        job.setReducerClass(MusicReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
