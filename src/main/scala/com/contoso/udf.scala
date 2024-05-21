package com.contoso

import com.voltage.securedata.enterprise.{LibraryContext, FPE, VeException}
import org.apache.spark.sql.functions.udf
import scala.collection.mutable
import java.io.PrintWriter
import java.io.StringWriter
import scala.io.Source

object voltageUDFs {

    //obviously should not be hard coded in a real implementation
    System.load("/Volumes/adb_cus_01/foo/voltage/libvibesimplejava.so")

    val decrypt = udf((cipherText: String, config: String) => cryptoImplementation(cipherText, true, config))
    val encrypt = udf((plainText: String, config: String) => cryptoImplementation(plainText, false, config))    

    val fpeMap = mutable.Map[String, FPE]()

    //obviously paths should not be hard coded in real implementation
    lazy val libraryContext = {
        new LibraryContext.Builder()
            .setPolicyURL("https://voltage-pp-0000.dataprotection.voltage.com/policy/clientPolicy.xml")
            .setTrustStorePath("/etc/ssl/certs")
            .build()
    }

    def cryptoImplementation(text: String, decrypt: Boolean, config: String) = {
        try {
            if(decrypt) getFPE(config).access(text) else getFPE(config).protect(text)
        }
        catch {
            case e : VeException => { 
                //this is a very hacky way to easily get failure details on a cell by cell basis - not production ready
                // getStackTrace(e)
                e.getDetailedMessage()
             }
        }
    }

    def getFPE(config: String) : FPE = {
        if(!fpeMap.contains(config)) {
            println("Create FPE with config: " + config)
            val configArray = config.split('|')
            fpeMap(config) = libraryContext
                                .getFPEBuilder(configArray(0))
                                .setIdentity(configArray(1))
                                .setSharedSecret(configArray(2))
                                .build()
        } 
        fpeMap(config)
    }

    def getStackTrace(exception: Exception) : String = {
        val sw = new StringWriter
        exception.printStackTrace(new PrintWriter(sw)) 
        sw.toString()
    }
}