package com.contoso

import com.voltage.securedata.enterprise.{LibraryContext, FPE, VeException}
import org.apache.spark.sql.functions.udf
import scala.collection.mutable

object voltageUDFsObject {
    System.load("/Volumes/adb_cus_01/default/voltage/libvibesimplejava.so")

    val fpeMap = mutable.Map[String, FPE]()
    
    val decrypt = udf((cipherText: String, config: String) => cryptoImplementation(cipherText, true, config))
    val encrypt = udf((plainText: String, config: String) => cryptoImplementation(plainText, false, config))   

    def cryptoImplementation(text: String, decrypt: Boolean, config: String) = {
        try {
            if(decrypt) getFPE(config).access(text) else getFPE(config).protect(text)
        }
        catch {
            case e : VeException => { 
                //this is a very hacky way to easily get failure details on a cell by cell basis - not production ready
                f"!!EXCEPTION!!: ${e.getMessage}" 
             }
        }
    }    

    def getFPE(config: String) : FPE = {
        if(!fpeMap.contains(config)) {
            println("Create FPE with config: " + config)
            val configArray = config.split('|')

            val libraryContext = new LibraryContext.Builder()
                .setPolicyURL(configArray(3))
                .setTrustStorePath("/etc/ssl/certs")
                .build()            

            fpeMap(config) = libraryContext
                                .getFPEBuilder(configArray(0))
                                .setIdentity(configArray(1))
                                .setSharedSecret(configArray(2))
                                .build()
        } 
        fpeMap(config)       
    }            
}