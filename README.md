# Desenvolvimento Local
  
1 - Confirmar que o container do dynamodb está rodando localmente;

    $ docker run -p 8000:8000 -v $(pwd)/local/dynamodb:/data/ amazon/dynamodb-local -jar DynamoDBLocal.jar -sharedDb -dbPath /data
  
2 - Criação tabela dynamodb;

    $ aws dynamodb create-table \
        --table-name trip_mgnt \
        --attribute-definitions \
            AttributeName=country,AttributeType=S \
            AttributeName=date,AttributeType=S \
            AttributeName=city,AttributeType=S \
            AttributeName=reason,AttributeType=S \
        --key-schema \
            AttributeName=country,KeyType=HASH \
            AttributeName=date,KeyType=RANGE \
        --local-secondary-indexes \
            'IndexName=cityLSI,KeySchema=[{AttributeName=country,KeyType=HASH},{AttributeName=city,KeyType=RANGE}],Projection={ProjectionType=ALL}' \
        --billing-mode PAY_PER_REQUEST \
        --endpoint-url http://localhost:8000
        
3 - Rodar api localmente via SAM CLI

- Linux; ``` $ sam local start-api --env-vars $(./sam-env-vars.sh "127.0.0.1" ) ```
- Windows; ``` $ sam local start-api --env-vars $(./sam-env-vars.sh "docker.for.windows.localhost" ) ```
- MacOS; ``` $ sam local start-api --env-vars $(./sam-env-vars.sh "docker.for.mac.localhost" ) ```

**Nota:** Para rodar em ambientes virtuais, como no **Cloud9** da AWS, não funcionou fornecer o 
 **ip de loopback 127.0.0.1** para o **SAM CLI**, eu tive que obter o meu ip público e então invocar;
``` $ sam local start-api --env-vars $(./sam-env-vars.sh "$MEU_IP_PUBLICO" ) ```