node {
   def gitRepositoryUrl="https://github.com/jrierapeiro/node_web_app.git"
   def appFileName
   def latestImageVersion
   stage('Preparation') {
        def exists = fileExists 'src'
        if (exists){
            sh 'rm -rf src'
        }
        new File('src').mkdir()
        
        dir ('src') {
            checkout([$class: 'GitSCM', branches: [[name: '*/version/aws-eb']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: gitRepositoryUrl]]])          
        }
   }
   
    nvm('version':'8.9.1') {
        stage('Version') {
            echo 'Update app version'
            latestImageVersion= "0.0.$BUILD_NUMBER";
            sh "sed -i -e \"0,/0.0.0/s//$latestImageVersion/\" src/package.json"            
            echo "cat src/package.json"
        }

        stage('Build') {
            dir('src'){    
                echo "NPM install"
                sh 'npm install'
                echo "NPM test"
            }
        }
        
        stage('Zip') {
            dir('src'){
                sh 'rm -rf .git'
                appFileName="app-${env.BUILD_NUMBER}.zip"
                sh "zip -r $appFileName ."
            }
        }
    }
    
    def bucketName="jariepei-elasticbeanstalk-zips"
    stage('Push zip to S3 bucket'){
        sh "aws s3 cp src/${appFileName} s3://${bucketName}/"
    }
    
    stage('EB create app version'){
        sh "aws elasticbeanstalk --region eu-west-1 create-application-version --application-name \"NodeApp\" --version-label \"${env.BUILD_NUMBER}\" --source-bundle S3Bucket=\"${bucketName}\",S3Key=\"${appFileName}\""
    }    

    stage('Update environment'){
      sh "aws elasticbeanstalk --region eu-west-1 update-environment --application-name \"NodeApp\" --environment-name \"nodeapp-testing\" --version-label \"${env.BUILD_NUMBER}\""
    }
}