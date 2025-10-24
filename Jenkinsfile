pipeline {
    agent any
    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/khouloud472/Application-micro-service-country-service-.git',
                    branch: 'main',
                    credentialsId: 'github-token'
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean install -Dmaven.test.failure.ignore=true'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                    sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=country-service \
                        -Dsonar.host.url=http://localhost:9000 \
                        -Dsonar.login=${SONAR_TOKEN}
                    """
                }
            }
        }

       stage('Deploy to Nexus') {
    steps {
        sh '''
        mvn clean package
        mvn deploy:deploy-file \
          -DgroupId=com.example.reservationavion \
          -DartifactId=Reservationavion \
          -Dversion=0.0.1-SNAPSHOT \
          -Dpackaging=jar \
          -Dfile=target/Reservationavion-0.0.1-SNAPSHOT.jar \
          -DpomFile=pom.xml \
          -DrepositoryId=nexus-snapshots \
          -Durl=http://localhost:8081/repository/Application-micro-service-country-service-snapshot/ \
          -DretryFailedDeploymentCount=3
        '''
    }
}



    }
}
