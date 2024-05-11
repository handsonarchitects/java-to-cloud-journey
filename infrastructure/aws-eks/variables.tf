variable "region" {
    type = string
    description = "Main region for all resources"
}

variable "cluster_name" {
    type = string
    description = "Name of the EKS cluster"
}

variable "availibilty_zone_1" {
    type = string
    description = "First availibility zone"
}

variable "availibilty_zone_2" {
    type = string
    description = "Second availibility zone"
}

variable "vpc_cidr" {
    type = string
    description = "CIDR block for the main VPC"
}

variable "public_subnet_1" {
    type = string
    description = "CIDR block for public subnet 1"
}

variable "public_subnet_2" {
    type = string
    description = "CIDR block for public subnet 2"
}

variable "private_subnet_1" {
    type = string
    description = "CIDR block for private subnet 1"
}

variable "private_subnet_2" {
    type = string
    description = "CIDR block for private subnet 2"
}

variable "default_tags" {
    type = map
    default = {
        Application = "Cleeng"
        Environment = "Demo"
    }
}

variable "container_port" {
  description = "Port that needs to be exposed for the application"
}