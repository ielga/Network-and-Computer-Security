#!/bin/bash
# mysqldump -u root -psirsRDpasswd47dif remoteDocsDB > "Documents/testingBackup5.sql"

backupFolder=server/src/main/DataBaseBackups
user=root 
database=remoteDocsDB
password=sirsRDpasswd47dif

sqlfile=$backupFolder/remoteDocs-backup-$(date +%d-%m-%Y_%H-%M-%S).sql
# zipfile=$backupfolder/remoteDocsDB-BackUp-$(date +%d-%m-%Y_%H-%M-%S).zip 

# Create a backup 
sudo mysqldump -u $user -p$password $database > $sqlfile 

