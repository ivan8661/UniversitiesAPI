
properties([disableConcurrentBuilds()])

pipeline {

        agent {
            label 'dev'
        }

        options {
            buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '10'))
            timestamps()
        }

        stages {


        stage('Build') {
            sh 'mvn clean install'
            def pom = readMavenPom file:'pom.xml'
            print pom.version
            env.version = pom.version
        }

        stage('create docker image') {
            steps {
                echo " ========= start building image ========"
                dir("dockerImages") {
                    sh 'docker build --tag core . '
                }
            }
        }
        }
    }

}
