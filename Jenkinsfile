pipeline {
    agent any
    environment {
        PATH = "$M2_HOME"
    }
     stages
     {
         stage('Scan') {
                steps {
                    withSonarQubeEnv(installationName: 'sq1')
                    {
                    sh ' mvnw clean package sonar:sonar '
                    }
                }
            }
        stage('Building') {
            steps {
                echo 'The Code will be now be built into an artifact'
            }
        }
        stage('Artifact Archiving') {
            steps {
                echo 'The Artifact will be uploaded to an artifact repository'
            }
        }
        stage('Testing') {
            steps {
                echo 'The Artifact will be tested'
            }
        }
        stage('Staging') {
            steps {
                echo 'The Artifact is staged onto the staging server'
            }
        }

        stage('Deploy') {
            steps {
                echo 'The software will now be deployed!'
            }
        }
    }
}
