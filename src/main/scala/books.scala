


import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import dsp.WordCount

import spray.json._
import DefaultJsonProtocol._ 
object books {

  def main(args: Array[String]): Unit ={
    
    val conf = new SparkConf().setAppName("project0")
    val sc = new SparkContext(conf)

    //Path to where all text files are located input as command-line argument
    val path = args(0)
    val stoppath = args(1)

    //Read the books and create RDD of (key,value) pair with as (index,text)
    val documents = sc.wholeTextFiles(path).zipWithIndex.map{ case((a,b),c) => (c.toInt,b)}
    val stopword = sc.textFile(stoppath)
    //documents.foreach(println(_))

    
    // From class WordCount call each of the subproject methods
    //val sp1 = WordCount.subprojectA(documents)
    //val jsonAst =sp1.toJson
    //println(jsonAst)

    val stopwords = sc.broadcast(stopword.collect())
    //val sp2 = WordCount.subprojectB(documents,stopwords)
    //val sp3 = WordCount.subprojectC(documents,stopwords)*/
    val sp4 = WordCount.subprojectD(documents,stopwords)


  }
}
