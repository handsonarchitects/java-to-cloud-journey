# Infrastructure
This directory contains the Terraform code for creating the AWS infrastructure for the project. It contains the following modules:

- `aws-eks`: Contains the Terraform code for creating an EKS cluster on AWS.
- `tf-state`: Contains the Terraform code for creating the S3 bucket and DynamoDB table used for storing the Terraform state.

## Prerequisites

- [Terraform](https://learn.hashicorp.com/tutorials/terraform/install-cli)
- [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/)
- [eksctl](https://eksctl.io/introduction/#installation)
- [aws-cli](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html)

## Terraform state management

> ![warning](https://img.shields.io/badge/-warning-red)  **Warning**: Do not store the Terraform state file in a local directory. The state file contains sensitive information about your infrastructure. It is recommended to store the state file in a remote location.

> ![info](https://img.shields.io/badge/-info-blue) **Info**: This modules must be created before creating the EKS cluster. However, it is created only once.

The Terraform state is stored in an S3 bucket and locked using a DynamoDB table. The state is stored in the `terraform.tfstate` file.

To create the S3 bucket and DynamoDB table, you need to run the following commands:

```bash
cd tf-state
terraform init
terraform apply
```

After creating the S3 bucket and DynamoDB table, you need to update the `0-provider.tf` file with the name of the S3 bucket and DynamoDB table.

```hcl
terraform {
  backend "s3" {
    bucket         = "2024-jug-terraform-state"
    key            = "terraform.tfstate"
    region         = "eu-central-1"
    dynamodb_table = "2024-jug-terraform-state-locks"
  }
}
```

Read more details about the Terraform state management [here](https://blog.gruntwork.io/how-to-manage-terraform-state-28f5697e68fa).

## Kubernetes Cluster and Integration with ALB Ingress Controller

EKS Cluster is created using the `aws-eks` module. The module creates the following resources:

- EKS Cluster
- Node Group
- Security Groups
- IAM Roles
- VPC
- Subnets
- Route Tables
- Internet Gateway
- NAT Gateway

### EKS Cluster

To create the EKS cluster, you need to run the following commands:

```bash
cd aws-eks
ssh-keygen -f bastion-ssh-key
terraform init
terraform apply
```

Once the EKS cluster is created, you need to update the `kubeconfig` file to use the new cluster:

```bash
aws eks --region eu-central-1 update-kubeconfig --name jug-demo-eks
```

### ALB Ingress Controller

Diagram of the ALB Ingress Controller is [here](https://aws.amazon.com/blogs/opensource/kubernetes-ingress-aws-alb-ingress-controller/).

In order to use the ALB Ingress Controller, you need to create an IAM policy:
```bash
curl -O https://raw.githubusercontent.com/kubernetes-sigs/aws-load-balancer-controller/v2.7.2/docs/install/iam_policy.json

aws iam create-policy --policy-name AWSLoadBalancerControllerIAMPolicy --policy-document file://iam_policy.json
```

After creating the IAM policy, you need to associate the OIDC provider with the EKS cluster and create an IAM role for the ALB Ingress Controller.
```bash
eksctl utils associate-iam-oidc-provider --region=eu-central-1 --cluster=jug-demo-eks --approve
eksctl create iamserviceaccount \
  --cluster=jug-demo-eks \
  --namespace=kube-system \
  --name=aws-load-balancer-controller \
  --role-name AmazonEKSLoadBalancerControllerRole \
  --attach-policy-arn=arn:aws:iam::610929429946:policy/AWSLoadBalancerControllerIAMPolicy \
  --override-existing-serviceaccounts \
  --approve
```

In the output of the command, you will see the `ServiceAccount` and `IAM Role` created. 

```bash
2024-05-11 21:46:46 [ℹ]  1 iamserviceaccount (kube-system/aws-load-balancer-controller) was included (based on the include/exclude rules)
2024-05-11 21:46:46 [!]  serviceaccounts that exist in Kubernetes will be excluded, use --override-existing-serviceaccounts to override
2024-05-11 21:46:46 [ℹ]  1 task: { 
    2 sequential sub-tasks: { 
        create IAM role for serviceaccount "kube-system/aws-load-balancer-controller",
        create serviceaccount "kube-system/aws-load-balancer-controller",
    } }2024-05-11 21:46:46 [ℹ]  building iamserviceaccount stack "eksctl-jug-demo-eks-addon-iamserviceaccount-kube-system-aws-load-balancer-controller"
2024-05-11 21:46:46 [ℹ]  deploying stack "eksctl-jug-demo-eks-addon-iamserviceaccount-kube-system-aws-load-balancer-controller"
2024-05-11 21:46:46 [ℹ]  waiting for CloudFormation stack "eksctl-jug-demo-eks-addon-iamserviceaccount-kube-system-aws-load-balancer-controller"
2024-05-11 21:47:17 [ℹ]  waiting for CloudFormation stack "eksctl-jug-demo-eks-addon-iamserviceaccount-kube-system-aws-load-balancer-controller"
2024-05-11 21:47:17 [ℹ]  created serviceaccount "kube-system/aws-load-balancer-controller"
```

You need to update the `alb-ingress-controller.yaml` file with the `ServiceAccount` and `IAM Role` created.

```yaml
helm install aws-load-balancer-controller eks/aws-load-balancer-controller \
  -n kube-system \
  --set clusterName=jug-demo-eks \
  --set serviceAccount.create=false \
  --set serviceAccount.name=aws-load-balancer-controller
```

You should see AWS Load Balancer Controller installed in the `kube-system` namespace.

```bash
kubectl get pods -n kube-system
```

More details can be found [here](https://docs.aws.amazon.com/eks/latest/userguide/lbc-helm.html).


### Verify the ALB Ingress Controller

Verify with this [example](https://docs.aws.amazon.com/eks/latest/userguide/alb-ingress.html#application-load-balancer-sample-application). Follow the steps with the `2048` game and private subnets.

Then check your bastion host public IP address and access the game with the following URL:
```bash
ssh -i bastion-ssh-key ubuntu@ec2-3-70-134-48.eu-central-1.compute.amazonaws.com
```
and run:
```bash
curl internal-k8s-game2048...
```

## Clean up

1. Uninstall all k8s resources
2. Delete CloudFormation stacks
3. Delete infrastructure using Terraform
