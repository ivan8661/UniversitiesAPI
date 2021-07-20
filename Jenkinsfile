
properties([disableConcurrentBuilds()])

pipeline {

            agent {
                label 'master'
            }

            options {
                buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '10'))
                timestamps()
            }
            
            node {
              stage ('Build') {
                withMaven {
                  sh "mvn clean install"
                } // withMaven will discover the generated Maven artifacts, JUnit Surefire & FailSafe reports and FindBugs reports
              }
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
