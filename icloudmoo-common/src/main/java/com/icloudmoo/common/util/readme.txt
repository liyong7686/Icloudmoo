RSA密钥对生成方式：
生成密钥：
openssl genrsa -out private_key.pem 1024

根据密钥生成公钥：
openssl rsa -in private_key.pem -out public_key.pem -pubout

把密钥的格式转换成pkcs8：
openssl pkcs8 -topk8 -in private_key.pem -out pkcs8_private_key.pem -nocrypt