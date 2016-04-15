#!/bin/sh
CP=.:config
for I in ./lib/*.jar
do
CP=$CP:$I
done
echo $CP
java -Xms2048m -Xmx2048m  -classpath $CP com.icloudmoo.business.file.upload.Transfer
