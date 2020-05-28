/*
Copyright 2020 Vinay Lodha (https://github.com/vinay-lodha)
 
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
http://www.apache.org/licenses/LICENSE-2.0
 
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions andR
limitations under the License.
*/
provider "aws" {
  region = "us-east-2"
}

data "aws_ami" "ubuntu" {
  most_recent = true

  filter {
    name = "name"
    values = [
      "ubuntu/images/hvm-ssd/ubuntu-trusty-14.04-amd64-server-*"]
  }

  filter {
    name = "virtualization-type"
    values = [
      "hvm"]
  }

  owners = [
    "099720109477"]
  # Canonical
}

resource "aws_instance" "one" {
  ami = "${data.aws_ami.ubuntu.id}"
  instance_type = "t2.micro"

  tags = {
    owner = "vinay"
  }
}


resource "aws_instance" "two" {
  ami = "${data.aws_ami.ubuntu.id}"
  instance_type = "t3a.micro"

  tags = {
    owner = "vinay"
  }
}

resource "aws_instance" "three" {
  ami = "${data.aws_ami.ubuntu.id}"
  instance_type = "m5.large"

  tags = {
    owner = "vinay"
  }
}

resource "aws_elastic_beanstalk_application" "older_gen_instance_beanstalk_application" {
  name = "older_gen_instance_beanstalk_application"
  description = "older_gen_instance_beanstalk_application"
  tags = {
    owner = "vinay"
  }
}

resource "aws_elastic_beanstalk_environment" "older_gen_instance_beanstalk_application_env" {
  name = "env-older-gen"
  application = "${aws_elastic_beanstalk_application.older_gen_instance_beanstalk_application.name}"
  solution_stack_name = "64bit Amazon Linux 2 v3.0.1 running Corretto 11"

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name = "InstanceType"
    value = "t3.micro"
  }
  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name = "IamInstanceProfile"
    value = "aws-elasticbeanstalk-ec2-role"
  }

  tags = {
    owner = "vinay"
  }
}

