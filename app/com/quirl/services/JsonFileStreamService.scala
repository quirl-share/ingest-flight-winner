package com.quirl.services

import java.nio.file.{Path, Paths}

import akka.stream.Materializer
import akka.stream.scaladsl.{FileIO, JsonFraming, Sink, Source}
import akka.util.ByteString
import play.api.Logging
import play.api.libs.json.JsObject
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}

class JsonFileStreamService ()(implicit cc: ExecutionContext, mat: Materializer) extends Logging {

  val input = "D:\\projects\\data\\2019-11-01-0000Z.json"

  val file: Path = Paths.get(input)
  val numEntries: Future[Int] =
    FileIO.fromPath(file)
    .via(JsonFraming.objectScanner(Int.MaxValue))
    .mapAsync[JsObject](100)((entry: ByteString) => Future {
        logger.info("Got em!")
        Json.parse(entry.toArray).as[JsObject]
    })
    .runFold(0)( (acc, _) => acc + 1)

  val test: Future[Seq[String]] =
    FileIO.fromPath(file)
      .via(JsonFraming.objectScanner(Int.MaxValue)).runFold(Seq.empty[String]) {
      case (acc, entry) => acc ++ Seq(entry.utf8String)
    }



}
