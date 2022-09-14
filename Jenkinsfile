pipeline {
    agent any
     stages {
         stage('Build') {
                 // Run the maven build
                 withEnv(["MVN_HOME=$mvnHome"]) {
                     if (isUnix()) {
                         sh '"$MVN_HOME/bin/mvn" -Dmaven.test.failure.ignore clean package'
                     } else {
                         bat(/"%MVN_HOME%\bin\mvn" -Dmaven.test.failure.ignore clean package/)
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