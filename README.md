# Desenvolvimento Local
  
1 - Criar diretório para volume do banco dynamodb no host local, e iniciar container ***aws dynamodb***;

    $ export DYNAMODB_HOME="$HOME/.dynamodb/local"
    $ mkdir -p "$DYNAMODB_HOME"
    $ docker run -d \
        -p 8000:8000 \
        -v "$DYNAMODB_HOME:/data/" \
        --name dynamodb \
        amazon/dynamodb-local -jar DynamoDBLocal.jar -sharedDb -dbPath /data
  
2 - Criação tabela dynamodb;

    $ aws dynamodb create-table \
        --table-name trip_mgnt \
        --attribute-definitions \
            AttributeName=partition,AttributeType=S \
            AttributeName=uuid,AttributeType=S \
            AttributeName=date,AttributeType=S \
            AttributeName=country,AttributeType=S \
        --key-schema \
            AttributeName=partition,KeyType=HASH \
            AttributeName=uuid,KeyType=RANGE \
        --local-secondary-indexes \
            'IndexName=dateLSI,KeySchema=[{AttributeName=partition,KeyType=HASH},{AttributeName=date,KeyType=RANGE}],Projection={ProjectionType=ALL}' \
            'IndexName=countryLSI,KeySchema=[{AttributeName=partition,KeyType=HASH},{AttributeName=country,KeyType=RANGE}],Projection={ProjectionType=ALL}' \
        --billing-mode PAY_PER_REQUEST \
        --endpoint-url http://localhost:8000
        
3 - Rodar api localmente via SAM CLI

- Linux; ``` $ sam local start-api --env-vars $(./sam-env-vars.sh "127.0.0.1" ) ```
- Windows; ``` $ sam local start-api --env-vars $(./sam-env-vars.sh "docker.for.windows.localhost" ) ```
- MacOS; ``` $ sam local start-api --env-vars $(./sam-env-vars.sh "docker.for.mac.localhost" ) ```

**Nota:** Para rodar em ambientes virtuais, como no **Cloud9** da AWS, não funcionou fornecer o 
 **ip de loopback 127.0.0.1** para o **SAM CLI**, eu tive que obter o meu ip público e então invocar;
``` $ sam local start-api --env-vars $(./sam-env-vars.sh "$MEU_IP_PUBLICO" ) ```