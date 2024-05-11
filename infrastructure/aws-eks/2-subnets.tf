### Private Subnets ###

resource "aws_subnet" "private_subnet_1" {
  vpc_id            = aws_vpc.vpc.id
  cidr_block        = var.private_subnet_1
  availability_zone = var.availibilty_zone_1

  tags = {
    "Name"                                      = "jug-demo-eks-private-1"
    # used by EKS to select subnets to create private load balancers
    "kubernetes.io/role/internal-elb"           = "1"
    "kubernetes.io/cluster/${var.cluster_name}" = "owned"
  }
}

resource "aws_subnet" "private_subnet_2" {
  vpc_id            = aws_vpc.vpc.id
  cidr_block        = var.private_subnet_2
  availability_zone = var.availibilty_zone_2

  tags = {
    "Name"                                      = "jug-demo-eks-private-2"
    # used by EKS to select subnets to create private load balancers
    "kubernetes.io/role/internal-elb"           = "1"
    "kubernetes.io/cluster/${var.cluster_name}" = "owned"
  }
}

### Public Subnets ###

resource "aws_subnet" "public_subnet_1" {
  vpc_id                  = aws_vpc.vpc.id
  cidr_block              = var.public_subnet_1
  availability_zone       = var.availibilty_zone_1
  map_public_ip_on_launch = true

  tags = {
    "Name"                                      = "jug-demo-eks-public-1"
    # used by EKS to select subnets to create public load balancers
    "kubernetes.io/role/elb"                    = "1"
    "kubernetes.io/cluster/${var.cluster_name}" = "owned"
  }
}

resource "aws_subnet" "public_subnet_2" {
  vpc_id                  = aws_vpc.vpc.id
  cidr_block              = var.public_subnet_2
  availability_zone       = var.availibilty_zone_2
  map_public_ip_on_launch = true

  tags = {
    "Name"                                      = "jug-demo-eks-public-2"
    # used by EKS to select subnets to create public load balancers
    "kubernetes.io/role/elb"                    = "1"
    "kubernetes.io/cluster/${var.cluster_name}" = "owned"
  }
}
