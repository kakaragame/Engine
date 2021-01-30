pipeline {
    agent any
    tools {
        jdk 'Java 11'
    }
    stages {
        stage ('Build') {
            steps {
              checkout([$class: 'GitSCM', branches: [[name: '**']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '6afd91c6-972a-4903-9ba9-c1871c0deda1', url: 'https://github.com/kakaragame/Engine.git']]])
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