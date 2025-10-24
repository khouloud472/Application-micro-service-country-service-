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
        mvn deploy -Dnexus.username=admin -Dnexus.password=admin
        '''
    }
}

stage('Deploy to Tomcat') {
    steps {
        sh '''
        # Définir le chemin vers Tomcat
        TOMCAT_HOME=/opt/tomcat
        
        # Copier le .war
        cp target/Reservationavion-0.0.1-SNAPSHOT.war $TOMCAT_HOME/webapps/
        
        # Redémarrer Tomcat pour prendre en compte le nouveau .war
        $TOMCAT_HOME/bin/shutdown.sh || true
        $TOMCAT_HOME/bin/startup.sh
        '''
    }
}

    }
}
