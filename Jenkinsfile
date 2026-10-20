
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
                echo 'Building and testing Payment application...'
                sh 'mvn clean package'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
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

        stage('Docker Run') {
            steps {
                echo 'Running Payment application inside Docker...'
                sh '''
                    docker rm -f payment-container || true
                    docker run --name payment-container payment-app:1.0
                '''
            }
        }
    }
}
