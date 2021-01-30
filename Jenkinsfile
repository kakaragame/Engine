pipeline {
    agent any
    tools {
        jdk 'Java 11'
    }
    stages {
        stage ('Build') {
            steps {
sh '''mkdir archives
gradle clean shadowJar -PbuildNumber=${BUILD_NUMBER} -Pnative=natives-linux -Pis-build=true
cp build/libs/*.jar archives/

gradle clean shadowJar -PbuildNumber=${BUILD_NUMBER} -Pnative=natives-windows -Pis-build=true
cp build/libs/*.jar archives/

ls archives/ > files.txt'''
            }
            post {
                success {
                    archiveArtifacts artifacts: 'archives/*.jar, files.txt, build/version.properties', followSymlinks: false
                    javadoc javadocDir: 'target/site/apidocs', keepAll: true
                }
            }
        }
}