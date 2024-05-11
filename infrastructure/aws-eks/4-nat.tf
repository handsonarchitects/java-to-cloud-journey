# Elastic IP (EIP) for NAT Gateway
# to provide a static IP address for NAT Gateway
resource "aws_eip" "nateip" {
  domain = "vpc"
}

# Network Address Translation (NAT) Gateway
# from public subnet to internet
resource "aws_nat_gateway" "ngw" {
  allocation_id = aws_eip.nateip.id
  subnet_id     = aws_subnet.public_subnet_1.id
  depends_on    = [aws_internet_gateway.igw]
}
