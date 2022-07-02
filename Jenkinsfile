pipeline {
    agent any
    tools {
        jdk 'Java 17'
    }
    stages {
        stage ('Build') {
            steps {
              sh 'sh build.sh'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'archives/*.jar, files.txt, build/version.properties', followSymlinks: false
                    javadoc javadocDir: 'build/docs/javadoc', keepAll: true
                }
            }
        }
    }
}