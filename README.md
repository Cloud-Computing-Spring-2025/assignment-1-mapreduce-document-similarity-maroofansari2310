# **Document Similarity Using Hadoop MapReduce**

## **Overview**

This project leverages Hadoop MapReduce to compute the Jaccard Similarity between multiple documents stored in HDFS, where the Mapper processes each document and the Reducer determines similarity between document pairs.

## **Approach and Implementation**  

### **Mapper**  
The Mapper extracts unique words from each document, associates them with their respective filenames, and outputs key-value pairs where the key is the document name and the value is a comma-separated list of words.  

### **Reducer**  
The Reducer aggregates word sets for all documents and computes the Jaccard similarity between document pairs by determining the ratio of shared words (intersection) to total unique words (union).  

```
The Jaccard Similarity is calculated as follows:
```
Jaccard Similarity = |A ∩ B| / |A ∪ B|
```
Where:
- `|A ∩ B|` is the number of words common to both documents
- `|A ∪ B|` is the total number of unique words in both documents

## Example Calculation

Consider two documents:
 
**doc1.txt words**: `{hadoop, is, a, distributed, system}`
**doc2.txt words**: `{hadoop, is, used, for, big, data, processing}`

- Common words: `{hadoop, is}`
- Total unique words: `{hadoop, is, a, distributed, system, used, for, big, data, processing}`

Jaccard Similarity calculation:
```
|A ∩ B| = 2 (common words)
|A ∪ B| = 10 (total unique words)

Jaccard Similarity = 2/10 = 0.2 or 20%
```

## Setup and Execution

### 1. **Start the Hadoop Cluster**

Run the following command to start the Hadoop cluster:

```bash
docker compose up -d
```

### 2. **Build the Code**

Build the code using Maven:

```bash
mvn install
```

### 3. **Copy JAR to Docker Container**

Copy the JAR file to the Hadoop ResourceManager container:

```bash
docker cp /workspaces/assignment-1-mapreduce-document-similarity-maroofansari2310/target/DocumentSimilarity-0.0.1-SNAPSHOT.jar resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 4. **Move Dataset to Docker Container**

Copy the dataset to the Hadoop ResourceManager container:

```bash
docker cp input/ resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 5. **Connect to Docker Container**

Access the Hadoop ResourceManager container:

```bash
docker exec -it resourcemanager /bin/bash
```

Navigate to the Hadoop directory:

```bash
cd /opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 6. **Set Up HDFS**

Create a folder in HDFS for the input dataset:

```bash
hadoop fs -mkdir -p /input/dataset
```

Copy the input dataset to the HDFS folder:

```bash
hadoop fs -put ./input /input/dataset
```

### 7. **Execute the MapReduce Job**

Run your MapReduce job using the following command:

```bash
hadoop jar DocumentSimilarity-0.0.1-SNAPSHOT.jar com.example.controller.DocumentSimilarityDriver /input/dataset/input /output
```

### 8. **View the Output**

To view the output of your MapReduce job, use:

```bash
hadoop fs -cat /output/*
```

### 9. **Copy Output from HDFS to Local OS**

To copy the output from HDFS to your local machine:

1. Use the following command to copy from HDFS:
    ```bash
    hdfs dfs -get /output /opt/hadoop-3.2.1/share/hadoop/mapreduce/
    ```

2. use Docker to copy from the container to your local machine:
   ```bash
   exit 
   ```
    ```bash
    docker cp resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/output/ output/
    ``` 

### **Sample Input**  
#### **Example Documents**  

##### **doc1.txt**  
```
hadoop is a distributed system
```
##### **doc2.txt**  
```
hadoop and big data is used for analysis and processing
```
##### **doc3.txt**  
```
big data is important for data analysis and processing
```
##### **doc4.txt**  
```
hadoop is used for big data processing
```
### **Sample Output**  
(doc3.txt, doc2.txt)    -> 70%
(doc4.txt, doc3.txt)    -> 50%
(doc4.txt, doc2.txt)    -> 78%
---

### **Challenges Faced**
```
    Troubleshooting Hadoop jobs is challenging because of their distributed execution. Resolving errors requires analyzing logs across multiple nodes, making the debugging process lengthy and complex.
```