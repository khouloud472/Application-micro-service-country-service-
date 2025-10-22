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

        stage('Compile, Test, Package') {
    steps {
        sh 'mvn clean install -Dmaven.test.failure.ignore=true'
    }
    post {
        always {
            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
        }
    }
}


        withCredentials([string(credentialsId: 'sonar-token', variable: 'fd947837-9bc5-4708-8788-5586e0c1fd8c')]) {
    sh "${mvnHome}/bin/mvn sonar:sonar -Dsonar.projectKey=Application-micro-service-country-service -Dsonar.login=${fd947837-9bc5-4708-8788-5586e0c1fd8c}"
}

    }
}
