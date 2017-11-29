# Node.js - AWS

## AWS console

Login: https://aws.amazon.com/console/

### VPC - Virtual Private Cloud‎

Creamos una VPC:
- Nombre: Testing
- CIDR (Rango de IPs)
- Tenancy (default)

### IGW - Internet Gateway

Puerta a internet para la nueva VPC.
Creamos: 
- Nombre: IGW-testing
Asignamos el IGW a la VPC
- Attach to VPC: testing

### Subnet

Creamos una subnet y asignamos el m'aimo CIDR (no vamos a trabajar con varias)

### Route table

Comprobamos que hay una Route table creada para la nueva VPC (testing).
Editamos la tabla y añadimos la entrada 0.0.0.0/0 que apunta al IGW.
Asociar la tabla con la subnet que hemos creado.

### Security Groups

Comprobamos que hay un security group asociado a la subnet y que esta permitido todo el trafico de entrada y salida por defecto. En ese punto, si permitimos solo el trafico de entrada por el puerto 80, el trafico de salida por el mismo puerto es habilitado a su vez.

### NACL - Network access control list

Comprobamos que hay un NACL creado y asignado a la subnet y que permite todo el trafico de entrada y salida. En este punto, si permitimos solo el trafico de entrada por el puerto 80, el trafico de salida por el mismo puerto NO es habilitado, hay que permitirlo manualmente.

### EC2

Creamos una EC2 instance donde vamos a publicar la aplicion en Node.js
Amazon linux - Free tier
Instance type: Geenral purpose - t2.micro
Configure instance details:
- Auto assign public IP : true 
- IAM role: None (por el momemnto no queremos acceso a otros servicios desde la instance)

User data (script que se ejecuta al arrancar):

Storage: Default
Tags:
- Add name: testing-box
Security:
- change the name to testing-box
- Add the TCP port 3000 for the nodejs app

Key pair:
- Create a new one and download the key
- chmod 400 key

Login en la nueva instance:
ssh -i testing-box.pem ec2-user@IP


Manual configuration

sudo yum install git -y
curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.33.6/install.sh | bash
. ~/.nvm/nvm.sh
nvm install --lts
npm install pm2 -g

git clone https://github.com/jrierapeiro/node_web_app
cd node_web_app
npm install
npm start

Note: https://github.com/Unitech/pm2


PM2
ssh-keyscan -t rsa github.com >> ~/.ssh/known_hosts

pm2 deploy ecosystem.json production setup
pm2 deploy ecosystem.json production

//TODO: Review PM2 tools


### Setup jenkins in AWS
sudo yum update -y
sudo wget -O /etc/yum.repos.d/jenkins.repo http://pkg.jenkins-ci.org/redhat/jenkins.repo
sudo rpm --import https://pkg.jenkins.io/redhat/jenkins.io.key
sudo yum install jenkins -y
sudo yum install java-1.8.0
sudo yum remove java-1.7.0-openjdk
sudo service jenkins start
sudo cat /var/lib/jenkins/secrets/initialAdminPassword

### ELB


### Elastic Beanstalk
Create an application called NodeApp - Webapp

#### IAM 

Role to access the services

#### S3
Create a S3 bucket to store the versions

#### Jenkins pipeline
Jenkins pipelie using aws cli


### Lambda

### Codebuild

### Route53