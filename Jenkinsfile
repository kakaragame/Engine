pipeline {
    agent any
    tools {
        jdk 'Java 17'
    }
    stages {
        stage ('Build') {
            steps {
              sh 'sh build.sh'
              sh './gradlew javadoc'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'archives/*.jar, files.txt, core/build/version.properties', followSymlinks: false
                    javadoc javadocDir: 'core/build/docs/javadoc', keepAll: true
                }
            }
        }
    }
}