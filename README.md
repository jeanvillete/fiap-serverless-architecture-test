###### Escola: FIAP
###### Curso: MBA FULLSTACK DEVELOPER, MICROSERVICES, CLOUD & IoT
###### Aluno / RM: JEAN BRUNO SOUTO VILLETE / 335435
###### Disciplina: Serverless Architecture
###### Professor: Peterson de Oliveira Larentis

# Domínio

O domínio base da aplicação é Trip (viagem), onde é possível fazer requisições RESTful para gerir este recurso.  
Os dados que compõem o domínio Trip são;
- country: String, min length 2, max length 35
- city: String, min length 2, max length 50
- date: LocalDate; yyyy/MM/dd
- reason: String, min length 10, max length 150

# Casos de Uso

Os casos de uso mapeados para lidar com este recurso são;

#### Adição de uma nova viagem

    $ curl "http://$targetHost/trips" \
        -d '{"country":"brasil", "city":"florianopolis", "date": "2012/11/01", "reason": "como diz bob esponja, lugar MARAVILINDO"}' \
        -H 'Content-Type: application/json'

#### Listagem de viagens por período, fornecendo inicio e fim via *query string*

    # onde start=2010%2F01%2F01&end=2016%2F12%2F31 é url encode para start=2010/01/01 & end=2016/12/31
    $ curl "http://$targetHost/trips?start=2010%2F01%2F01&end=2016%2F12%2F31"

#### Listagem de viagens por país, fornecendo o nome do país via *path variable*

    $ curl "http://$targetHost/trips/brasil"

#### Listagem de viagens por país e filtro *contains* para cidade, fornecendo o nome do país via *path variable* e cidade via *query string*

    $ curl "http://$targetHost/trips/brasil/?city=flori"

# Desenvolvimento Local
  
1 - Criar diretório para volume do banco dynamodb no host local, e iniciar container ***aws dynamodb***;

    $ export DYNAMODB_HOME="$HOME/.dynamodb/local"
    $ mkdir -p "$DYNAMODB_HOME"
    $ docker run -d \
        -p 8000:8000 \
        -v "$DYNAMODB_HOME:/data/" \
        --name dynamodb \
        amazon/dynamodb-local -jar DynamoDBLocal.jar -sharedDb -dbPath /data

#

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

#
        
3 - Rodar api localmente via SAM CLI

- Linux; ``` $ sam local start-api --env-vars $(./sam-env-vars.sh "127.0.0.1" ) ```
- Windows; ``` $ sam local start-api --env-vars $(./sam-env-vars.sh "docker.for.windows.localhost" ) ```
- MacOS; ``` $ sam local start-api --env-vars $(./sam-env-vars.sh "docker.for.mac.localhost" ) ```

**Nota:** Para rodar em ambientes virtuais, como no **Cloud9** da AWS, não funcionou fornecer o 
 **ip de loopback 127.0.0.1** para o **SAM CLI**, eu tive que obter o meu ip público e então invocar;
``` $ sam local start-api --env-vars $(./sam-env-vars.sh "$MEU_IP_PUBLICO" ) ```

#

4 - Execução de testes integrados

Foi disponibilizado um bash script denominado ` usecase-tests.sh `, onde é apresentado uma série de comandos com ` curl `
e um breve comentário do estado esperado antes e depois da execução dos passos.

    $ ./usecase-tests.sh