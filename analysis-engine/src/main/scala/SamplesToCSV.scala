import scala.util.Properties

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.DataFrame
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row

import org.apache.spark.ml.feature.QuantileDiscretizer
import org.apache.spark.mllib.fpm.FPGrowth
import org.apache.spark.mllib.fpm.AssociationRules.Rule

import java.io._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.JsonDSL._

import mllibTest.utils.TimeUtils
import mllibTest.models.samples._


object SamplesToCSV {

  def initSpark(): SparkSession = {
		val sparkMaster = Properties.envOrNone("SPARK_MASTER").get

		SparkSession.builder()
			.appName("MLlib Test")
			.master(sparkMaster)
			.getOrCreate()
	}


  def readCaratRates(sampleDir: String)(implicit sc: SparkContext): RDD[fi.helsinki.cs.nodes.carat.sample.Rate] = {
    sc.objectFile[fi.helsinki.cs.nodes.carat.sample.Rate](s"${sampleDir}")
  }

  //quick and dirty should not be run on a low mem node :)
  def writeCSV(samples: RDD[Sample], outFile: String): Unit = {
    import java.io._

    val writer = new BufferedWriter(new FileWriter(new File(outFile)))
    //header
    writer.write(
      Seq(
        "rate",
        "cpu",
        "distance",
        "temp",
        "voltage",
        "screen",
        "mobileNetwork",
        "network",
        "wifiStrength",
        "wifiSpeed"
      ).mkString("\t") + "\n"
    )

    samples.collect().foreach { s =>
      writer.write(
        Seq(
          s.rate,
          s.cpu,
          s.distance,
          s.temp,
          s.voltage,
          s.screen,
          s.mobileNetwork,
          s.network,
          s.wifiStrength,
          s.wifiSpeed
        ).map(_.toString).mkString("\t") + "\n"
      )
    }

    writer.close()
  }

  def main(args: Array[String]): Unit = {
    implicit val spark = initSpark()
    implicit val sc = spark.sparkContext
    implicit val sqlContext = new org.apache.spark.sql.SQLContext(sc)

    //val ratePath = "/dev/shm/tmp/spark/jirihamb/single-samples-2016-06-22-until-2016-08-22-cc-mcc-obj-rates/"

    if (args.length < 3) {
      throw new Exception("Invalid number of arguments.")
    }

    val inputFile = args(1)
    val outputFile = args(2)


    val samples = readCaratRates(inputFile).collect {
      case rate /* if rate.allApps().contains(applicationName)*/ => Sample.fromCaratRate(rate)
    }

    writeCSV(samples, outputFile)
  }


}
