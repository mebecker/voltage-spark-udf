#! /bin/bash
databricks artifact-allowlists update LIBRARY_JAR --json @./jsonwhitelist-jars.json
databricks artifact-allowlists update LIBRARY_MAVEN --json @./jsonwhitelist-maven.json
