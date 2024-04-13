#!/bin/bash

set -xe

kind delete cluster || true

kind create cluster --config .github/kind.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml

kubectl -n ingress-nginx rollout status deployment ingress-nginx-controller
