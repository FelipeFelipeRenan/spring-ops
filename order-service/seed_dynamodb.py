import boto3
from botocore.exceptions import ClientError
from decimal import Decimal, ROUND_HALF_UP
from faker import Faker

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

# Inicializar o Faker
fake = Faker()

# Gerar dados fictícios
def generate_fake_orders(n):
    orders = []
    for i in range(n):
        order = {
            'orderId': f'{i+1}',
            'product': fake.word(),
            'quantity': fake.random_int(min=1, max=10),
            'price': Decimal(str(round(fake.random_number(digits=2, fix_len=True) + fake.random.random(), 2))).quantize(Decimal('0.01'), rounding=ROUND_HALF_UP)
        }
        orders.append(order)
    return orders

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

def seed_data(orders):
    table = dynamodb.Table(table_name)
    for order in orders:
        try:
            table.put_item(Item=order)
            print(f"Added order: {order['orderId']}")
        except ClientError as e:
            print(f"Unable to add order: {order['orderId']} - {e.response['Error']['Message']}")

if __name__ == "__main__":
    create_table()
    fake_orders = generate_fake_orders(50)  # Gerar 50 pedidos fictícios
    seed_data(fake_orders)
