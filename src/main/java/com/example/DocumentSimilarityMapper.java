package com.example;
import java.io.IOException;
import java.util.HashSet;
import java.util.StringTokenizer;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class DocumentSimilarityMapper extends Mapper<Object, Text, Text, Text> {
    
    public void map(Object Key, Text value, Context context) throws IOException, InterruptedException{
        
        String fileNames = ((FileSplit) context.getInputSplit()).getPath().getName();
        HashSet<String> wordsSet = new HashSet<>();
        StringTokenizer stringTokenizer = new StringTokenizer(value.toString());

        while (stringTokenizer.hasMoreTokens()) {
            wordsSet.add(stringTokenizer.nextToken().toLowerCase());
        }
        context.write(new Text(fileNames), new Text(String.join(",", wordsSet)));

    }
}