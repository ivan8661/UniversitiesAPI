
properties([disableConcurrentBuilds()])

pipeline {

            agent {
                label 'master'
            }

            options {
                buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '10'))
                timestamps()
            }

            stages {

            stage('Build') { 
                    steps {
                            sh 'mvn install' 
                          }
                        }
                        
                stage('create docker image') {
                    steps {
                        echo " ========= start building image ========"
                        bat 'docker build --tag core . '     
                           }
                }
                
                
            }
    }
