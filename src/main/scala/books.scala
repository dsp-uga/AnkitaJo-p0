/**Implementation of Project 0
 *
 * Work done by Ankita Joshi
 *
 */

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import dsp.WordCount

import spray.json._
import DefaultJsonProtocol._
import java.io._

object books {

  /*
  Contains a main fuinction which calls calls the methods defined in the class file - WordCount.scala.
  */

  def main(args: Array[String]): Unit ={
    
    //Create the sparkcontext
    val conf = new SparkConf().setAppName("project0")
    val sc = new SparkContext(conf)

    //Path to where all text files are located input as command-line argument
    val path = args(0)
    val stoppath = args(1)

    //Read the books and create RDD of (key,value) pair with as (index,text)
    val documents = sc.wholeTextFiles(path).zipWithIndex.map{ case((a,b),c) => (c.toInt,b)}
    val stopword = sc.textFile(stoppath)
    

    
    // From class WordCount call each of the subproject methods

    //subprojectA
    val sp1 = WordCount.subprojectA(documents)

    //implementation of writing a json file 
    val writer = new PrintWriter(new File("sp1.json"))
    writer.write("{"+"\n")
    for((k,v) <- sp1){

      writer.write("\""+v.toString+"\""+":"+k+","+"\n")

    }
    writer.write("}")
    writer.close()

    //subprojectB
    val stopwords = sc.broadcast(stopword.collect())
    val sp2 = WordCount.subprojectB(documents,stopwords)
    val writer2 = new PrintWriter(new File("sp2.json"))

    writer2.write("{"+"\n")
    for((k,v) <- sp2){

      writer2.write("\""+v.toString+"\""+":"+k+","+"\n")

    }
    writer2.write("}")
    writer2.close()

    //subprojectC
    val sp3 = WordCount.subprojectC(documents,stopwords)
    val writer3 = new PrintWriter(new File("sp3.json"))
    writer3.write("{"+"\n")
    for((k,v) <- sp3){

      writer3.write("\""+v.toString+"\""+":"+k+","+"\n")

    }
    writer3.write("}")
    writer3.close()

    //subprojectD

    val sp4 = WordCount.subprojectD(documents,stopwords)
    val writer4 = new PrintWriter(new File("sp4.json"))
    writer4.write("{"+"\n")
    for(i <- sp4){
      
      for((k,v) <- i)  
        writer4.write("\""+k.toString+"\""+":"+v+","+"\n")

    }
    writer4.write("}")

    writer4.close()


  }
}
