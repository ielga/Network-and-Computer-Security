rm *.pem
# 1. Generate CA's private key and self-signed certificate
openssl req -x509 -newkey rsa:4096 -days 365 -nodes -keyout CAPrivateKey.pem -out CACert.pem -subj "/C=PT/ST=Lisbon/L=Lisbon/O=IST/OU=SIRS/CN=sirs.remoteDocs.CA/emailAddress=CA.remoteDocs.group47@gmail.com"

# 2. Generate server's private key and certificate signing request (CSR)
openssl req -newkey rsa:4096 -nodes -keyout ServerKey.pem -out ServerReq.pem -subj "/C=PT/ST=Lisbon/L=Lisbon/O=IST/OU=SIRS/CN=sirs.remoteDocs/emailAddress=CA-remoteDocs.group47@gmail.com"

# 3. Use CA's private key to sign web server's CSR and get back the signed certificate
openssl x509 -req -in ServerReq.pem -days 60 -CA CACert.pem -CAkey CAPrivateKey.pem -CAcreateserial -out ServerCert.pem -extfile server-ext.cnf

# 4. Generate Client's private key and certificate signing request (CSR)
openssl req -newkey rsa:4096 -nodes -keyout UserKey.pem -out UserReq.pem -subj "/C=PT/ST=Lisbon/L=Lisbon/O=IST/OU=SIRS/CN=sirs.remoteDocs/emailAddress=CA-remoteDocs.group47@gmail.com"

# 5. Use CA's private key to sign Client's CSR and get back the signed certificate
openssl x509 -req -in UserReq.pem -days 60 -CA CACert.pem -CAkey CAPrivateKey.pem -CAcreateserial -out UserCert.pem -extfile server-ext.cnf

############## DATABASE #############
# 2. Generate database backup server's private key and certificate signing request (CSR)
openssl req -newkey rsa:4096 -nodes -keyout DatabaseBackupServerKey.pem -out DatabaseBackupServerReq.pem -subj "/C=PT/ST=Lisbon/L=Lisbon/O=IST/OU=SIRS/CN=sirs.remoteDocs/emailAddress=CA-remoteDocs.group47@gmail.com"

# 3. Use CA's private key to sign web server's CSR and get back the signed certificate
openssl x509 -req -in DatabaseBackupServerReq.pem -days 60 -CA CACert.pem -CAkey CAPrivateKey.pem -CAcreateserial -out DatabaseBackupServerCert.pem -extfile server-ext.cnf
######################################

echo "CA's self-signed certificate"
openssl x509 -in CACert.pem -noout -text

echo "Server's signed certificate"
openssl x509 -in ServerCert.pem -noout -text

echo "Client's signed certificate"
openssl x509 -in UserCert.pem -noout -text