
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
