package com.assignment3.spark;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.*;
import org.apache.spark.SparkFiles;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import scala.Tuple2;

import org.apache.spark.sql.Dataset;

public class WordCount {

    static class Filter implements FlatMapFunction<String, String> {
        @Override
        public Iterator<String> call(String s) {
            String[] subStrings = s.split("\\s+");
            return Arrays.asList(subStrings).iterator();
        }
    }

    public static void main(String[] args) {
        //String textFilePath = "input/pigs.txt"; // file path to input for task1
        String textFilePath = "hdfs://172.20.10.2:9000/sparkApp/input/pigs.txt"; // file path to input for task2
        //SparkConf conf = new SparkConf().setAppName("WordCountWithSpark").setMaster("local[*]"); // task1
        SparkConf conf = new SparkConf().setAppName("WordCountWithSpark").setMaster("spark://172.20.10.2:7077"); //task2, set master to master IP address
        JavaSparkContext sparkContext = new JavaSparkContext(conf);
        JavaRDD<String> textFile = sparkContext.textFile(textFilePath);
        JavaRDD<String> words = textFile.flatMap(new Filter());

        //stores pairs of values in a JavaPairRDD (a pair being a tuple with each word in the pigs.txt file being mapped to 1)
        JavaPairRDD mappedPairs = words.mapToPair(word -> new Tuple2<>(word,1))
                //takes the key of two identical words and adds them together
                .reduceByKey((x, y) -> (int) x + (int) y);

        //save all tuples in a list for later use (printing)
        List<Tuple2<String, Integer>> pairs = mappedPairs.collect();

        //print the output on the terminal of the Spark Master
        pairs.forEach(pair -> System.out.println("(" + pair._1() + "," + pair._2() + ")"));

        //coalesce takes all partitions and saves them in a single file. output folder created in current directory of local machine
        //mappedPairs.coalesce(1).saveAsTextFile("output"); //task1

        //output folder created on hadoop with the path "/sparkApp/output"
        mappedPairs.coalesce(1).saveAsTextFile("hdfs://172.20.10.2:9000/sparkApp/output"); //task2

        sparkContext.stop();
        sparkContext.close();

    }
}
