pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_TOKEN = credentials('sonar-token')
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                dir('backend') {
                    sh 'mvn clean install -U -DskipTests'
                }
            }
        }

        stage('Test') {
            steps {
                dir('backend') {
                    sh 'mvn test -B'
                }
            }
        }

        stage('SonarQube') {
            steps {
                dir('backend') {
                    sh """
                    mvn clean verify sonar:sonar \
                    -Dsonar.projectKey=inforush \
                    -Dsonar.host.url=${SONAR_HOST_URL} \
                    -Dsonar.login=${SONAR_TOKEN} \
                    -B
                    """
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker compose build'
            }
        }

        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                sh 'docker compose up -d'
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline OK!'
        }
        failure {
            echo '❌ Pipeline falhou.'
        }
    }
}