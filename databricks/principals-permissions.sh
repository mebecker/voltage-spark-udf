#! /bin/bash
users=("mike@mngenvmcap126710.onmicrosoft.com" "admin@mngenvmcap126710.onmicrosoft.com")
groupName="voltage-users"
volumeFullName="adb_cus_01.default.voltage"

groupId=$(databricks account groups list --output json | jq -r '.[] | select(.displayName == '\"$groupName\"').id')

if [ -z $groupId ]; then
    echo "Group not found. Creating group..."
    groupId=$(databricks account groups create --display-name $groupName | jq -r '.id')
fi

for i in "${users[@]}"
do
    userId=$(jq -r '.[] | select(.userName == '\"$i\"').id' <<< $(databricks account users list --output json))
    
    #needs to be "compact" json format here or we get "Error: unexpected end of JSON input" ¯\_(ツ)_/¯
    json=$(jq -c '.Operations[0].value.members[0].value = '\"$userId\"'' json/group-members.json) 
    
    echo "Adding user $i ($userId) to group..."
    databricks account groups patch $groupId --json $json --profile ACCOUNT 
done

echo "Updating volume permissions..."
databricks grants update volume $volumeFullName --json @json/volume-permissions.json
