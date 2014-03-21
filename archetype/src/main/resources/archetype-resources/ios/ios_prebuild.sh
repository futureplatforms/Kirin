#!/bin/bash
#

cd ..

# -pl common/gwt ensures that only the GWT project is build (and not the Android project), 
# and `-am` ensures that all dependencies are compiled first, i.e. the app core project.

${M2}/mvn clean install \
	-pl gwt \
	-Dconfiguration=${CONFIGURATION} \
	-DgwtModule=${package}.${rootArtifactId}_safari_${CONFIGURATION} \
	-Dgwt.compiler.strict=true \
	-am 