pipeline {
    agent any
    tools {
        jdk 'Java 11'
    }
    stages {
        stage ('Build') {
            steps {
              sh 'sh build.sh'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'archives/*.jar, files.txt, build/version.properties', followSymlinks: false
                    javadoc javadocDir: 'target/site/apidocs', keepAll: true
                }
            }
        }
}