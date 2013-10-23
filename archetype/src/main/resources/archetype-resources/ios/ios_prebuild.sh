cd ..

# -pl common/gwt ensures that only the GWT project is build (and not the Android project), 
# and `-am` ensures that all dependencies are compiled first, i.e. the app core project.

mvn -pl common/gwt -DgwtModule=${package}.${rootArtifactId}_safari_${CONFIGURATION} -am clean install