variable "cluster_name" {
  description = "EKS cluster name"
  type        = string
}

variable "provider_arn" {
    description = "The ARN of the IAM provider"
    type        = string
}

variable "thumbprint" {
    description = "The thumbprint of the IAM provider"
    type        = string
}