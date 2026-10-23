
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
        echo 'Scanning Docker image for HIGH and CRITICAL vulnerabilities...'

        sh '''
            mkdir -p "$WORKSPACE/trivy-tmp"

            TMPDIR="$WORKSPACE/trivy-tmp" trivy image \
              --config /dev/null \
              --scanners vuln \
              --severity HIGH,CRITICAL \
              --exit-code 1 \
              --format table \
              --output trivy-report.txt \
              payment-app:1.0
        '''
    }
    post {
        always {
            archiveArtifacts artifacts: 'trivy-report.txt',
                             allowEmptyArchive: true
        }
    }
}



stage('Docker Run') {
    steps {
        echo 'Starting Payment application container...'

        sh '''
            set -eu

            docker rm -f payment-container || true

            docker run -d \
              --name payment-container \
              -p 127.0.0.1:8081:8080 \
              payment-app:1.0

            echo "Waiting for application health check..."

            for i in $(seq 1 15); do
                if curl --fail --silent http://127.0.0.1:8081/health; then
                    echo ""
                    echo "Application health check passed!"
                    exit 0
                fi

                sleep 2
            done

            echo "Application health check failed!"
            docker logs payment-container
            exit 1
        '''
    }
}
        stage('Docker Hub Push') {
            steps {
                echo 'Publishing Payment application image to Docker Hub...'

                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_TOKEN'
                    )
                ]) {
                    sh '''
                        set -eu

                        echo "$DOCKER_TOKEN" | docker login \
                          --username "$DOCKER_USER" \
                          --password-stdin

                        docker tag payment-app:1.0 "$DOCKER_USER/payment-app:${BUILD_NUMBER}"

                        docker push "$DOCKER_USER/payment-app:${BUILD_NUMBER}"

                        docker logout
                    '''
                }
            }
        }
        
stage('Deploy to Kubernetes') {
    steps {
        echo 'Deploying Payment application to Kubernetes...'

        withCredentials([
            string(
                credentialsId: 'k8s-deployer-token',
                variable: 'K8S_TOKEN'
            )
        ]) {
            sh '''
                set -eu
                set +x

                IMAGE="midhun0917/payment-app:${BUILD_NUMBER}"

                kubectl \
                  --server=https://172.31.38.134:6443 \
                  --token="$K8S_TOKEN" \
                  --insecure-skip-tls-verify=true \
                  -n payment-app \
                  set image deployment/payment-app \
                  payment-app="$IMAGE"

                kubectl \
                  --server=https://172.31.38.134:6443 \
                  --token="$K8S_TOKEN" \
                  --insecure-skip-tls-verify=true \
                  -n payment-app \
                  rollout status deployment/payment-app \
                  --timeout=180s

                echo "Kubernetes deployment successful!"
            '''
        }
    }
}

    }
}
