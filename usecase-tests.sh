#!/bin/bash

if [[ $# -ne 1 ]]; then
  echo -e "\tParameter \"host\" is mandatory. Port might be provided if required."
  echo -e "\tSample usage; $ ./usecase-test.sh \"127.0.0.1:3000\""
  exit 1
fi

alias curl='curl -w "\n ---- \n" -v'

targetHost="$1"

#### invoca registros de viagem com falhas de validações, pois o payload não está válido ####
# payload incompleto
curl "http://$targetHost/trips" -d '{}'
# reclama que falta o campo city
curl "http://$targetHost/trips" -d '{"country":"exemplo país"}'
# reclama que falta o campo date
curl "http://$targetHost/trips" -d '{"country":"exemplo país", "city":"exemplo cidade"}'
# campo date no formato inválido
curl "http://$targetHost/trips" -d '{"country":"exemplo país", "city":"exemplo cidade"}, "date": "2016-12-25"'
# reclama que falta o campo reason
curl "http://$targetHost/trips" -d '{"country":"exemplo país", "city":"exemplo cidade"}, "date": "2016/12/25"'
# campo country é inválido, pois precisa ter no mínimo 4 caracteres
curl "http://$targetHost/trips" -d '{"country":"usa", "city":"NY", "date": "2020/06/05", "reason": "só vontade mesmo"}'
# campo city é inválido, pois precisa ter no mínimo 4 caracteres
curl "http://$targetHost/trips" -d '{"country":"united states", "city":"NY", "date": "2020/06/05", "reason": "só vontade mesmo"}'
# campo reason é inválido, pois precisa ter no mínimo 10 caracteres
curl "http://$targetHost/trips" -d '{"country":"united states", "city":"new york city", "date": "2020/06/05", "reason": "ops"}'

# cria registros de viagem válidos
curl "http://$targetHost/trips" -d '{"country":"italy", "city":"milano", "date": "2016/12/25", "reason": "trabalhar em cliente estrangeiro"}'
curl "http://$targetHost/trips" -d '{"country":"brasil", "city":"goiânia", "date": "2019/12/20", "reason": "de volta a terra para ver familiares"}'
curl "http://$targetHost/trips" -d '{"country":"brasil", "city":"uberaba", "date": "2019/12/21", "reason": "no caminho indo para goiania"}'
curl "http://$targetHost/trips" -d '{"country":"brasil", "city":"uberlandia", "date": "2019/12/21", "reason": "no caminho indo para goiania"}'
curl "http://$targetHost/trips" -d '{"country":"brasil", "city":"belo horizonte", "date": "2019/12/21", "reason": "no caminho indo para goiania"}'
curl "http://$targetHost/trips" -d '{"country":"united states of america", "city":"new york", "date": "2019/06/01", "reason": "só na vontade, mas nunca fui"}'
curl "http://$targetHost/trips" -d '{"country":"argentina", "city":"buenos aires", "date": "2019/06/05", "reason": "dançar um tango e comer churrasco"}'
curl "http://$targetHost/trips" -d '{"country":"brasil", "city":"sorocaba", "date": "2019/06/07", "reason": "conhecendo os interiores de são paulo"}'
curl "http://$targetHost/trips" -d '{"country":"brasil", "city":"itu", "date": "2019/06/07", "reason": "conhecendo os interiores de são paulo"}'
curl "http://$targetHost/trips" -d '{"country":"brasil", "city":"campinas", "date": "2019/06/07", "reason": "conhecendo os interiores de são paulo"}'
