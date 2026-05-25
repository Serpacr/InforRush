pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        SONAR_HOST_URL = 'http://host.docker.internal:9000'
        SONAR_TOKEN    = credentials('sonar-token')
    }

    stages {

        stage('Checkout') {
            steps {
                echo '📥 Clonando repositório...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo '🔨 Compilando backend (Maven)...'
                dir('inforush/backend') {
                    sh 'mvn clean install -U -DskipTests -B'
                }
            }
        }

        stage('Test') {
            steps {
                echo '🧪 Executando testes...'
                dir('inforush/backend') {
                    sh 'mvn test -B'
                }
            }
            post {
                always {
                    junit allowEmptyResults: true,
                          testResults: 'inforush/backend/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube') {
            steps {
                echo '🔍 Analisando qualidade com SonarQube...'
                dir('inforush/backend') {
                    sh """
                        mvn sonar:sonar \
                            -Dsonar.projectKey=inforush \
                            -Dsonar.projectName=InfoRush \
                            -Dsonar.host.url=${SONAR_HOST_URL} \
                            -Dsonar.token=${SONAR_TOKEN} \
                            -B
                    """
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo '🐳 Construindo imagens Docker...'
                dir('inforush') {
                    sh 'docker compose build'
                }
            }
        }

        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                echo '🚀 Subindo containers...'
                dir('inforush') {
                    sh 'docker compose up -d'
                }
            }
        }
    }

    post {
        success { echo '✅ Pipeline concluída com sucesso!' }
        failure { echo '❌ Pipeline falhou. Verifique os logs acima.' }
    }
}