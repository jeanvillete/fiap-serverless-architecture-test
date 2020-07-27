#!/bin/bash

if [[ $# -ne 1 ]]; then
  echo -e "\tParameter \"bucket name\" is mandatory."
  echo -e "\tSample usage; $ ./sam-pack-and-deploy.sh \"fiap-serverless-arch\""
  exit 1
fi

BUCKET_NAME="$1"
outputPackaged="packaged.yaml"

aws s3 mb "s3://$BUCKET_NAME"

echo "empacotando funções lambda e disponibilizando instruções de deploy no artefato $outputPackaged"
sam package --template-file template.yaml --output-template-file "$outputPackaged" --s3-bucket "$BUCKET_NAME"

echo "efetuando deploy"
sam deploy --template-file "$outputPackaged" --stack-name "$BUCKET_NAME" --capabilities CAPABILITY_IAM

echo "endereço exposto para as funções;"
aws cloudformation describe-stacks --stack-name sam-orderHandler --query 'Stacks[].Outputs'