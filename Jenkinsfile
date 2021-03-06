pipeline {
  agent {
    docker {
      image 'maven'
      args '-v /var/run/docker.sock:/var/run/docker.sock -v ${PWD}/.m2:/root/.m2 --network clustercontrol-net'
    }    
  }
  
  environment {
    SETTINGS_XML = credentials('settings.xml')
  }

  stages {
    stage('Create environment') {
      steps {
        sh 'curl -sX POST http://clustercontrol:8080/marketplace/deploy/kafka/1.0.0?wait=true -H "Content-Type: application/json" -H "Accept: text/html" -d "{\\"namespace\\":\\"$HOSTNAME\\", \\"KAFKA_MUTEX\\":\\"28888\\", \\"ZOOKEEPER_MUTEX\\":\\"28889\\"}"'
      }
    }
    stage('Clean') {
      steps {
        sh 'mvn clean:clean'
      }
    }
    stage('Resources') {
      steps {
        sh 'mvn resources:resources resources:testResources'
      }
    }
    stage('Compile') {
      steps {
        sh 'mvn compiler:compile compiler:testCompile'
      }
    }
    stage('Connect to environment') {
      steps {
        sh 'curl --unix-socket /var/run/docker.sock -X POST http:/v1.33/networks/kafka-net-$HOSTNAME/connect -H "Content-Type: application/json" -d "{\\"Container\\":\\"$HOSTNAME\\"}"'
      }
    }
    stage('Test') {
      steps {
        sh 'mvn -DautoOffsetReset=earliest -DbrokerList=kafka-1-$HOSTNAME:9092,kafka-2-$HOSTNAME:9092,kafka-3-$HOSTNAME:9092 surefire:test'
      }
      post {
        always {
          sh 'curl --unix-socket /var/run/docker.sock -X POST http:/v1.33/networks/kafka-net-$HOSTNAME/disconnect -H "Content-Type: application/json" -d "{\\"Container\\":\\"$HOSTNAME\\",\\"force\\":true}"'
          sh 'sleep 5'
          sh 'curl -sX POST http://clustercontrol:8080/marketplace/undeploy/kafka/1.0.0?wait=true -H "Content-Type: application/json" -H "Accept: text/html" -d "{\\"namespace\\":\\"$HOSTNAME\\"}"'            
        }
      }
    }
    stage('Package') {
      steps {
        sh 'mvn jar:jar shade:shade'
      }
    }
    stage('Image') {
      steps {
        sh 'mvn -s $SETTINGS_XML docker:build -DpushImage'
      }
    }
  } 
  
  post {
    always {
      junit 'target/surefire-reports/*.xml'    
    }
  }
}
