# Run stress tests on a running container

- Ejecutar la docker image creada en el ejemplo 05
  - docker run -d -p 7000:7000 $IMAGE_NAME
- Instalar los modulos necesarios para los test de rendimiento/estres
  - npm install
- Definir el TEST_HOST para los tests
  - export TEST_HOST="http://127.0.0.1:7000"
- Ejecutar los test
  - npm run stress
- Analizar los resultados
- Ver los logs del container en ejecución