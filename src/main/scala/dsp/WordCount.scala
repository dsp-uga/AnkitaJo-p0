/**Implementation of Project 0
 *
 * Work done by Ankita Joshi
 *
 */

package dsp

import org.apache.spark.rdd.RDD
import org.apache.spark.broadcast.Broadcast

object WordCount {

	//subprojectA - Simple word count
	def subprojectA(A: RDD[(Int, String)]): Array[(Int, String)] ={

		val r1 = A.flatMap{ case(index,line) => line.split("\\s")}.map(word => (word.toLowerCase,1)).reduceByKey(_+_).filter{case(a,b) => b > 2}.map{case(a,b) => (b,a)}.sortByKey(ascending = false).top(40)
		r1
 
	}
	
	//subprojectB - WordCount with removal of stop words
	def subprojectB(A: RDD[(Int, String)], B: Broadcast[Array[String]]): Array[(Int, String)] ={

		//Removing stopwords 
		val stopwords = B.value.toArray
	
		val r2 = A.flatMap{ case(index,line) => line.split("\\s")}.map(word => (word.toLowerCase,1)).reduceByKey(_+_).filter{case(a,b) => b > 2 && !stopwords.contains(a)}.map{case(a,b) => (b,a)}.sortByKey(ascending = false).top(40)
 		r2
	}

	//subprojectC - Wordcount along with removal of stopwords and some basic preprocessing
	def subprojectC(A: RDD[(Int, String)], B: Broadcast[Array[String]]): Array[(Int, String)] ={

		//Removing stopwords along with special characters in the words
		val stopwords = B.value.toArray
		
		val r3 = A.flatMap{ case(index,line) => line.split("\\s")}.map{case(word) => word.replaceAll("[,.!?:;]", "").trim.toLowerCase}.map(word => (word,1)).reduceByKey(_+_).filter{case(a,b) => b > 2 && !stopwords.contains(a) && a.length > 1}.map{case(a,b) => (b,a)}.sortByKey(ascending = false).top(40)
		r3//.foreach(println(_))
	}

	//subprojectD - Implementation of tf-idf
	def subprojectD(A: RDD[(Int, String)], B: Broadcast[Array[String]]): Array[Seq[(String, Double)]]={

		val stopwords = B.value.toArray
		 
		val wordsInDocs = A.flatMap{ case(index,line) => 
			line.split("\\s").map{ case(word) => (index,word)}}.map{case(index,word) =>(index, word.replaceAll("[-_,.!?:;\"]", "").trim.toLowerCase)}.filter{ case(index,word) => !word.startsWith("'") || !word.endsWith("'")  && !stopwords.contains(word) }.map{case(index,word) => ((word,"d"+index.toString),1)}
		
		//Compute the term frequency: number of times a word appears in a document
		val tf = wordsInDocs.reduceByKey{case(accCount,count) => accCount + count }

		//Compute the number of documents the word appears in
		val nt = tf.map{ case((word,doc),count) => (word,doc)}.groupByKey().map{case(word,lists) => (word,lists.size)}

		//Compute the IDF
		val idf = nt.map{case(word,termindoc) => (word,Math.log(8/termindoc))}
 
		//Compute the TF-IDF score

		val tfidf = tf.map{case((word,doc),count) => (word,(doc,count))}.join(idf).map{case(word,((doc,tfscore),idfscore)) => (doc,(word,tfscore.toDouble*idfscore))}

		val r4 = tfidf.groupByKey().map{case(doc,list) => (doc,list.toArray.sortBy(-_._2).slice(0, 5))}.map{case (doc,stuff) => stuff.toSeq.map{case(a,b) => (a,b)}}.collect
		r4

			
	}
 	
}

