resource "aws_instance" "webserver" {
	ami = var.aws_ami
	instance_type = var.aws_instance_type
	tags = {
		Terraform = "True"
		Purpose  = "1st Simple attempt at a server"
	}
}
