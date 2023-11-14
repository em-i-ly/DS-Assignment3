Assignment 3
------------

# Team Members
Emily Gallacher

# GitHub link to your repository (if submitting through GitHub)
https://github.com/em-i-ly/DS-Assignment3.git

# Note
I had problems with my local machine for the second task of the assignment. I had to reload the shell files (zsh) leading 
to data being reset quickly. I instead decided to complete the task with 2 RaspberryPi's, which ended up working fine.

# Task 2

Executing the task on my local machine is faster (by about 13 seconds) than working with the two Raspberry Pis in the cluster. 
In the current scenario this is most likely because the workload is small enough to be handled by my machine, which makes 
the use of a cluster obsolete. I believe that clusters shine when you're dealing with big sets of data. If the data gets 
bigger, using a cluster should speed up things a lot, since we have multiple workers that we can divide our workload to. 
The divided workload is now being handled all at once. Doing the WordCount on regular machines (without clusters) will slow 
down as the data gets larger.


# Task 3

1. How does Spark optimize its file access compared to the file access in MapReduce?
> Ans: MapReduce alters data through various steps, and after each step, the data is saved to disk. Spark can carry out 
> operations concurrently and stores data in memory instead of writing to disk at each stage, resulting in a more optimized 
> performance.

2. In your implementation of WordCount (task1), did you use ReduceByKey or groupByKey method? 
   What does your preferred method do in your implementation? 
   What are the differences between the two methods in Spark?
> Ans: In my implementation, I went with the ReduceByKey method, which adds up values with the same keys locally within 
> partitions first, and then combines them into bigger sums during the shuffle phase. This helps to keep data shuffling 
> across the network to a minimum, making things more efficient. GroupByKey doesn't do this local aggregation. Instead, 
> it sends all the data related to a key across the network. This could slow things down by a lot. 

3. Explain what Resilient Distributed Dataset (RDD) is and the benefits it brings to the classic MapReduce model.
> Ans: An RDD (Resilient Distributed Dataset), is a collection of elements distributed across nodes in a cluster. This 
> distribution facilitates concurrent operations and data sharing, contributing to enhanced efficiency by utilizing memory 
> for accelerated computations on distinct data segments. Another benefit is fault tolerance, which enables the recovery of 
> data in the event of failures. These features contribute to the advantages RDD brings to the classic MapReduce model.

4. Imagine that you have a large dataset that needs to be processed in parallel. 
   How would you partition the dataset efficiently and keep control over the number of outputs created at the end of the execution?
> Ans: When partitioning the dataset, it is important that the partitions are evenly distributed over the workers in the cluster.
> Partitions that are too large could cause a resource bottleneck and partitions that are too small can lead to excessive overhead.
> To keep control over the number of outputs, there are numerous methods that can be used. In my implementation for example, I used
> Coalesce, which minimizes the number of partitions without shuffling data across the cluster. Another way is Repartition, which
> can be used to increase or decrease the number of partitions, but it involves heavy data shuffling across the cluster.

  If a task is stuck on the Spark cluster due to a network issue that the cluster had during execution, 
  which methods can be used to retry or restart the task execution on a node?
> Ans: If a task ends up being stuck, we do not have to retry or restart the task execution since this is being done automatically 
> by spark. If necessary spark can rerun the failed task for us by rerouting it to a different node. Spark has a specified tolerance 
> rate of tasks/amount of tasks that can go wrong, which we can adjust manually in the files. We can reduce the amount of tasks that 
> end up being stuck in the cluster, by properly handling and catching errors. With a custom logic the code will now rerun, when such 
> an error occurs.

sources used:
https://sparkbyexamples.com/spark/spark-groupbykey-vs-reducebykey/
https://towardsdataengineering.quora.com/What-is-the-difference-between-GroupByKey-and-ReduceByKey-in-Spark
https://spark.apache.org/docs/latest/rdd-programming-guide.html
https://www.quora.com/What-are-the-main-differences-between-Sparks-RDD-and-Hadoop-s-HDFS-MapReduce-Hive
https://www.oreilly.com/library/view/apache-spark-quick/9781789349108/55c256fc-8b33-473c-b6c6-a4118a25192d.xhtml