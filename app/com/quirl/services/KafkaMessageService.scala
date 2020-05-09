package com.quirl.services

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer.DrainingControl
import akka.kafka.CommitterSettings
import akka.kafka.ConsumerSettings
import akka.kafka.ProducerMessage
import akka.kafka.ProducerSettings
import akka.kafka.Subscriptions
import akka.kafka.scaladsl.Consumer
import akka.kafka.scaladsl.Producer
import akka.stream.Materializer
import akka.stream.scaladsl.Keep
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class KafkaMessageService(implicit system: ActorSystem) {
  implicit val cc: ExecutionContext = system.dispatcher

  val inTopic = "input"
  val outTopic = "output"

  val config = system.settings.config.getConfig("quirl.kafka")

  val consumerSettings: ConsumerSettings[String, String] = ConsumerSettings(config, new StringDeserializer, new StringDeserializer)
  val producerSettings: ProducerSettings[String, String] = ProducerSettings(system, new StringSerializer, new StringSerializer)
  val committerSettings: CommitterSettings = CommitterSettings(system)

  def control()(implicit mat: Materializer): DrainingControl[Done] =
    Consumer
      .committableSource(consumerSettings, Subscriptions.topics(inTopic))
      .map { msg =>
        ProducerMessage.single(
          new ProducerRecord(outTopic, msg.record.key, msg.record.value),
          msg.committableOffset
        )
      }
      .toMat(Producer.committableSink(producerSettings))(Keep.both)
      .mapMaterializedValue(DrainingControl.apply)
      .run()

  def business(str: String, str1: String): Future[Done] = Future.successful(Done)
}
