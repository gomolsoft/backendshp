package de.moso

import com.mongodb.{Mongo, MongoClient}
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoConfiguration

@Configuration
class MongoConfig extends AbstractMongoConfiguration {

  def getDatabaseName:String = "shop"

  def mongo:Mongo = new MongoClient()

}
