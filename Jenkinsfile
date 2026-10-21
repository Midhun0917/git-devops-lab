
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

        stage('SonarQube Analysis') {
            steps {
                echo 'Analyzing source code with SonarQube Cloud...'

                withCredentials([
                    string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')
                ]) {
                    sh '''
                        mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
                          -Dsonar.projectKey=Midhun0917_git-devops-lab \
                          -Dsonar.organization=midhun0917 \
                          -Dsonar.host.url=https://sonarcloud.io \
                          -Dsonar.token="$SONAR_TOKEN"
                    '''
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image...'
                sh 'docker build -t payment-app:1.0 .'
            }
        }


stage('Trivy Security Scan') {
    steps {
        echo 'Scanning Docker image for vulnerabilities...'

        sh '''
            mkdir -p "$WORKSPACE/trivy-tmp"

            TMPDIR="$WORKSPACE/trivy-tmp" trivy image \
              --config /dev/null \
              --scanners vuln \
              --severity HIGH,CRITICAL \
              --exit-code 0 \
              --format table \
              --output trivy-report.txt \
              payment-app:1.0
        '''

        archiveArtifacts artifacts: 'trivy-report.txt',
                         fingerprint: true
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
