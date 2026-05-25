pipeline {
    agent any
    environment {
        SONAR_HOST_URL = 'http://sonarqube:9000'
        SONAR_TOKEN    = credentials('sonar-token')
    }
    stages {
        stage('Checkout') { steps { checkout scm } }
        stage('Build') { steps { dir('backend') { sh 'mvn clean package -DskipTests -B' } } }
        stage('Test')  { steps { dir('backend') { sh 'mvn test -B' } } }
        stage('SonarQube') {
            steps {
                dir('backend') {
                    sh "mvn sonar:sonar -Dsonar.projectKey=inforush -Dsonar.host.url=${SONAR_HOST_URL} -Dsonar.token=${SONAR_TOKEN} -B"
                }
            }
        }
        stage('Docker Build') { steps { sh 'docker compose build' } }
        stage('Deploy') { when { branch 'main' }; steps { sh 'docker compose up -d' } }
    }
    post {
        success { echo '✅ Pipeline OK!' }
        failure { echo '❌ Pipeline falhou.' }
    }
}
