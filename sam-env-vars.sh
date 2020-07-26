#!/bin/bash

if [[ $# -ne 1 ]]; then
  echo -e "\tParameter \"to ip\" is mandatory."
  exit 1
fi

toip="$1"
envvars=$( cat <<EOF
{
  "CreateATripRecordFunction": {
    "ENDPOINT_OVERRIDE": "http://$toip:8000",
    "TABLE_NAME": "trip_mgnt"
  }
}
EOF
)

echo "$envvars"