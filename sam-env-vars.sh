#!/bin/bash

if [[ $# -ne 1 ]]; then
  echo -e "\tParameter \"to ip\" is mandatory."
  echo -e "\tSample usage; $ ./sam-env-vars.sh \"127.0.0.1\""
  exit 1
fi

toip="$1"
tmpoutput="./envvars.tmp"
envvars=$( cat <<EOF
{
  "CreateATripRecordFunction": {
    "ENDPOINT_OVERRIDE": "http://$toip:8000",
    "TABLE_NAME": "trip_mgnt"
  },
  "GetTripRecordsByPeriodFunction": {
    "ENDPOINT_OVERRIDE": "http://$toip:8000",
    "TABLE_NAME": "trip_mgnt"
  }
}
EOF
)

echo -n "$envvars" > "$tmpoutput"

echo -n "$tmpoutput"