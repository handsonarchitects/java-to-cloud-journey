# Infrastructure

This directory contains the infrastructure code for the project.

## Modules

### `aws-eks`

This module contains the Terraform code for creating an EKS cluster on AWS.

EKS uses Fargate for running the pods, so there is no need to create EC2 instances for the cluster.

Check [Create AWS EKS Fargate Using Terraform](https://antonputra.com/amazon/create-aws-eks-fargate-using-terraform/
) for more detailed technical information.

### `tf-state`

This module contains the Terraform code for creating the S3 bucket and DynamoDB table used for storing the Terraform state.
