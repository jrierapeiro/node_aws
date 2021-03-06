
node {
   def gitRepositoryUrl="https://github.com/jrierapeiro/node_aws.git"
   def lambdaFunction="searchItem"
   def lambdaCodeSource="src/lambda/searchAPI"
   def appFileName
   def latestImageVersion
   
   stage('Preparation') {
        def exists = fileExists 'src'
        if (exists){
            sh 'rm -rf src'
        }
        new File('src').mkdir()
        
        dir ('src') {
            checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: gitRepositoryUrl]]])          
        }
   }
   
    nvm('version':'6.10') {
        stage('Version') {
            echo 'Update app version'
            latestImageVersion= "0.0.$BUILD_NUMBER";
            sh "sed -i -e \"0,/0.0.0/s//$latestImageVersion/\" ${lambdaCodeSource}/package.json"            
            echo "cat ${lambdaCodeSource}/package.json"
        }

        stage('Build') {
            dir(lambdaCodeSource){    
                echo "NPM install"
                // sh 'npm install'
                echo "NPM test"
                // sh 'npm test'
            }
        }
        
        stage('Zip') {
            dir(lambdaCodeSource){               
                appFileName="app-${env.BUILD_NUMBER}.zip"
               sh "zip -r $appFileName index.js"
            }
        }
    }
    
    stage('Update Function code'){
       dir(lambdaCodeSource){  
        sh "aws lambda --region eu-west-1 update-function-code --function-name ${lambdaFunction} --zip-file fileb://${appFileName}"
       }
    }
}