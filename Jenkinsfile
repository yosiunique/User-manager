pipeline {
    options {
        buildDiscarder(logRotator(numToKeepStr: '2'))
    }
    environment {
        DATE =  new Date().format('yy.M')
        TAG = "${DATE}.${BUILD_NUMBER}"
        PRODUCTION_SERVER_ADDRESS = "${PRODUCTION_SERVER_ADDRESS}"
        PRODUCTION_SERVER_USERNAME = "${PRODUCTION_SERVER_USERNAME}"
        TEST_SERVER_ADDRESS = "${TEST_SERVER_ADDRESS}"
        TEST_SERVER_USERNAME = "${TEST_SERVER_USERNAME}"
        DOCKER_PRIVATE_REGISTRY = "${DOCKER_PRIVATE_REGISTRY}"
    }
    tools {
        maven 'maven'
    }
    agent any
    stages {
        stage("Build") {
            steps {
                sh "mvn -version"
                sh "mvn clean install -DskipTests"
            }
        }
        stage("Build Docker") {
            steps {
                script {
                    docker.build("registry:5000/loan-repayment:${TAG}")
                }
            }
        }
        stage("Push Docker Image to Local Registry") {
            steps {
                script {
                    docker.withRegistry("http://registry:5000") {
                        docker.image("registry:5000/loan-repayment:${TAG}").push()
                        docker.image("registry:5000/loan-repayment:${TAG}").push("latest")
                    }
                }
            }
        }
        stage("Deliver for development") {
            when {
                 expression {
                           return env.BRANCH_NAME ==~ /(feature)\/.*/
                       }

                       branch "develop"
            }
            steps {
                sshagent(['enat-remedy-development']) {
                sh 'ssh -o StrictHostKeyChecking=no -l  ${TEST_SERVER_USERNAME} ${TEST_SERVER_ADDRESS} \
                 "docker stop aloan-repayment || true; \
                 docker rm aloan-repayment || true; \
                 docker run -p 8061:8080 \
                   -v /mnt/loan:/var/storage \
                   -e SPRING_PROFILES_ACTIVE=develop \
                   -d --name aloan-repayment \
                   ${DOCKER_PRIVATE_REGISTRY}/loan-repayment:${TAG}"'
                }
            }
        }
        stage("Deploy for production") {
            when {
                branch "main"
            }
            steps {
                sshagent(['enat-remedy-production']) {
                    sh 'ssh -o StrictHostKeyChecking=no -l  ${PRODUCTION_SERVER_USERNAME} ${PRODUCTION_SERVER_ADDRESS} "docker stop loan-repayment | true;     docker rm loan-repayment | true;     docker run -p 8060:8080 -v /mnt/loan:/var/storage -e "SPRING_PROFILES_ACTIVE=live" -d --name loan-repayment ${DOCKER_PRIVATE_REGISTRY}/loan-repayment:${TAG}"'
                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}
