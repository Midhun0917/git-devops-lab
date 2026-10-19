
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo 'Starting GitHub checkout...'
                checkout scm
                sh 'git log -1 --oneline'
                echo 'GitHub checkout completed successfully!'
            }
        }

        stage('Verify Tools') {
            steps {
                sh '''
                    echo "Checking installed tools..."
                    git --version
                    java -version
                    mvn --version
                    docker --version
                '''
            }
        }
    }
}
