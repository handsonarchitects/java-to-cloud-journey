# Infrastructure

This directory contains the infrastructure code for the project.

## Modules

### `aws-eks`

This module contains the Terraform code for creating an EKS cluster on AWS.

EKS spin up the control plane and worker nodes in the private subnets. The worker nodes are managed by Node Group, which is a group of EC2 instances.

#### Kubectl Context

After creating the EKS cluster, you need to update the `kubeconfig` file to use the new cluster. You can do this by running the following command:

```bash
aws eks --region eu-central-1 update-kubeconfig --name jug-demo-eks
```

### `tf-state`

This module contains the Terraform code for creating the S3 bucket and DynamoDB table used for storing the Terraform state.
