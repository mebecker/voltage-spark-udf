#! /bin/bash
users=("mike@mngenvmcap126710.onmicrosoft.com" "admin@mngenvmcap126710.onmicrosoft.com")
groupName="voltage-users"
volumeFullName="adb_cus_01.default.voltage"
sleepInterval=5s

groupId=$(databricks account groups list --output json | jq -r '.[] | select(.displayName == '\"$groupName\"').id')

if [ -z $groupId ]; then
    echo "Group not found. Creating group..."
    groupId=$(databricks account groups create --display-name $groupName | jq -r '.id')
fi

echo "Sleep for $sleepInterval to allow group creation..."
sleep $sleepInterval

for i in "${users[@]}"
do
    userId=$(jq -r '.[] | select(.userName == '\"$i\"').id' <<< $(databricks account users list --output json))
    
    #needs to be "compact" json format here or we get "Error: unexpected end of JSON input" ¯\_(ツ)_/¯
    userAddJson=$(jq -c '.Operations[0].value.members[0].value = '\"$userId\"'' json/group-members.json) 
    
    echo "Adding user $i ($userId) to group..."
    databricks account groups patch $groupId --json $userAddJson --profile ACCOUNT 
done

echo "Updating volume permissions..."

permissionGrantJson=$(jq -c '.changes[0].principal = '\"$groupName\"'' json/volume-permissions.json) 
databricks grants update volume $volumeFullName --json $permissionGrantJson
