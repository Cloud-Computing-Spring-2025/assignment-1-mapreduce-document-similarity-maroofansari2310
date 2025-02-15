package com.example;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DocumentSimilarityReducer extends Reducer<Text, Text, Text, Text> {
    
    private final Map<String, Set<String>> documentWordMap = new HashMap<>();
    
    @Override
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Set<String> wordsSet = new HashSet<>();
        for (Text textValue : values) {
            wordsSet.addAll(Arrays.asList(textValue.toString().split(",")));
        }

        documentWordMap.put(key.toString(), wordsSet);
        for (Map.Entry<String, Set<String>> entry : documentWordMap.entrySet()) {
            String docExisting = entry.getKey();
            Set<String> wordSetExisting = entry.getValue();
            if (docExisting.equals(key.toString())) {
                continue;
            }
            
            Set<String> intersect = new HashSet<>(wordSetExisting);
            intersect.retainAll(wordsSet);
            Set<String> union = new HashSet<>(wordSetExisting);
            union.addAll(wordsSet);
            double docSimilarity = (double) intersect.size() / union.size();
            int similarPercentage = (int) Math.round(docSimilarity * 100);
            if (similarPercentage >= 50) {
                String doc1 = docExisting;
                String doc2 = key.toString();
                context.write(new Text("(" + doc2 + ", " + doc1 + ")"), new Text("-> " + similarPercentage + "%"));
            }
        }
    }
}