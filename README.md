# Desenvolvimento Local
  
Criação tabela dynamodb

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