module "iam_eks_role" {
  source      = "terraform-aws-modules/iam/aws//modules/iam-eks-role"

  role_name   = "hawk-app"

  cluster_service_accounts = {
    "cluster1" = ["default:hawk-app"]
    "cluster2" = [
      "default:hawk-app",
      "canary:hawk-app",
    ]
  }

  tags = {
    Name = "eks-role"
  }

  role_policy_arns = {
    AmazonEKS_CNI_Policy = "arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy"
  }
}