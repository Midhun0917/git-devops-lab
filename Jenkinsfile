
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo 'Source code checked out from GitHub'
                sh 'git log -1 --oneline'
            }
        }

        stage('Maven Build') {
            steps {
                echo 'Building Payment application...'
                sh 'mvn clean package'
            }
        }

        stage('Verify JAR') {
            steps {
                sh 'ls -lh target/*.jar'
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image...'
                sh 'docker build -t payment-app:1.0 .'
            }
        }
    }
}
