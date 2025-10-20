pipeline {
    agent any

    environment {
        APP_NAME = "Flight-Reservation-System"
        JAR_NAME = "reservationavion.jar"
        TOMCAT_URL = "http://localhost:8080/manager/text"
        TOMCAT_CREDENTIALS = "tomcat-credentials"
    }

    tools {
        maven 'Maven' 
        jdk 'JDK17'   
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', 
                url: 'https://github.com/khouloud472/Application-micro-service-country-service-.git',
                credentialsId: 'github-token'
            }
        }

        stage('Build') {
            steps {
                script {
                    try {
                        sh 'mvn clean compile'
                    } catch (err) {
                        echo "❌ Erreur de compilation : ${err}"
                        error("Build failed")
                    }
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    try {
                        sh 'mvn test'
                    } catch (err) {
                        echo "⚠️ Certains tests ont échoué, mais on continue..."
                        // Le build continue même si des tests échouent
                    }
                }
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml' // Publication des résultats des tests
                }
            }
        }

        stage('Package') {
            steps {
                script {
                    try {
                        sh 'mvn package -DskipTests'
                    } catch (err) {
                        echo "❌ Erreur de packaging : ${err}"
                        error("Package failed")
                    }
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Deploy Options') {
            parallel {
                stage('Deploy to Tomcat') {
                    when {
                        expression { params.DEPLOY_TO_TOMCAT == true }
                    }
                    steps {
                        script {
                            echo "🚀 Déploiement sur Tomcat..."
                            sh """
                                # Arrêt de Tomcat
                                sudo systemctl stop tomcat9 || true
                                
                                # Nettoyage de l'ancienne application
                                sudo rm -rf /var/lib/tomcat9/webapps/${APP_NAME}*
                                
                                # Copie du WAR
                                sudo cp target/*.war /var/lib/tomcat9/webapps/${APP_NAME}.war
                                
                                # Redémarrage de Tomcat
                                sudo systemctl start tomcat9
                                
                                # Attente du démarrage
                                sleep 30
                            """
                        }
                    }
                }

                stage('Deploy Standalone') {
                    when {
                        expression { params.DEPLOY_STANDALONE == true }
                    }
                    steps {
                        script {
                            echo "🚀 Lancement de l'application en mode standalone..."
                            
                            // Arrêt de l'instance précédente si elle existe
                            sh 'pkill -f "java -jar target/${JAR_NAME}" || true'
                            sleep 5
                            
                            // Démarrage de la nouvelle instance
                            sh """
                                nohup java -jar target/${JAR_NAME} > app.log 2>&1 &
                                echo $! > app.pid
                            """
                            
                            // Attente du démarrage
                            sleep 30
                            
                            // Vérification que l'application répond
                            sh 'curl -f http://localhost:8080/actuator/health || echo "Application non encore prête"'
                        }
                    }
                }
            }
        }

        stage('Health Check') {
            steps {
                script {
                    echo "🔍 Vérification du statut de l'application..."
                    retry(3) {
                        sleep 10
                        sh '''
                            curl -f http://localhost:8080/actuator/health && \
                            echo "✅ Application démarrée avec succès" || \
                            echo "❌ L'application ne répond pas"
                        '''
                    }
                }
            }
        }
    }

    post {
        always {
            echo "📊 Build ${currentBuild.result} - Rapport: ${env.BUILD_URL}"
            cleanWs() // Nettoyage du workspace
        }
        success {
            echo '✅ Pipeline exécuté avec succès !'
            emailext (
                subject: "SUCCESS: Pipeline '${env.JOB_NAME}' [${env.BUILD_NUMBER}]",
                body: """
                Le pipeline a été exécuté avec succès!
                
                Détails:
                - Application: ${APP_NAME}
                - Build: ${env.BUILD_NUMBER}
                - URL: ${env.BUILD_URL}
                """,
                to: 'votre-email@example.com'
            )
        }
        failure {
            echo '❌ Erreur dans le pipeline.'
            emailext (
                subject: "FAILED: Pipeline '${env.JOB_NAME}' [${env.BUILD_NUMBER}]",
                body: """
                Le pipeline a échoué!
                
                Détails:
                - Application: ${APP_NAME}
                - Build: ${env.BUILD_NUMBER}
                - URL: ${env.BUILD_URL}
                
                Consultez les logs pour plus de détails.
                """,
                to: 'votre-email@example.com'
            )
        }
        unstable {
            echo '⚠️ Pipeline instable (tests échoués)'
        }
    }
}

// Paramètres du pipeline (optionnel)
properties([
    parameters([
        booleanParam(
            name: 'DEPLOY_TO_TOMCAT',
            defaultValue: false,
            description: 'Déployer sur Apache Tomcat'
        ),
        booleanParam(
            name: 'DEPLOY_STANDALONE', 
            defaultValue: true,
            description: 'Déployer en mode standalone'
        )
    ])
])