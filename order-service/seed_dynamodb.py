import boto3
from botocore.exceptions import ClientError
from decimal import Decimal

# Configuração do cliente DynamoDB
dynamodb = boto3.resource(
    'dynamodb',
    endpoint_url='http://localhost:4566',
    region_name='us-east-1',
    aws_access_key_id='test',
    aws_secret_access_key='test'
)

# Nome da tabela
table_name = 'Orders'

# Estrutura da tabela
table_definition = {
    'TableName': table_name,
    'KeySchema': [
        {
            'AttributeName': 'orderId',
            'KeyType': 'HASH'  # Partition key
        }
    ],
    'AttributeDefinitions': [
        {
            'AttributeName': 'orderId',
            'AttributeType': 'S'
        }
    ],
    'ProvisionedThroughput': {
        'ReadCapacityUnits': 10,
        'WriteCapacityUnits': 10
    }
}

# Dados para inserir
orders = [
    {
        'orderId': '1',
        'product': 'Product A',
        'quantity': 2,
        'price': Decimal('10.0')
    },
    {
        'orderId': '2',
        'product': 'Product B',
        'quantity': 1,
        'price': Decimal('20.0')
    },
    {
        'orderId': '3',
        'product': 'Product C',
        'quantity': 3,
        'price': Decimal('15.0')
    }
]

def create_table():
    try:
        table = dynamodb.create_table(**table_definition)
        table.wait_until_exists()
        print(f"Table {table_name} created successfully.")
    except ClientError as e:
        if e.response['Error']['Code'] == 'ResourceInUseException':
            print(f"Table {table_name} already exists.")
        else:
            print(f"Unable to create table: {e.response['Error']['Message']}")

def seed_data():
    table = dynamodb.Table(table_name)
    for order in orders:
        try:
            table.put_item(Item=order)
            print(f"Added order: {order['orderId']}")
        except ClientError as e:
            print(f"Unable to add order: {order['orderId']} - {e.response['Error']['Message']}")

if __name__ == "__main__":
    create_table()
    seed_data()
