#!/bin/bash

set -xe

if [ "$1" == "-b" ]; then
  ./application/code/gradlew -p ./application/code build-docker --info
  kind load docker-image handsonarchitects/knotx-demo-application
fi

kubectl create namespace pdp || true

helm dependency update examples/pdp
helm -n pdp upgrade --install pdp examples/pdp

kubectl -n pdp rollout status deployment

curl -i http://127.0.0.1.nip.io/api/v1/example
curl -i http://127.0.0.1.nip.io/content/index.html
