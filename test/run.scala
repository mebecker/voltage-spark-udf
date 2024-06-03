var lowerCaseCryptoConfig = lit("LowerCaseAlphaNumeric|accounts22@dataprotection.voltage.com|voltage123|https://voltage-pp-0000.dataprotection.voltage.com/policy/clientPolicy.xml")
val clearTextDf = spark.read.format("csv").option("header","true").load("/home/mike/code/voltage-spark-udf/data/test.csv")

import com.contoso.voltageUDFsObject._

clearTextDf.withColumn("firstname", encrypt(clearTextDf("firstname"), lowerCaseCryptoConfig)).withColumn("lastname", encrypt(clearTextDf("lastname"), lowerCaseCryptoConfig)).show

import com.contoso.voltageUDFsClass

lowerCaseCryptoConfig = lit("LowerCaseAlphaNumeric|accounts22@dataprotection.voltage.com|voltage123")
val voltage = new voltageUDFsClass("/home/mike/code/voltage-spark-udf/lib/libvibesimplejava.so", "https://voltage-pp-0000.dataprotection.voltage.com/policy/clientPolicy.xml")
clearTextDf.withColumn("firstname", voltage.encrypt(clearTextDf("firstname"), lowerCaseCryptoConfig)).withColumn("lastname",voltage. encrypt(clearTextDf("lastname"), lowerCaseCryptoConfig)).show
