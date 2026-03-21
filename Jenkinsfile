pipeline {
    agent { label 'docker' }  // ← меняем с 'windows' на 'docker'

    environment {
        IMAGE_NAME = 'saucedemo-tests'
    }

    stages {
        stage('Build Docker Image') {
            steps {
                script {
                    // Собираем образ (Linux shell)
                    sh "docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} ."
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    // Запускаем тесты с headless (Linux пути)
                    sh """
                        docker run --rm \
                        -v ${WORKSPACE}/target/allure-results:/tests/target/allure-results \
                        -e MAVEN_OPTS="-Dheadless=true" \
                        ${IMAGE_NAME}:${BUILD_NUMBER}
                    """
                }
            }
        }

        stage('Publish Allure Report') {
            steps {
                // Публикуем отчет (путь остается без изменений)
                allure includeProperties: false,
                       results: [[path: 'target/allure-results']]
            }
        }
    }

    post {
        always {
            // Очищаем после сборки
            cleanWs()
        }
        success {
            echo '✅ Тесты успешно пройдены!'
        }
        failure {
            echo '❌ Тесты упали. Смотри отчет Allure.'
        }
    }
}