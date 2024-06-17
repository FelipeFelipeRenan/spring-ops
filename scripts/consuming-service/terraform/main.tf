provider "aws" {
  access_key = "test"
  secret_key = "test"
  region     = "us-east-1"
  skip_credentials_validation = true
  skip_metadata_api_check = true
  skip_requesting_account_id = true
  endpoints {
    dynamodb = "http://localhost:4566"
    sqs      = "http://localhost:4566"
  }
}

resource "aws_dynamodb_table" "my_table" {
  name         = "my_table"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "id"

  attribute {
    name = "id"
    type = "S"
  }
}

resource "aws_sqs_queue" "my_queue" {
  name = "my_queue"
}
