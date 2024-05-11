resource "aws_security_group" "alb_sg" {
    vpc_id = aws_vpc.vpc.id
    name = "demo-sg-alb"
    description = "Security group for alb"
    revoke_rules_on_delete = true
}

resource "aws_security_group_rule" "alb_http_ingress" {
    type = "ingress"
    from_port = 80
    to_port = 80
    protocol = "TCP"
    description = "Allow http inbound traffic from internet"
    security_group_id = aws_security_group.alb_sg.id
    cidr_blocks = ["0.0.0.0/0"] 
}

resource "aws_security_group_rule" "alb_egress" {
    type = "egress"
    from_port = 0
    to_port = 0
    protocol = "-1"
    description = "Allow outbound traffic from alb"
    security_group_id = aws_security_group.alb_sg.id
    cidr_blocks = ["0.0.0.0/0"] 
}