pipeline {
    agent any

    environment {
        IMAGE_NAME = 'saucedemo-tests'
        // Сохраняем отчеты как артефакты
        ALLURE_RESULTS = 'target/allure-results'
    }

    stages {
        stage('Checkout') {
            steps {
                // Код уже склонирован Jenkins
                echo 'Код успешно получен из GitHub'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Собираем образ
                    bat "docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} ."
                    bat "docker tag ${IMAGE_NAME}:${BUILD_NUMBER} ${IMAGE_NAME}:latest"
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    // Запускаем тесты в headless-режиме
                    bat """
                        docker run --rm \
                        -v ${WORKSPACE}\\target\\allure-results:/tests/target/allure-results \
                        -e MAVEN_OPTS="-Dheadless=true" \
                        ${IMAGE_NAME}:${BUILD_NUMBER}
                    """
                }
            }
        }

        stage('Publish Allure Report') {
            steps {
                // Публикуем Allure-отчет
                allure includeProperties: false,
                       results: [[path: 'target/allure-results']]
            }
        }
    }

    post {
        always {
            // Очищаем старые отчеты
            cleanWs()
        }
        success {
            echo '✅ Все тесты успешно пройдены!'
        }
        failure {
            echo '❌ Некоторые тесты упали. Смотри Allure-отчет.'
        }
    }
}