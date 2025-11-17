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
                    docker.build("registry:5000/saving-loan-repayments:${TAG}")
                }
            }
        }
        stage("Push Docker Image to Local Registry") {
            steps {
                script {
                    docker.withRegistry("http://registry:5000") {
                        docker.image("registry:5000/saving-loan-repayments:${TAG}").push()
                        docker.image("registry:5000/saving-loan-repayments:${TAG}").push("latest")
                    }
                }
            }
        }
        stage("Deliver for development") {
            when {
                 expression {
                           return env.BRANCH_NAME ==~ /(feature|develop)\/.*/
                       }
            }
            steps {
                sshagent(['enat-remedy-development']) {
                    sh 'ssh -o StrictHostKeyChecking=no -l  ${TEST_SERVER_USERNAME} ${TEST_SERVER_ADDRESS} "docker stop saving-loan-repayments| true; docker rm saving-loan-repayments | true; docker run -p 8061:8080   -e "SPRING_PROFILES_ACTIVE=develop" -d --name saving-loan-repayments ${DOCKER_PRIVATE_REGISTRY}/share-holder:${TAG}"'
                }
            }
        }
        stage("Deploy for production") {
            when {
                branch "main"
            }
            steps {
                sshagent(['enat-remedy-production']) {
                    sh 'ssh -o StrictHostKeyChecking=no -l  ${PRODUCTION_SERVER_USERNAME} ${PRODUCTION_SERVER_ADDRESS} "docker stop share-holder | true;     docker rm saving-loan-repayments | true;     docker run -p 8061:8080  -e "SPRING_PROFILES_ACTIVE=live" -d --name saving-loan-repayments ${DOCKER_PRIVATE_REGISTRY}/share-holder:${TAG}"'
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
