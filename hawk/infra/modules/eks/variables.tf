variable "cluster_name" {
  description = "EKS cluster name"
  type        = string
}

variable "node_group_desired_capacity" {
  description = "Desired capacity for the EKS managed node group"
  type        = number
}

variable "vpc_id" {
  description = "VPC ID where the EKS cluster will be created"
  type        = string
}

variable "private_subnets" {
  description = "List of private subnets for the EKS cluster"
  type = list(string)
}