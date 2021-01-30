#!/bin/bash
mkdir archives
gradle clean shadowJar -PbuildNumber=${BUILD_NUMBER} -Pnative=natives-linux -Pis-build=true
cp build/libs/*.jar archives/

gradle clean shadowJar javadoc -PbuildNumber=${BUILD_NUMBER} -Pnative=natives-windows -Pis-build=true
cp build/libs/*.jar archives/


ls archives/ > files.txt