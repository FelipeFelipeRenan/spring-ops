import boto3
import time

# Configurar LocalStack com credenciais fictícias
boto3.setup_default_session(
    aws_access_key_id='test',
    aws_secret_access_key='test',
    region_name='us-east-1'
)
ec2 = boto3.client('ec2', endpoint_url='http://localhost:4566')

# Criar uma chave de acesso
key_pair = ec2.create_key_pair(KeyName='localstack-key')
with open('localstack-key.pem', 'w') as file:
    file.write(key_pair['KeyMaterial'])

# Configurar segurança
security_group = ec2.create_security_group(
    GroupName='localstack-sg', 
    Description='Security group for LocalStack EC2 instance'
)
ec2.authorize_security_group_ingress(
    GroupId=security_group['GroupId'],
    IpPermissions=[
        {
            'IpProtocol': 'tcp',
            'FromPort': 22,
            'ToPort': 22,
            'IpRanges': [{'CidrIp': '0.0.0.0/0'}]
        },
        {
            'IpProtocol': 'tcp',
            'FromPort': 8080,
            'ToPort': 8080,
            'IpRanges': [{'CidrIp': '0.0.0.0/0'}]
        }
    ]
)

# Lançar a instância
instance = ec2.run_instances(
    ImageId='ami-12345678',  # AMI fictícia, ajuste conforme necessário
    InstanceType='t2.micro',
    KeyName='localstack-key',
    SecurityGroupIds=[security_group['GroupId']],
    MinCount=1,
    MaxCount=1
)

instance_id = instance['Instances'][0]['InstanceId']
print(f'Instância EC2 lançada: {instance_id}')

# Esperar a instância estar em estado 'running'
while True:
    response = ec2.describe_instances(InstanceIds=[instance_id])
    state = response['Reservations'][0]['Instances'][0]['State']['Name']
    if state == 'running':
        print('Instância EC2 está em execução.')
        break
    time.sleep(5)

instance_ip = response['Reservations'][0]['Instances'][0]['PublicIpAddress']
print(f'Endereço IP da instância: {instance_ip}')
