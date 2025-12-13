package org.example;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class Main {
    public static void main(String[] args) {

        SparkSession sparkSession = SparkSession.builder()
                .master("local[*]")
                .appName("spark-example")
                .getOrCreate();

        // create a tiny DataFrame in memory
        Dataset<Row> df = sparkSession.createDataFrame(
                java.util.Arrays.asList(
                        new Person("Alice", 25),
                        new Person("Bob", 30),
                        new Person("Charlie", 35)
                ),
                Person.class
        );

        // print the first 20 rows
        df.show(20);

        // print full column content (no truncation)
        df.show(20, false);


        sparkSession.close();

    }
    public static class Person implements java.io.Serializable {
        private String name;
        private int age;

        public Person() {}

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() { return name; }
        public int getAge() { return age; }

        public void setName(String name) { this.name = name; }
        public void setAge(int age) { this.age = age; }
    }
}