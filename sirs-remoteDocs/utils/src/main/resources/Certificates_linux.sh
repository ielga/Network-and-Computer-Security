# FOR CA
# generate the key pair
openssl genrsa -out CA_pair.key 2048
# Save the public key:
openssl rsa -in CA_pair.key -pubout > CA_public.key
#Create a Certificate Signing Request, using same key:
openssl req -new -key CA_pair.key -out CA.csr -subj '/C=PT/ST=Lisbon/L=Lisbon/O=IST/OU=SIRS/CN=sirs.remoteDocs.CA/emailAddress=CA.remoteDocs.group47@gmail.com'
# Self-sign:
openssl x509 -req -days 365 -in CA.csr -signkey CA_pair.key -out CA.crt
# For our certificate to be able to sign other certificates, OpenSSL requires that a database exists (a .srl file). Create it:
echo 01 > CA.srl

# So java can read:
#Convert server.key to .pem
openssl rsa -in CA_pair.key -text > CA_pair.pem
#Convert private Key to PKCS#8 format (so Java can read it)
openssl pkcs8 -topk8 -inform PEM -outform DER -in CA_pair.pem -out CA_pair.der -nocrypt
#Output public key portion in DER format (so Java can read it)
openssl rsa -in CA_pair.pem -pubout -outform DER -out CA_public.der


# FOR SERVER
# generate the key pair
openssl genrsa -out server_pair.key 2048 #size of rsa
# Save the public key:
openssl rsa -in server_pair.key -pubout > server_public.key
#Create a Certificate Signing Request, using same key:
openssl req -new -key server_pair.key -out server.csr -subj "/C=PT/ST=Lisbon/L=Lisbon/O=IST/OU=SIRS/CN=sirs.remoteDocs.CA/emailAddress=CA-remoteDocs.group47@gmail.com"
# CA signs the server
openssl x509 -req -days 365 -in server.csr -CA CA.crt -CAkey CA_pair.key -CAcreateserial -out server.crt

# So java can read:
#Convert server.key to .pem
openssl rsa -in server_pair.key -text > server_pair.pem
#Convert private Key to PKCS#8 format (so Java can read it)
openssl pkcs8 -topk8 -inform PEM -outform DER -in server_pair.pem -out server_pair.der -nocrypt
#Output public key portion in DER format (so Java can read it)
openssl rsa -in server_pair.pem -pubout -outform DER -out server_public.der



# to verify:
# openssl req -text -in server.csr -noout -verify

# FOR USER
# generate the key pair
openssl genrsa -out user_pair.key 2048 #size of rsa
# Save the public key:
openssl rsa -in user_pair.key -pubout > user_public.key
#Create a Certificate Signing Request, using same key:
openssl req -new -key user_pair.key -out user.csr -subj "/C=PT/ST=Lisbon/L=Lisbon/O=IST/OU=SIRS/CN=sirs.remoteDocs.CA/emailAddress=CA-remoteDocs.group47@gmail.com"
# Server signs the User
openssl x509 -req -days 365 -in user.csr -CA CA.crt -CAkey CA_pair.key -CAcreateserial -out user.crt


# So java can read:
#Convert server.key to .pem
openssl rsa -in user_pair.key -text > user_pair.pem
#Convert private Key to PKCS#8 format (so Java can read it)
openssl pkcs8 -topk8 -inform PEM -outform DER -in user_pair.pem -out user_pair.der -nocrypt
#Output public key portion in DER format (so Java can read it)
openssl rsa -in user_pair.pem -pubout -outform DER -out user_public.der

## conversion of .crt to .pem
openssl x509 -in CA.crt -out CA-cert.pem
openssl x509 -in server.crt -out server-cert.pem
openssl x509 -in user.crt -out user-cert.pem
