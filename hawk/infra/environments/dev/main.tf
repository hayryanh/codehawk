module "vpc" {
  source = "../../modules/vpc"
  name = var.vpc_name
  cidr = var.cidr

  azs             = var.azs
  private_subnets = var.private_subnets
  public_subnets  = var.public_subnets

  enable_nat_gateway = var.enable_nat_gateway
  enable_vpn_gateway = var.enable_vpn_gateway
}

module "eks" {
  source = "../../modules/eks"
  cluster_name = ""
  node_group_desired_capacity = 0
  private_subnets = []
  vpc_id = ""
}

module "ecr" {
  source = "../../modules/ecr"
}

module "iam" {
  source       = "../../modules/iam"
  cluster_name = ""
  provider_arn = ""
  thumbprint   = ""
}