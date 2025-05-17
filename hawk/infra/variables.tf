variable "cluster_name" {
  description = "EKS cluster name"
  type        = string
}

variable "node_group_desired_capacity" {
  description = "Desired capacity for the EKS managed node group"
  type        = number
}