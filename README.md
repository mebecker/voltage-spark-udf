# voltage-spark-udf

Note that all of the shell scripts assume that you are using Azure CLI authentication for the Databricks CLI. If you need to use PAT or Token authentication, refer to the Databricks CLI documentation. 

The principals-permissions shell script creates account-level objects so you need to make sure that your .databrickscfg contains an "account profile." You can accomplish that by adding the following to your .databrickscfg file:

```bash
[ACCOUNT]
host = https://accounts.azuredatabricks.net/
account_id = xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
```
You can get your Databricks account id from the Account console (https://accounts.azuredatabricks.net/) by clicking on your user name in the top right.

![Account Id](docs/account-id.png)
