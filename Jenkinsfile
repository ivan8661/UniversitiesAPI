
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
                                        bat 'mvn -v'
                                        bat 'mvn dependency:tree'
                                        bat 'mvn clean verify'
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
