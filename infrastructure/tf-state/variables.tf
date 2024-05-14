variable "region" {
    type = string
    description = "Main region for all resources"
}

variable "bucket_name" {
    type = string
    description = "Name of the S3 bucket for Terraform state"
}

variable "default_tags" {
    type = map
    default = {
        Application = "JUG"
        Environment = "Demo"
    }
}