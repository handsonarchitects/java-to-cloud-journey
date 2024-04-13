#!/bin/bash

set -xe

helm uninstall pdp -n pdp || true

./application/code/gradlew -p ./application/code build-docker --info
kind load docker-image handsonarchitects/knotx-demo-application

kubectl create namespace pdp || true
helm -n pdp upgrade --install pdp examples/pdp
kubectl -n pdp rollout status deployment pdp-knotx

curl http://127.0.0.1.nip.io/api/v1/example
